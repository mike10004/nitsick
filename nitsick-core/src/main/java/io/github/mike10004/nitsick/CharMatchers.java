package io.github.mike10004.nitsick;

class CharMatchers {

    private CharMatchers(){}

    private static final CharMatcher DOT = CharMatcher.is('.');

    public static CharMatcher dot() {
        return DOT;
    }
}
