package duel.threads.task.impl;

import duel.threads.task.BasicTask;

import java.util.concurrent.CountDownLatch;

public class ChallengeOneTask extends BasicTask {

    final CountDownLatch parent;
    final CountDownLatch child;

    public ChallengeOneTask(int taskNumber, final CountDownLatch parent, final CountDownLatch child) {
        super(taskNumber);
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void run() {

        final int value = RANDOM.nextInt() * RANDOM.nextInt();

        System.out.println(getTaskNumber() + " | " + value);

        // tell parent to start next thread
        child.countDown();

        try {
            parent.await(); // wait for parent to release all children
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
