package io.github.mike10004.nitsick.containment;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.ExecState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * Executor that polls until the execution has finished. Warning: you won't get any output with this one.
 */
class DetachedClientExecutor implements DockerExecutor {

    private final DockerClient docker;
    private final String containerId;
    private final Duration pollInterval;
    private final int maxNumPolls;

    public DetachedClientExecutor(DockerClient docker, String containerId) {
        this(docker, containerId, Duration.ofMillis(1000), 30);
    }

    public DetachedClientExecutor(DockerClient docker, String containerId, Duration pollInterval, int maxNumPolls) {
        this.docker = requireNonNull(docker);
        this.containerId = containerId;
        this.pollInterval = requireNonNull(pollInterval);
        this.maxNumPolls = maxNumPolls;
    }

    @Override
    public DockerExecResult<String> execute(String executable, String... args) throws ContainmentException {
        try {
            String[] cmd = ArrayUtil.prepend(args, executable);
            System.out.format("command: %s%n", Arrays.toString(cmd));
            ExecCreation execCreation = docker.execCreate(containerId, cmd, DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
            String execId = execCreation.id();
            Long exitCode;
            String stdout, stderr;
            try (ByteArrayOutputStream stdoutb = new ByteArrayOutputStream(256);
                 ByteArrayOutputStream stderrb = new ByteArrayOutputStream(256)) {
                try (LogStream output = docker.execStart(execId, DockerClient.ExecStartParameter.DETACH)) {
                    Poller.PollOutcome<Long> outcome = new Poller<Long>() {
                        @Override
                        protected PollAnswer<Long> check(int pollAttemptsSoFar) {
                            try {
                                ExecState state = docker.execInspect(execId);
                                checkState(state.openStdout() && state.openStderr(), "stdout/stderr not open: %s", state);
                                Boolean running;
                                if ((running = state.running()) != null) {
                                    if (running.booleanValue()) {
                                        return continuePolling();
                                    } else {
                                        checkState(state.exitCode() != null, "exit code null");
                                        return resolve(state.exitCode());
                                    }
                                } else {
                                    return continuePolling();
                                }
                            } catch (DockerException | InterruptedException e) {
                                return abortPolling(null);
                            }
                        }
                    }.poll(pollInterval, maxNumPolls);
                    checkState(outcome.reason == Poller.StopReason.RESOLVED, "not resolved: %s", outcome);
                    output.attach(stdoutb, stderrb);
                    exitCode = requireNonNull(outcome.content);
                }
                stdoutb.flush();
                stderrb.flush();
                stdout = new String(stdoutb.toByteArray(), UTF_8);
                stderr = new String(stderrb.toByteArray(), UTF_8);
            }
            System.out.format("%s stdout:%n%s%n", executable, stdout);
            System.out.format("%s stderr:%n%s%n", executable, stderr);
            return DockerExecResult.create(exitCode.intValue(), stdout, stderr);
        } catch (DockerException | InterruptedException | IOException e) {
            throw new ContainmentException(e);
        }
    }


}
