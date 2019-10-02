package io.github.mike10004.nitsick;

import java.util.function.Function;

/**
 * Interface of a service that tranforms keys into values.
 */
public interface SettingLayer extends Function<String, String> {

}
