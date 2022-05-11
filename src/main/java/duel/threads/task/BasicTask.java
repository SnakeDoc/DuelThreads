package duel.threads.task;

import java.util.Random;

public abstract class BasicTask implements Task {

    private final int taskNumber;
    protected static final Random RANDOM = new Random();

    public BasicTask(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public int getTaskNumber() {
        return taskNumber;
    }

    @Override
    public void run() {
        printMessage(RANDOM.nextInt());
    }
}
