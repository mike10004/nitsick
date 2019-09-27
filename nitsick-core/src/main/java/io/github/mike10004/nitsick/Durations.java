package io.github.mike10004.nitsick;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Durations {

    private static final CharMatcher NUMBERS = CharMatcher.inRange('0', '9');
    private static final CharMatcher LETTERS = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z'));

    public static Duration parseDuration(String definition) {
        return parseDuration(definition, null);
    }

    public static Duration parseDuration(String definition, Duration defaultValue) {
        if (definition != null && !definition.isEmpty()) {
            try {
                return Duration.parse(definition); // support standard duration syntax
            } catch (java.time.format.DateTimeParseException ignore) {
            }
            // then fall back to lax syntax
            definition = CharMatcher.whitespace().removeFrom(definition);
            String numbers = LETTERS.trimTrailingFrom(definition);
            String unitToken = NUMBERS.trimLeadingFrom(definition);
            TimeUnit unit = parseUnit(unitToken, TimeUnit.MILLISECONDS);
            long magnitude = Long.parseLong(numbers);
            long millis = unit.toMillis(magnitude);
            return Duration.ofMillis(millis);
        }
        return defaultValue;
    }

    public static TimeUnit parseUnit(String unitToken, TimeUnit defaultValue) {
        if (unitToken == null || unitToken.isEmpty()) {
            return defaultValue;
        }
        unitToken = unitToken.toLowerCase();
        switch (unitToken) {
            case "ms":
            case "milli":
            case "millis":
            case "milliseconds":
            case "millisecs":
            case "millisec":
                return TimeUnit.MILLISECONDS;
            case "s":
            case "sec":
            case "seconds":
                return TimeUnit.SECONDS;
            case "m":
            case "min":
            case "minutes":
                return TimeUnit.MINUTES;
        }
        throw new IllegalArgumentException("failed to parse unit: " + StringUtils.abbreviate(unitToken, 128));
    }

}
