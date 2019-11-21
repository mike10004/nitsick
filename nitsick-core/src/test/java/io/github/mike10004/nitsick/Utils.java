package io.github.mike10004.nitsick;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, String> map(String...keyValuePairs) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            m.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return m;
    }

    public static SettingLayer layer(String...keyValuePairs) {
        return new ForwardingLayer(map(keyValuePairs)::get);
    }
}
