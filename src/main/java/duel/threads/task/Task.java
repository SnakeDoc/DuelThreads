package duel.threads.task;

public interface Task extends Runnable {

    int getTaskNumber();

    default void printMessage(int value) {
        System.out.println("Task# " + getTaskNumber() + " | Value: " + value);
    }

    default void printMessage(long value) {
        System.out.println("Task# " + getTaskNumber() + " | Value: " + value);
    }

    default void printMessage(String value) {
        System.out.println("Task# " + getTaskNumber() + " | Value: " + value);
    }

}
