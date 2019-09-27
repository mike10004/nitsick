package io.github.mike10004.nitsick;

class EnvironmentLayer extends FunctionLayer {

    private static final EnvironmentLayer INSTANCE = new EnvironmentLayer();

    EnvironmentLayer() {
        super(System::getenv, EnvironmentLayer::tranformToEnvironmentVariable);
    }

    public static String tranformToEnvironmentVariable(String systemPropertyName) {
        return CharMatchers.dot().replaceFrom(systemPropertyName, '_').toUpperCase();
    }

    public static SettingLayer getInstance() {
        return INSTANCE;
    }
}
