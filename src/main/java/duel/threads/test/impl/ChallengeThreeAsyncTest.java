package duel.threads.test.impl;

import duel.threads.task.impl.ChallengeThreeTask;
import duel.threads.test.ChallengeThreeTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

public class ChallengeThreeAsyncTest extends ChallengeThreeTest {

    private static final Random RANDOM = new Random();
    private static final String VALIDATION_STRING;

    static {
        try {
            VALIDATION_STRING = Files.readString(Paths.get("./src/main/resources/500_000_digits.pi"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ChallengeThreeAsyncTest(ExecutorService executorService, int numberOfThreads) {
        super(executorService, numberOfThreads);
    }

    public ChallengeThreeAsyncTest(ThreadFactory threadFactory, int numberOfThreads) {
        super(Executors.newThreadPerTaskExecutor(threadFactory), numberOfThreads);
    }

    @Override
    protected void startThread(int threadNumber) {

    }

    @Override
    public void run() throws InterruptedException {

        // TODO try map and reduce to just successes... we already know how many we spawned
        var completableFutures = new ArrayList<CompletableFuture<Boolean>>();
        for (int i = 1; i <= numberOfThreads; i++) {
            final var precision = getRandomPrecision();
            final var threadNumber = i;
            System.out.println("Thread# " + threadNumber + " | precision: " + precision);

            completableFutures.add(
                    CompletableFuture.supplyAsync(new ChallengeThreeTask(threadNumber, precision), executorService)
                            .thenApplyAsync(PiValidator::isValid));
        }

        completableFutures.parallelStream().forEach((future) -> {
            try {
                System.out.println("Thread # " + " result: " + future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void cleanup() {
        executorService.shutdown();
    }

    private int getRandomPrecision() {
        var rnd = ThreadLocalRandom.current().nextInt(1, 25 + 1);
        return rnd;
    }

    private class PiValidator {
        public static boolean isValid(final ChallengeThreeTask.PiCalculation pi) {
            final var validator = new BigDecimal(VALIDATION_STRING.substring(0, pi.precision() + 2)).setScale(pi.precision(), RoundingMode.HALF_UP).toString();
            var isValid = pi.pi().toString().equals(validator);
            if (!isValid) System.out.println("Failed | C: " + pi.pi() + " R: " + validator);
            return isValid;
        }
    }
}
