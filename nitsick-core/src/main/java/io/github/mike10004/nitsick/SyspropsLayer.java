package io.github.mike10004.nitsick;

class SyspropsLayer extends KeyTransformingLayer {

    private static final SyspropsLayer INSTANCE = new SyspropsLayer();

    private SyspropsLayer() {
        super(System::getProperty, identityKeyTransform());
    }

    public static SettingLayer getInstance() {
        return INSTANCE;
    }
}
