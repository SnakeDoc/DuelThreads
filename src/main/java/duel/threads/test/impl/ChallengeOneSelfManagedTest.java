package duel.threads.test.impl;

import duel.threads.task.impl.ChallengeOneTask;
import duel.threads.test.ChallengeOneTest;

import java.util.concurrent.ThreadFactory;

public class ChallengeOneSelfManagedTest extends ChallengeOneTest {

    public ChallengeOneSelfManagedTest(final ThreadFactory threadFactory, int numberOfThreads) {
        super(threadFactory, numberOfThreads);
    }

    @Override
    protected void startThread(int threadNumber) {
        final Thread thread = threadFactory.newThread(new ChallengeOneTask(threadNumber, parent, child));
        threads.add(thread);
        thread.start();
    }

    @Override
    protected void cleanup() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
