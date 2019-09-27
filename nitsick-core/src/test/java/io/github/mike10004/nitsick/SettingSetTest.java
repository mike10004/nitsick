package io.github.mike10004.nitsick;

import org.junit.Test;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SettingSetTest {

    @Test
    public void toKey() {
        String actual = SettingSet.local("a", SyspropsLayer.getInstance()).toKey("b", "c");
        assertEquals("a.b.c", actual);
    }

    private static final BigInteger EXAMPLE_BIGINT = new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

    @Test
    public void getTyped() {
        BigInteger present = sample().getTyped("e.bigint", BigInteger::new, BigInteger.ZERO);
        assertEquals(EXAMPLE_BIGINT, present);
        BigInteger absent = sample().getTyped("e.absent", BigInteger::new, BigInteger.ZERO);
        assertEquals(BigInteger.ZERO, absent);
    }

    @Test
    public void get() {
        SettingSet s = sample();
        assertEquals("hello", s.get("d.e"));
        assertEquals("10", s.get("b"));
        assertEquals(null, s.get("x"));
    }

    @Test
    public void get_boolean() {
        SettingSet s = sample();
        assertTrue(s.get("bool", false));
        assertTrue(s.get("ya", false));
        assertTrue(s.get("r2d2", false));
        assertFalse(s.get("c3p0", true));
        assertFalse(s.get("gobbledygood", false));
    }

    @Test
    public void get_int() {
        SettingSet s = sample();
        assertEquals(10, s.get("b", 0));
        assertEquals(10, s.get("b", 20));
    }

    @Test(expected = NumberFormatException.class)
    public void get_int_malformed() {
        SettingSet s = sample();
        assertEquals(10, s.get("d.x", 10));
    }

    @Test
    public void get_aliases() {
        SettingSet s = sample();
        assertEquals("hello", s.get(Stream.of("d.d", "d.e", "d.f")));
        assertNull(s.get(Stream.of("j", "k", "l")));
    }

    @Test
    public void get_fallbackToEnvironment() {
        SettingLayer fakeEnv = new FunctionLayer(Utils.map("A_D_G", "shibboleth")::get, EnvironmentLayer::tranformToEnvironmentVariable);
        SettingSet s = SettingSet.local("a", sampleLayer(), fakeEnv);
        String actual = s.get("d.g");
        assertEquals("shibboleth", actual);
    }

    private static SettingSet sample() {
        return SettingSet.local("a", sampleLayer());
    }

    private static SettingLayer sampleLayer() {
        String[] pairs = {
                "a.b", "10",
                "a.c", "20",
                "a.bool", "true",
                "a.ya", "yes",
                "a.r2d2", "1",
                "a.c3p0", "0",
                "a.d.e", "hello",
                "a.d.f", "world",
                "a.d.x", "z",
                "a.e.bigint", EXAMPLE_BIGINT.toString(),
        };
        return Utils.layer(pairs);
    }
}