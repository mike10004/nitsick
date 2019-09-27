package io.github.mike10004.nitsick;

import java.time.Duration;
import java.util.stream.Stream;

public interface TimeoutSetting {
    Stream<String> aliases();
    Duration defaultValue();
}
