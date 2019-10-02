package io.github.mike10004.nitsick;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class EnvironmentLayerTest {

    @Test
    public void tranformToEnvironmentVariable() {
        String[][] testCases = {
                {"foo", "FOO"},
                {"foo.", "FOO"},
                {"foo.bar", "FOO_BAR"},
                {"foo-bar.baz", "FOOBAR_BAZ"},
                {"foo_bar.baz", "FOO_BAR_BAZ"},
        };
        List<List<String>> failures = new ArrayList<>();
        for (String[] testCase : testCases) {
            String input = testCase[0];
            List<String> expected = Arrays.asList(testCase).subList(1, testCase.length);
            List<String> actual = EnvironmentLayer.transformToEnvironmentVariables(input).collect(Collectors.toList());
            if (!expected.equals(actual)) {
                List<String> failure = new ArrayList<>(1 + expected.size() + actual.size());
                failure.add(input);
                failure.addAll(expected);
                failure.addAll(actual);
                failures.add(failure);
                System.err.format("failed test case: input=\"%s\", expected=%s, actual=%s%n", input, describe(expected), describe(actual));
            }
        }
        assertEquals("list of failures", Collections.emptyList(), failures);
    }

    private static String describe(List<String> things) {
        return things.stream()
                .map(s -> "\"" + StringEscapeUtils.escapeJava(s) + "\"")
                .collect(Collectors.joining(", ", "[", "]"));
    }
}