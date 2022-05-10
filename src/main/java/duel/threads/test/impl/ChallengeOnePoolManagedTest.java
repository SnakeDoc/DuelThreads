package duel.threads.test.impl;

import duel.threads.task.impl.ChallengeOneTask;
import duel.threads.test.ChallengeOneTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class ChallengeOnePoolManagedTest extends ChallengeOneTest {

    public ChallengeOnePoolManagedTest(final ThreadFactory threadFactory, int numberOfThreads) {
        super(Executors.newThreadPerTaskExecutor(threadFactory), numberOfThreads);
    }

    public ChallengeOnePoolManagedTest(final ExecutorService executorService, int numberOfThreads) {
        super(executorService, numberOfThreads);
    }

    @Override
    protected void startThread(int threadNumber) {
        executorService.execute(new ChallengeOneTask(threadNumber, parent, child));
    }

    @Override
    protected void cleanup() {
        executorService.shutdown();
    }

}
