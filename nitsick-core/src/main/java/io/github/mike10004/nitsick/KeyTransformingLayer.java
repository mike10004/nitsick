package io.github.mike10004.nitsick;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class KeyTransformingLayer implements SettingLayer {

    private final Function<String, Stream<String>> keyTransform;
    private final Function<String, String> getter;

    public KeyTransformingLayer(Function<String, String> getter, Function<String, Stream<String>> keyTransform) {
        this.keyTransform = requireNonNull(keyTransform);
        this.getter = requireNonNull(getter);
    }

    @Override
    public String apply(String key) {
        Stream<String> transformedKeys = keyTransform.apply(key);
        return transformedKeys.map(getter)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static Function<String, Stream<String>> identityKeyTransform() {
        return Stream::of;
    }

    public static KeyTransformingLayer withIdentityKey(Function<String, String> settingGetter) {
        return new KeyTransformingLayer(settingGetter, identityKeyTransform());
    }
}
