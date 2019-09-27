package io.github.mike10004.nitsick;

import java.time.Duration;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Timeouts {

    private final SettingSet settings;
    private final String infix;

    Timeouts(SettingSet settings) {
        this(settings, "timeout.");
    }

    Timeouts(SettingSet settings, String infix) {
        this.settings = requireNonNull(settings);
        this.infix = requireNonNull(infix);
    }

    public Duration get(String identifier, long defaultMs) {
        return get(identifier, Duration.ofMillis(defaultMs));
    }

    public Duration get(String identifier, Duration defaultValue) {
        return get(Stream.of(identifier), defaultValue);
    }

    public Duration get(Stream<String> identifierAliases, Duration defaultValue) {
        identifierAliases = identifierAliases.map(a -> infix + a);
        return settings.getTyped(identifierAliases, Durations::parseDuration, defaultValue);
    }

    public Duration get(TimeoutSetting length) {
        return get(length.aliases(), length.defaultValue());
    }

    public Duration getShort() {
        return get(StandardTimeout.SHORT);
    }

    public Duration getMedium() {
        return get(StandardTimeout.MEDIUM);
    }

    public Duration getLong() {
        return get(StandardTimeout.LONG);
    }

}
