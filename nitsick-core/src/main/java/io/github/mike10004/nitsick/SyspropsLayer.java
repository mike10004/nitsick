package io.github.mike10004.nitsick;

class SyspropsLayer extends ForwardingLayer {

    private static final SyspropsLayer INSTANCE = new SyspropsLayer();

    private SyspropsLayer() {
        super(System::getProperty);
    }

    public static SettingLayer getInstance() {
        return INSTANCE;
    }
}
