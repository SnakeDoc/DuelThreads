package duel.threads.test.impl;

import duel.threads.task.impl.ChallengeOneTask;
import duel.threads.test.BasicTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadFactory;

public class ChallengeOneTest extends BasicTest {

    private final ThreadFactory threadFactory;
    private final int numberOfThreads;

    public ChallengeOneTest(final ThreadFactory threadFactory, int numberOfThreads) {
        this.threadFactory = threadFactory;
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void run() throws InterruptedException {

        final List<Thread> threads = new ArrayList<>();
        final CountDownLatch parent = new CountDownLatch(1);
        final CyclicBarrier child = new CyclicBarrier(2);

        setStartTime();

        for (int i = 1; i <= numberOfThreads; i++) {

            final Thread thread = threadFactory.newThread(new ChallengeOneTask(i, parent, child));

            threads.add(thread);
            thread.start();

            try {
                child.await(); // wait for child to complete before starting next thread
            } catch (BrokenBarrierException e) {
                throw new InterruptedException(e.getMessage());
            }
            child.reset();

        }

        printRuntimeDuration("Parent Spawned All Children");

        parent.countDown(); // release children
        for (Thread thread : threads) {
            thread.join();
        }

        printRuntimeDuration("All Children Done");

    }

}
