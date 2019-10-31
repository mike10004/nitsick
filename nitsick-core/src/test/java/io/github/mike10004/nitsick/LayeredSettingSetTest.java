package io.github.mike10004.nitsick;

import org.junit.Test;

import static org.junit.Assert.*;

public class LayeredSettingSetTest extends SettingSetTestBase {

    @Test
    public void toKey() {
        String actual = LayeredSettingSet.of("a", SyspropsLayer.getInstance()).toKey("b", "c");
        assertEquals("a.b.c", actual);
    }

    @Test
    public void get_fallbackToEnvironment() {
        SettingLayer fakeEnv = new KeyTransformingLayer(Utils.map("A_D_G", "shibboleth")::get, EnvironmentLayer::transformToEnvironmentVariables);
        SettingSet s = LayeredSettingSet.of("a", sampleLayer(), fakeEnv);
        String actual = s.get("d.g");
        assertEquals("shibboleth", actual);
    }

}