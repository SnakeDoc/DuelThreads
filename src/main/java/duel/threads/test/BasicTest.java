package duel.threads.test;

import java.time.Duration;
import java.time.Instant;

public abstract class BasicTest implements Test {

    private Instant start;

    protected void setStartTime() {
        this.start = Instant.now();
    }

    protected long getRuntimeMillis() {
        return Duration.between(start, Instant.now()).toMillis();
    }

    protected void printRuntimeDuration() {
        System.out.println("Runtime: " + getRuntimeMillis() + " (ms)");
    }

    protected void printRuntimeDuration(String tag) { System.out.println(tag + " - Runtime: " + getRuntimeMillis() + " (ms)"); }

}
