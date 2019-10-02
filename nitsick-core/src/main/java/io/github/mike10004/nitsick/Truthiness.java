package io.github.mike10004.nitsick;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Static methods that relate to lenient parsing of boolean values.
 * Truthy values are those commonly used to specify a boolean value
 * of {@code true} in a configuration file, system property value,
 * or environment variable value.
 *
 * The set of values considered truthy is: {@code {true, 1, yes, y}}.
 */
public class Truthiness {

    private static Set<String> NORMALIZED_TRUTHS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("true", "yes", "y", "1")));

    private Truthiness() {}

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase();
    }

    /**
     * Determines whether a string represents a value of {@code true}.
     * @param value the string
     * @return true iff the normalized string is a truthy value
     */
    public static boolean parseTruthy(@Nullable String value) {
        if (value == null) {
            return false;
        }
        value = normalize(value);
        return NORMALIZED_TRUTHS.contains(value);
    }
}
