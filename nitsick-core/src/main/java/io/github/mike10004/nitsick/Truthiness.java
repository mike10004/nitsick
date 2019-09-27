package io.github.mike10004.nitsick;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Truthiness {

    private static Set<String> NORMALIZED_TRUTHS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("true", "yes", "y", "1")));

    private Truthiness() {}

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase();
    }

    public static boolean parseTruthy(String value) {
        if (value == null) {
            return false;
        }
        value = normalize(value);
        return NORMALIZED_TRUTHS.contains(value);
    }
}
