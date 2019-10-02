package io.github.mike10004.nitsick;

import java.util.function.Function;
import java.util.stream.Stream;

class EnvironmentLayer extends KeyTransformingLayer {

    private static final EnvironmentLayer INSTANCE = new EnvironmentLayer(System::getenv);

    public EnvironmentLayer(Function<String, String> getenv) {
        super(getenv, EnvironmentLayer::transformToEnvironmentVariables);
    }

    public static Stream<String> transformToEnvironmentVariables(String systemPropertyName) {
        systemPropertyName = CharMatchers.dot().trimFrom(systemPropertyName);
        String primary = CharMatchers.dot().replaceFrom(systemPropertyName, '_').toUpperCase();
        primary = CharMatchers.usEnglishAlphanumericOrUnderscore().negate().removeFrom(primary);
        return Stream.of(primary);
    }

    public static SettingLayer getInstance() {
        return INSTANCE;
    }
}
