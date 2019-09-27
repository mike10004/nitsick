package io.github.mike10004.nitsick;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

public class TimeoutsTest {

    private SettingSet s = SettingSet.local("a", Utils.layer(
            "a.timeout.short", "200ms",
            "a.timeout.medium", "12sec",
            "a.timeout.custom", "4292millis",
            "a.timeout.long", "10min"));

    @Test
    public void getMedium() {
        assertEquals(Duration.ofSeconds(12), s.timeouts().getMedium());
    }

    @Test
    public void get_absent() {
        assertNull(s.timeouts().get("whoknows", null));
        assertEquals(Duration.ofMillis(123), s.timeouts().get("whoknows", Duration.ofMillis(123)));
    }

    @Test
    public void getCustom() {
        assertEquals(Duration.ofMillis(4292), s.timeouts().get("custom", null));
    }
}