package io.github.mike10004.nitsick;

import java.time.Duration;
import java.util.stream.Stream;

/**
 * Interface of a value class that defines a timeout setting.
 */
public interface TimeoutSetting {

    /**
     * Returns a new stream of aliases of the suffix of key that defines this setting.
     * @return a stream of aliases
     */
    Stream<String> aliases();

    /**
     * Gets the default value of the setting.
     * @return default value
     */
    Duration defaultValue();
}
