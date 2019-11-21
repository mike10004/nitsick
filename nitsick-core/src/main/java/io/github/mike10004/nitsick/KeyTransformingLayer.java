package io.github.mike10004.nitsick;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Setting layer that transforms keys before applying an underlying function.
 */
public class KeyTransformingLayer extends ForwardingLayer {

    private final Function<String, Stream<String>> keyTransform;

    /**
     *
     * @param getter the underlying getter function
     * @param keyTransform the key transform
     */
    public KeyTransformingLayer(Function<String, String> getter, Function<String, Stream<String>> keyTransform) {
        super(getter);
        this.keyTransform = requireNonNull(keyTransform);
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
        return transformedKeys.map(super::apply)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

}
