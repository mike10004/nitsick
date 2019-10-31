package io.github.mike10004.nitsick;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Interface of a service that provides access to settings.
 * Some terminology:
 * <ul>
 *     <li>setting - a key-value pair</li>
 *     <li>key - setting key</li>
 *     <li>value - setting value</li>
 *     <li>domain - prefix that all keys have</li>
 *     <li>identifier - portion of a key following the domain and one delimiter</li>
 * </ul>
 */
public interface SettingSet {

    /**
     * Parses a value from this setting set.
     * @param identifierAliases one or more identifiers
     * @param parser value parser
     * @param valueIfUndefined value to return if setting is not defined
     * @param <T> type of the parsed value
     * @return the parsed value
     */
    default <T> T getTyped(Stream<String> identifierAliases, Function<? super String, ? extends T> parser, @Nullable T valueIfUndefined) {
        String token = get(identifierAliases);
        if (token != null && !token.isEmpty()) {
            return parser.apply(token);
        }
        return valueIfUndefined;
    }

    /**
     * Parses a value from this setting set.
     * @param identifier the identifier
     * @param parser the parser
     * @param valueIfUndefined value to return if setting is not defined
     * @param <T> type of the parsed value
     * @return the parsed value
     */
    default <T> T getTyped(String identifier, Function<? super String, ? extends T> parser, @Nullable T valueIfUndefined) {
        return getTyped(Stream.of(identifier), parser, valueIfUndefined);
    }

    /**
     * Gets the value of a setting as a boolean.
     * @param identifier identifier
     * @param defaultValue value to return if setting is not defined
     * @return boolean value of the setting
     * @see Truthiness#parseTruthy(String)
     */
    default boolean get(String identifier, boolean defaultValue) {
        return getTyped(identifier, Truthiness::parseTruthy, defaultValue);
    }

    /**
     * Gets the value of a setting as an integer.
     * @param identifier the identifier
     * @param defaultValue value to return if setting is not defined
     * @return integer value of the setting
     */
    default int get(String identifier, int defaultValue) {
        return getTyped(identifier, Integer::parseInt, defaultValue);
    }

    /**
     * Gets the value of a setting.
     * @param identifier the identifier
     * @return the value of the setting, or null if not defined
     */
    default String get(String identifier) {
        return get(Stream.of(identifier));
    }

    /**
     * Gets the value of a setting.
     * @param identifierAliases one or more identifiers under which the setting is defined
     * @return the value of the setting
     */
    String get(Stream<String> identifierAliases);

    /**
     * Gets a timeouts provider for this setting set.
     * @return a new timeouts instance
     */
    default Timeouts timeouts() {
        return new Timeouts(this);
    }

    /**
     * Creates a domain-scoped layered setting set that represents the system properties
     * composed on top of the system environment variables.
     * For domain {@code foo}, fetching the value of identifier {@code bar.baz} returns the value corresponding
     * to system property {@code foo.bar.baz}, or if that is undefined, the value of environment variable
     * {@code FOO_BAR_BAZ}.
     * @param domain the settings domain
     * @return a new setting set instance
     */
    @SuppressWarnings("unused") // unused in this project because only local instance are used for testing
    static SettingSet system(String domain) {
        List<SettingLayer> layers = Lists.asList(SyspropsLayer.getInstance(), EnvironmentLayer.getInstance());
        return new LayeredSettingSet(domain, layers);
    }

    /**
     * Returns a setting set representing the system settings.
     * Deprecated alias of {@link #system(String)}.
     * @see #system(String)
     * @deprecated use the more-aptly named {@link #system(String)}
     */
    @Deprecated
    static SettingSet global(String domain) {
        return system(domain);
    }
}

