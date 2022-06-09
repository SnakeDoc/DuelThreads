package duel.threads.test.impl;

import duel.threads.task.impl.ChallengeThreeTask;
import duel.threads.test.ChallengeThreeTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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

        final int successCount = IntStream.rangeClosed(1, numberOfThreads)
                .parallel()
                .map(threadNumber -> PiValidator.isValid(new ChallengeThreeTask(threadNumber, getRandomPrecision()).get()) ? 1 : 0)
                .reduce(0, Integer::sum);

        System.out.println(successCount + " out of " + numberOfThreads + " - " + successCount / (double) numberOfThreads * 100 + "%");
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
