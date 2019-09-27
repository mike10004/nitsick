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

public class SettingSet {

    private final String domain;
    private final List<SettingLayer> layers;

    SettingSet(String domain, List<SettingLayer> layers) {
        this.domain = domain;
        this.layers = Collections.unmodifiableList(new ArrayList<>(layers));
    }

    // TODO overload with one-arg and two-arg shortcuts for efficiency
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

    public String toKey(List<String> subsections) {
        List<String> allSections = Lists.asList(domain, subsections);
        return allSections.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(CharMatchers.dot()::trimFrom).collect(Collectors.joining("."));
    }

    public <T> T getTyped(Stream<String> identifierAliases, Function<? super String, ? extends T> parser, @Nullable T valueIfUndefined) {
        String token = get(identifierAliases);
        if (token != null && !token.isEmpty()) {
            return parser.apply(token);
        }
        return valueIfUndefined;
    }

    public <T> T getTyped(String identifier, Function<? super String, ? extends T> parser, @Nullable T valueIfUndefined) {
        return getTyped(Stream.of(identifier), parser, valueIfUndefined);
    }

    public boolean get(String identifier, boolean defaultValue) {
        return getTyped(identifier, Truthiness::parseTruthy, defaultValue);
    }

    public int get(String identifier, int defaultValue) {
        return getTyped(identifier, Integer::parseInt, defaultValue);
    }

    public String get(String identifier) {
        return get(Stream.of(identifier));
    }

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

    public Timeouts timeouts() {
        return new Timeouts(this);
    }

    public static SettingSet global(String domain) {
        List<SettingLayer> layers = Lists.asList(SyspropsLayer.getInstance(), EnvironmentLayer.getInstance());
        return new SettingSet(domain, layers);
    }

    public static SettingSet local(String domain, SettingLayer layer, SettingLayer...otherLayers) {
        return new SettingSet(domain, Lists.asList(layer, otherLayers));
    }
}
