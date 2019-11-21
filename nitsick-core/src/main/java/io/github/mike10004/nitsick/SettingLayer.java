package io.github.mike10004.nitsick;

import java.util.function.Function;

/**
 * Interface of a service that tranforms keys into values.
 */
public interface SettingLayer extends Function<String, String> {

    /**
     * Returns a setting layer that represents the JVM system properties.
     * @return a layer that represents the system properties
     * @see System#getProperty(String)
     */
    static SettingLayer systemPropertiesLayer() {
        return SyspropsLayer.getInstance();
    }

    /**
     * Returns a setting layer that represents the environment of the process.
     * This layer provides access to the values of environment variables.
     * @return a layer that represents the environment
     * @see System#getenv(String)
     */
    static SettingLayer environmentLayer() {
        return EnvironmentLayer.getInstance();
    }
    
}
