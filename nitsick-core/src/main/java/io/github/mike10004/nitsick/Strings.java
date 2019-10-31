package io.github.mike10004.nitsick;

class Strings {

    private Strings() {}

    public static String emptyToNull(String value) {
        if (value != null && value.isEmpty()) {
            value = null;
        }
        return value;
    }
}
