package io.github.mike10004.nitsick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// TODO shade Guava Lists class
class Lists {
    private Lists() {}

    public static <T> List<T> asList(T first, T second) {
        return asList(first, Collections.singletonList(second));
    }

    public static <T> List<T> asList(T first, T[] others) {
        return asList(first, Arrays.asList(others));
    }

    public static <T> List<T> asList(T first, List<T> others) {
        List<T> l = new ArrayList<>(1 + others.size());
        l.add(first);
        l.addAll(others);
        return l;
    }
}
