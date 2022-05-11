package duel.threads.task.impl;

import duel.threads.task.BasicTask;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ChallengeOneTask extends BasicTask {

    final CountDownLatch parent;
    final CyclicBarrier child;

    public ChallengeOneTask(int taskNumber, final CountDownLatch parent, final CyclicBarrier child) {
        super(taskNumber);
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void run() {

        final int value = RANDOM.nextInt() * RANDOM.nextInt();

        System.out.println(getTaskNumber() + " | " + value);

        try {
            child.await(); // tell parent to start next thread
            parent.await(); // wait for parent to release all children
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

    }
}
