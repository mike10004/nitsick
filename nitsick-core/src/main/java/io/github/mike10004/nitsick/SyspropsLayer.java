package io.github.mike10004.nitsick;

import java.util.function.Function;

class SyspropsLayer extends FunctionLayer {

    private static final SyspropsLayer INSTANCE = new SyspropsLayer();

    private SyspropsLayer() {
        super(System::getProperty, Function.identity());
    }

    public static SettingLayer getInstance() {
        return INSTANCE;
    }
}
