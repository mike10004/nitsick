package io.github.mike10004.nitsick;

import java.time.Duration;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Class that represents a settings subset where keys are prefixed
 * by the domain and the infix {@code timeout}. An identifier, in
 * this context, is the portion of the key following {@code domain.timeout}.
 */
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

    /**
     * Gets a duration for the given identifier.
     * @param identifier the identifier
     * @param defaultMs value to return if undefined, in milliseconds
     * @return a duration instance
     */
    public Duration get(String identifier, long defaultMs) {
        return get(identifier, Duration.ofMillis(defaultMs));
    }

    /**
     * Gets a duration for the given identifier.
     * @param identifier the identifier
     * @param defaultValue value to return if undefined
     * @return a duration instance
     */
    public Duration get(String identifier, Duration defaultValue) {
        return get(Stream.of(identifier), defaultValue);
    }

    /**
     * Gets a duration for the given identifier.
     * @param identifierAliases one or more identifiers under which the setting is stored
     * @param defaultValue value to return if undefined
     * @return a duration instance
     */
    public Duration get(Stream<String> identifierAliases, Duration defaultValue) {
        identifierAliases = identifierAliases.map(a -> infix + a);
        return settings.getTyped(identifierAliases, Durations::parseDuration, defaultValue);
    }

    /**
     * Gets a duration for the setting specified by the given key.
     * @param length value that maps to the desired identifier
     * @return a duration instance
     */
    public Duration get(TimeoutSetting length) {
        return get(length.aliases(), length.defaultValue());
    }

    /**
     * Gets the duration that is the value of the setting mapped by {@link StandardTimeout#SHORT}.
     * @return the duration
     */
    public Duration getShort() {
        return get(StandardTimeout.SHORT);
    }

    /**
     * Gets the duration that is the value of the setting mapped by {@link StandardTimeout#MEDIUM}.
     * @return the duration
     */
    public Duration getMedium() {
        return get(StandardTimeout.MEDIUM);
    }

    /**
     * Gets the duration that is the value of the setting mapped by {@link StandardTimeout#LONG}.
     * @return the duration
     */
    public Duration getLong() {
        return get(StandardTimeout.LONG);
    }

    /**
     * Gets the duration that is the value of the setting keyed by {@code domain.timeout}
     * with no suffix.
     * @return the duration
     */
    public Duration getAnonymous(Duration valueIfUndefined) {
        return get("", valueIfUndefined);
    }

}
