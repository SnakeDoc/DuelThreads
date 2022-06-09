package duel.threads.test.impl;

import duel.threads.task.impl.ChallengeOneTask;
import duel.threads.test.ChallengeOneTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ChallengeOneAsyncTest extends ChallengeOneTest {

    private final List<CompletableFuture<?>> futures = new ArrayList<>();

    public ChallengeOneAsyncTest(ExecutorService executorService, int numberOfThreads) {
        super(executorService, numberOfThreads);
    }

    public ChallengeOneAsyncTest(ThreadFactory threadFactory, int numberOfThreads) {
        super(Executors.newThreadPerTaskExecutor(threadFactory), numberOfThreads);
    }

    @Override
    protected void startThread(int threadNumber) {
        final CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new ChallengeOneTask(threadNumber, parent, child), this.executorService);
        this.futures.add(completableFuture);
    }

    @Override
    protected void cleanup() {
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }
}
