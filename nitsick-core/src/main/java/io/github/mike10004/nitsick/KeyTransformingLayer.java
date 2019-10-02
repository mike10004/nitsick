package io.github.mike10004.nitsick;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Setting layer that transforms keys before applying an underlying function.
 */
public class KeyTransformingLayer implements SettingLayer {

    private final Function<String, Stream<String>> keyTransform;
    private final Function<String, String> getter;

    /**
     *
     * @param getter the underlying getter function
     * @param keyTransform the key transform
     */
    public KeyTransformingLayer(Function<String, String> getter, Function<String, Stream<String>> keyTransform) {
        this.keyTransform = requireNonNull(keyTransform);
        this.getter = requireNonNull(getter);
    }

    /**
     * Transforms the input key into zero or more output keys and attempts to apply the
     * underlying function to each.
     * @param key the un-transformed key
     * @return the first non-null output of the underlying function
     */
    @Override
    public String apply(String key) {
        Stream<String> transformedKeys = keyTransform.apply(key);
        return transformedKeys.map(getter)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a key transform that makes no change.
     * @return a key transform
     */
    public static Function<String, Stream<String>> identityKeyTransform() {
        return Stream::of;
    }

    /**
     * Creates an instance with the identity key transform and the given underlying function.
     * @param settingGetter the underlying function
     * @return a new instance
     */
    public static KeyTransformingLayer withIdentityKey(Function<String, String> settingGetter) {
        return new KeyTransformingLayer(settingGetter, identityKeyTransform());
    }
}
