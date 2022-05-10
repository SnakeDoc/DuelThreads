package duel.threads.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

public abstract class ChallengeOneTest extends BasicTest {

    protected final ThreadFactory threadFactory;
    protected final ExecutorService executorService;
    protected final int numberOfThreads;

    protected final List<Thread> threads = new ArrayList<>();
    protected final CountDownLatch parent = new CountDownLatch(1);
    protected final CyclicBarrier child = new CyclicBarrier(2);

    protected ChallengeOneTest(final ExecutorService executorService, int numberOfThreads) {
        this(null, executorService, numberOfThreads);
    }

    protected ChallengeOneTest(final ThreadFactory threadFactory, int numberOfThreads) {
        this(threadFactory, null, numberOfThreads);
    }

    protected ChallengeOneTest(final ThreadFactory threadFactory, final ExecutorService executorService, final int numberOfThreads) {
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

            try {
                child.await(); // wait for child to complete before starting next thread
            } catch (BrokenBarrierException e) {
                throw new InterruptedException(e.getMessage());
            }
            child.reset();

        }

        printRuntimeDuration("Parent Spawned All Children");

        parent.countDown(); // release children
        cleanup();

        printRuntimeDuration("All Children Done");

    }

}
