package io.github.mike10004.nitsick;

import org.junit.Test;

import static org.junit.Assert.*;

public class TruthinessTest {

    @Test
    public void parseTruthy() {
        for (String expectTrue : new String[]{
                "yes",
                "y",
                "true",
                "True",
                "TRUE",
                "1",
                "YES",
                "Y",
                "trUe",
        }) {
            boolean actual = Truthiness.parseTruthy(expectTrue);
            assertTrue(expectTrue, actual);

        }

        for (String expectFalse : new String[]{
                "0",
                "false",
                "False",
                "UNTRUE",
                "TRU",
                "2",
                "n",
                "ye",
        }) {
            boolean actual = Truthiness.parseTruthy(expectFalse);
            assertFalse(expectFalse, actual);
        }
    }
}