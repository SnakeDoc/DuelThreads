package duel.threads.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

public abstract class ChallengeThreeTest extends BasicTest {

    protected final ThreadFactory threadFactory;
    protected final ExecutorService executorService;
    protected final int numberOfThreads;

    protected ChallengeThreeTest(final ExecutorService executorService, int numberOfThreads) {
        this(null, executorService, numberOfThreads);
    }

    protected ChallengeThreeTest(final ThreadFactory threadFactory, int numberOfThreads) {
        this(threadFactory, null, numberOfThreads);
    }

    protected ChallengeThreeTest(final ThreadFactory threadFactory, final ExecutorService executorService, int numberOfThreads) {
        this.threadFactory = threadFactory;
        this.executorService = executorService;
        this.numberOfThreads = numberOfThreads;
    }

    abstract protected void startThread(int threadNumber);
    abstract protected void cleanup();

    @Override
    public void run() throws InterruptedException {

        setStartTime();

        for (int i = 1; i <= numberOfThreads; i++) {
            startThread(i);
        }
        printRuntimeDuration("Parent Spawned All Children");
        cleanup();
        printRuntimeDuration("All Children Done");

    }
}
