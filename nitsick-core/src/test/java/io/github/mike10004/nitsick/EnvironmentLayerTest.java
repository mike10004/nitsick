package io.github.mike10004.nitsick;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            String input = testCase[0], expected = testCase[1];
            String actual = EnvironmentLayer.tranformToEnvironmentVariable(input);
            if (!expected.equalsIgnoreCase(actual)) {
                failures.add(Arrays.asList(input, expected, actual));
                System.err.format("failed test case: input=\"%s\", expected=\"%s\", actual=\"%s\"%n", input, expected, actual);
            }
        }
        assertEquals("list of failures", Collections.emptyList(), failures);
    }
}