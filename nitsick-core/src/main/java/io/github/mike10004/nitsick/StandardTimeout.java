package io.github.mike10004.nitsick;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StandardTimeout implements TimeoutSetting {

    SHORT(500), MEDIUM(5 * 1000), LONG(30 * 1000);

    private final Duration defaultValue;
    private final List<String> aliases;

    StandardTimeout(long defaultMs, String...aliases) {
        this.defaultValue = Duration.ofMillis(defaultMs);
        this.aliases = Stream.concat(Stream.of(name().toLowerCase()), Arrays.stream(aliases)).collect(Collectors.toList());
    }

    @Override
    public Duration defaultValue() {
        return defaultValue;
    }

    @Override
    public Stream<String> aliases() {
        return aliases.stream();
    }

}
