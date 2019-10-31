package io.github.mike10004.nitsick;

import java.math.BigInteger;

public class SettingSetTestBase {

    static final BigInteger EXAMPLE_BIGINT = new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

    protected static SettingSet sample() {
        return LayeredSettingSet.of("a", sampleLayer());
    }

    protected static SettingLayer sampleLayer() {
        String[] pairs = {
                "a.b", "10",
                "a.c", "20",
                "a.bool", "true",
                "a.ya", "yes",
                "a.empty", "",
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
