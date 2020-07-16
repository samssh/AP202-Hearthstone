package ir.sam.hearthstone.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaskTimer {
    private Thread thread;
    private Runnable task;
    private long time;
    private final AtomicBoolean isWaiting;

    public TaskTimer() {
        isWaiting = new AtomicBoolean();
    }

    public void setTask(Runnable task, long time) {
        cancelTask();
        this.task = task;
        this.time = time;
        thread = new Thread(this::run);
        isWaiting.set(true);
        thread.start();
    }

    public void cancelTask() {
        if (isWaiting.get())
            thread.interrupt();
    }

    private void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            return;
        }
        isWaiting.set(false);
        task.run();
    }
}
