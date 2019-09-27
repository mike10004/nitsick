package io.github.mike10004.nitsick.junit;

import io.github.mike10004.nitsick.SettingSet;
import io.github.mike10004.nitsick.Timeouts;
import org.junit.rules.Timeout;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class TimeoutRules {

    private final Timeouts timeouts;

    public TimeoutRules(Timeouts timeouts) {
        this.timeouts = requireNonNull(timeouts);
    }

    public Timeout rule(Duration duration) {
        return new Timeout(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public Timeout getShortRule() {
        return rule(timeouts.getShort());
    }

    public Timeout getMediumRule() {
        return rule(timeouts.getMedium());
    }

    public Timeout getLongRule() {
        return rule(timeouts.getLong());
    }

    public static TimeoutRules from(SettingSet settings) {
        return new TimeoutRules(settings.timeouts());
    }
}
