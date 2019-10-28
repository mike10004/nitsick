package io.github.mike10004.nitsick.containment;

import com.google.common.primitives.Ints;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.ExecState;
import io.github.mike10004.subprocess.ProcessResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkState;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Executor that SHOULD be totally fine but instead tends to fail with an
 * IOException "Connection reset by peer" on some long-running commands.
 * (Long-running means merely longer than like a few milliseconds.)
 */
class AttachedClientExecutor implements DockerExecutor {

    private final DockerClient docker;
    private final String containerId;
    private final Charset execOutputCharset;

    public AttachedClientExecutor(DockerClient docker, String containerId, Charset execOutputCharset) {
        this.docker = docker;
        this.containerId = containerId;
        this.execOutputCharset = execOutputCharset;
    }

    @Override
    public DockerExecResult<String> execute(String executable, String... args) throws ContainmentException {
        try {
            String[] cmd = ArrayUtil.prepend(args, executable);
            System.out.format("command: %s%n", Arrays.toString(cmd));
            ExecCreation execCreation = docker.execCreate(containerId, cmd, DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr());
            String execId = execCreation.id();
            String stdout, stderr;
            try (ByteArrayOutputStream stdoutb = new ByteArrayOutputStream(256);
                 ByteArrayOutputStream stderrb = new ByteArrayOutputStream(256)) {
                try (LogStream output = docker.execStart(execId)) {  // no ExecStartParam.DETACH, so presumably we block until execution finishes
                    output.attach(stdoutb, stderrb); // pumps until done
                }
                stdoutb.flush();
                stderrb.flush();
                stdout = new String(stdoutb.toByteArray(), execOutputCharset);
                stderr = new String(stderrb.toByteArray(), execOutputCharset);
            }
            ExecState state = docker.execInspect(execId);
            Boolean running = state.running();
            if (running == null || running.booleanValue()) {
                throw new IllegalStateException("should not be running: " + running);
            }
            Long exitCodeObject = state.exitCode();
            checkState(exitCodeObject != null, "exit code null");
            int exitCode = Ints.checkedCast(exitCodeObject.longValue());
            Thread.sleep(1000);
            return DockerExecResult.create(exitCode, stdout, stderr);
        } catch (DockerException | IOException | InterruptedException e) {
            throw new ContainmentException(e);
        }
    }

}
