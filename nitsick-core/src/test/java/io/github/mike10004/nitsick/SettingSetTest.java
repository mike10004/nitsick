package io.github.mike10004.nitsick;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class SettingSetTest extends SettingSetTestBase {

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
    public void getTyped_empty() {
        SettingSet s = sample();
        int val = s.getTyped("c.empty", Integer::parseInt, -1);
        assertEquals(-1, val);
    }

    @Test
    public void getOpt() {
        SettingSet s = sample();
        Optional<String> opt = s.getOpt("g");
        assertFalse(opt.isPresent());
        opt = s.getOpt("d.e");
        assertEquals("hello", opt.orElse(null));
        assertEquals(Integer.valueOf(10), s.getOpt("b").map(Integer::parseInt).orElse(null));
    }
}