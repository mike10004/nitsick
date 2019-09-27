package io.github.mike10004.nitsick;

import java.util.function.Function;

public class FunctionLayer implements SettingLayer {

    private final Function<String, String> keyTransform;
    private final Function<String, String> getter;

    public FunctionLayer(Function<String, String> getter, Function<String, String> keyTransform) {
        this.keyTransform = keyTransform;
        this.getter = getter;
    }

    @Override
    public String apply(String key) {
        String transformedKey = keyTransform.apply(key);
        return getter.apply(transformedKey);
    }

    public static FunctionLayer identityKey(Function<String, String> fn) {
        return new FunctionLayer(fn, Function.identity());
    }
}
