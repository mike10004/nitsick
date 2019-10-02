package io.github.mike10004.nitsick;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Class that provides access to multiple layers of settings.
 * Some terminology:
 * <ul>
 *     <li>setting - a key-value pair</li>
 *     <li>key - setting key</li>
 *     <li>value - setting value</li>
 *     <li>domain - prefix that all keys have</li>
 *     <li>identifier - portion of a key following the domain and delimiter</li>
 * </ul>
 */
public class SettingSet {

    private final String domain;
    private final List<SettingLayer> layers;

    SettingSet(String domain, List<SettingLayer> layers) {
        this.domain = domain;
        this.layers = Collections.unmodifiableList(new ArrayList<>(layers));
    }

    // TODO overload with one-arg and two-arg shortcuts for efficiency

    /**
     * Transforms a path of components of a key into key in this setting domain.
     * @param subSection the first component
     * @param moreSubsections other components
     * @return the key
     */
    public String toKey(String subSection, String...moreSubsections) {
        requireNonNull(moreSubsections);
        return toKey(Lists.asList(subSection, moreSubsections));
    }

    // TODO shade Guava Lists class
    private static class Lists {
        private Lists() {}

        public static <T> List<T> asList(T first, T second) {
            return asList(first, Collections.singletonList(second));
        }

        public static <T> List<T> asList(T first, T[] others) {
            return asList(first, Arrays.asList(others));
        }

        public static <T> List<T> asList(T first, List<T> others) {
            List<T> l = new ArrayList<>(1 + others.size());
            l.add(first);
            l.addAll(others);
            return l;
        }
    }

    /**
     * Transforms a path of components of a key into key in this setting domain.
     * @param subsections components of the key, excluding the domain
     * @return the key
     */
    public String toKey(List<String> subsections) {
        List<String> allSections = Lists.asList(domain, subsections);
        return allSections.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(CharMatchers.dot()::trimFrom).collect(Collectors.joining("."));
    }

    /**
     * Parses a value from this setting set.
     * @param identifierAliases one or more identifiers
     * @param parser value parser
     * @param valueIfUndefined value to return if setting is not defined
     * @param <T> type of the parsed value
     * @return the parsed value
     */
    public <T> T getTyped(Stream<String> identifierAliases, Function<? super String, ? extends T> parser, @Nullable T valueIfUndefined) {
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
    public <T> T getTyped(String identifier, Function<? super String, ? extends T> parser, @Nullable T valueIfUndefined) {
        return getTyped(Stream.of(identifier), parser, valueIfUndefined);
    }

    /**
     * Gets the value of a setting as a boolean.
     * @param identifier identifier
     * @param defaultValue value to return if setting is not defined
     * @return boolean value of the setting
     * @see Truthiness#parseTruthy(String)
     */
    public boolean get(String identifier, boolean defaultValue) {
        return getTyped(identifier, Truthiness::parseTruthy, defaultValue);
    }

    /**
     * Gets the value of a setting as an integer.
     * @param identifier the identifier
     * @param defaultValue value to return if setting is not defined
     * @return integer value of the setting
     */
    public int get(String identifier, int defaultValue) {
        return getTyped(identifier, Integer::parseInt, defaultValue);
    }

    /**
     * Gets the value of a setting.
     * @param identifier the identifier
     * @return the value of the setting, or null if not defined
     */
    public String get(String identifier) {
        return get(Stream.of(identifier));
    }

    /**
     * Gets the value of a setting.
     * @param identifierAliases one or more identifiers under which the setting is defined
     * @return the value of the setting
     */
    public String get(Stream<String> identifierAliases) {
        return get(identifierAliases, layers);
    }

    String get(Stream<String> identifierAliases, Iterable<SettingLayer> layers) {
        List<String> identifierKeyList = identifierAliases
                .map(this::toKey)
                .collect(Collectors.toList());
        for (SettingLayer layer : layers) {
            String value = identifierKeyList.stream()
                    .map(layer).filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Gets a timeouts provider for this setting set.
     * @return a new timeouts instance
     */
    public Timeouts timeouts() {
        return new Timeouts(this);
    }

    /**
     * Creates a domain-scoped setting set that represents the system properties composed on top of the process environment.
     * For domain {@code foo}, fetching the value of identifier {@code bar.baz} returns the value corresponding
     * to system property {@code foo.bar.baz}, or if that is undefined, the value of environment variable
     * {@code FOO_BAR_BAZ}.
     * @param domain the settings domain
     * @return a new setting set instance
     */
    @SuppressWarnings("unused") // unused in this project because only local instance are used for testing
    public static SettingSet global(String domain) {
        List<SettingLayer> layers = Lists.asList(SyspropsLayer.getInstance(), EnvironmentLayer.getInstance());
        return new SettingSet(domain, layers);
    }

    /**
     * Creates a domain-scoped setting set that composes the given layers of settings.
     * @param domain the settings domain
     * @param layer the top layer
     * @param otherLayers other layers, from highest to lowest precedence
     * @return a new setting set instance
     */
    public static SettingSet local(String domain, SettingLayer layer, SettingLayer...otherLayers) {
        return new SettingSet(domain, Lists.asList(layer, otherLayers));
    }
}
