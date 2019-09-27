package io.github.mike10004.nitsick;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class DurationsTest {

    @Test
    public void parseDuration() {
        TestCase[] testCases = {
                TestCase.millis("123ms", 123),
                TestCase.millis("123 ms", 123),
                TestCase.millis("123 milliseconds", 123),
                new TestCase("1min", Duration.ofMinutes(1)),
                new TestCase("1 min", Duration.ofMinutes(1)),
                new TestCase("1m", Duration.ofMinutes(1)),
                TestCase.seconds("456sec", 456),
                TestCase.seconds("456s", 456),
                TestCase.seconds("456seconds", 456),
                TestCase.seconds("456 sec", 456),
                TestCase.seconds("456 s", 456),
                TestCase.seconds("456 seconds", 456),
                TestCase.formal(Duration.ofMillis(789)),
                TestCase.formal(Duration.ofSeconds(30)),
                TestCase.formal(Duration.ofSeconds(300)),
                TestCase.formal(Duration.ofHours(36)),
                TestCase.formal(Duration.ofHours(12)),
                TestCase.formal(Duration.ofDays(3)),
                TestCase.formal(Duration.ofDays(30)),
                TestCase.formal(Duration.ofDays(365)),
                TestCase.formal(Duration.ofDays(400)),
        };
        for (TestCase testCase : testCases) {
            assertEquals(testCase.toString(), testCase.expected, Durations.parseDuration(testCase.input));
        }
    }

    private static class TestCase {
        public final String input;
        public final Duration expected;

        public TestCase(String input, Duration expected) {
            this.input = input;
            this.expected = expected;
        }

        public static TestCase millis(String input, long millis) {
            return new TestCase(input, Duration.ofMillis(millis));
        }

        public static TestCase seconds(String input, long sec) {
            return new TestCase(input, Duration.ofSeconds(sec));
        }

        public static TestCase formal(Duration duration) {
            return new TestCase(duration.toString(), duration);
        }

        public String toString() {
            return String.format("TestCase{input=\"%s\",expected=%s}", StringEscapeUtils.escapeJava(input), expected);
        }
    }
}