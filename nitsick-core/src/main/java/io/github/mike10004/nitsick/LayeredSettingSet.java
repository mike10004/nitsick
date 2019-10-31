package io.github.mike10004.nitsick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class LayeredSettingSet implements SettingSet {

    private final String domain;
    private final List<SettingLayer> layers;

    public LayeredSettingSet(String domain, List<SettingLayer> layers) {
        this.domain = requireNonNull(domain, "domain");
        this.layers = Collections.unmodifiableList(new ArrayList<>(layers));
    }

    /**
     * Creates a domain-scoped setting set that composes the given layers of settings.
     * @param domain the settings domain
     * @param layer the top layer
     * @param otherLayers other layers, from highest to lowest precedence
     * @return a new setting set instance
     */
    public static LayeredSettingSet of(String domain, SettingLayer layer, SettingLayer... otherLayers) {
        return new LayeredSettingSet(domain, Lists.asList(layer, otherLayers));
    }

    /**
     * Transforms a path of components of a key into key in this setting domain.
     * @param subSection the first component
     * @param subSubSection the second component
     * @return the key
     */
    protected String toKey(String subSection, String subSubSection) {
        requireNonNull(subSection);
        requireNonNull(subSubSection);
        return toKey(Lists.asList(subSection, subSubSection));
    }

    /**
     * Transforms a subkey path of components of a key into key in this setting domain.
     * @param subSection a component
     * @return the key
     */
    protected String toKey(String subSection) {
        requireNonNull(subSection, "subSection");
        return toKey(Collections.singletonList(subSection));
    }

    protected String toKey(List<String> subsections) {
        List<String> allSections = Lists.asList(domain, subsections);
        return allSections.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(CharMatchers.dot()::trimFrom).collect(Collectors.joining("."));
    }

    @Override
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
     * Transforms a path of components of a key into key in this setting domain.
     * @param subSection the first component
     * @param moreSubsections other components
     * @return the key
     */
    protected String toKey(String subSection, String...moreSubsections) {
        requireNonNull(subSection);
        requireNonNull(moreSubsections);
        return toKey(Lists.asList(subSection, moreSubsections));
    }


}
