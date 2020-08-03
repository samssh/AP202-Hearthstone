package ir.sam.hearthstone.util;

import lombok.SneakyThrows;


public class Loop {
    private final int fps;
    private volatile boolean running = false;
    protected Thread thread;
    private final Updatable updatable;

    public Loop(int fps, Updatable updatable) {
        this.fps = fps;
        this.updatable = updatable;
        thread = new Thread(this::run);
    }

    public void update() {
        if (updatable != null)
            updatable.update();
    }

    private void run() {
        long lastTime = System.nanoTime();
        int ns_per_update = 1000000000 / fps;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) * 1.0 / ns_per_update;
            lastTime = now;
            if (delta < 1) {
                sleep((long) (ns_per_update * (1 - delta)));
            }
            while (running && delta >= 1) {
                try {
                    update();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                delta--;
            }
        }
    }

    @SneakyThrows
    public void sleep(long time) {
        int milliseconds = (int) (time) / 1000000;
        int nanoseconds = (int) (time) % 1000000;
        Thread.sleep(milliseconds, nanoseconds);
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void restart() {
        thread = new Thread(this::run);
        running = true;
        thread.start();
    }

    @SneakyThrows
    public void stop() {
        running = false;
        if (Thread.currentThread().equals(thread))
            return;
        thread.join();
    }
}