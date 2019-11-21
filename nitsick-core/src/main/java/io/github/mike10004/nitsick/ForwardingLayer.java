package io.github.mike10004.nitsick;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Layer that forwards value requests to a getter function.
 */
public class ForwardingLayer implements SettingLayer {

    private final Function<String, String> getter;

    /**
     * Constructs an instance.
     * @param getter getter function that handles value requests
     */
    public ForwardingLayer(Function<String, String> getter) {
        this.getter = requireNonNull(getter);
    }

    @Override
    public String apply(String key) {
        return getter.apply(key);
    }

}
