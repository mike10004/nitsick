package io.github.mike10004.nitsick.containment;

import com.google.common.io.ByteSource;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class DockerSubprocessExecutorTest {

    @Test
    public void execute_echo() throws Exception {
        ContainerParametry parametry = ContainerParametry.builder("busybox")
                .command(Arrays.asList("tail", "-f", "/dev/null"))
                .build();
        DockerExecResult<String> result;
        try (ContainerRunner runner = new DjContainerRunner()) {
            try (RunningContainer container = runner.run(parametry)) {
                DockerExecutor executor = new DockerSubprocessExecutor(container.id(), new HashMap<>(), UTF_8);
                result = executor.execute("echo", "hello, world");
            }
        }
        System.out.println(result);
        assertEquals("result content", "hello, world", result.stdout().trim());
        assertEquals("process exit code", 0, result.exitCode());
    }


}