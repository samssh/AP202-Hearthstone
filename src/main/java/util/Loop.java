package util;

import lombok.SneakyThrows;


public class Loop implements Runnable, Updatable {
    private final int fps;
    private volatile boolean running = false;
    protected Thread thread;
    private Updatable updatable;


    public Loop(int fps) {
        this.fps = fps;
        thread = new Thread(this);
    }

    public Loop(int fps, Updatable updatable) {
        this.fps = fps;
        this.updatable = updatable;
        thread = new Thread(this);
    }

    public void update() {
        if (updatable != null)
            updatable.update();
    }

    @Override
    public void run() {
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
                update();
                delta--;
            }
        }
    }

    @SneakyThrows
    public void sleep(long time){
        int milliseconds = (int) (time) / 1000000;
        int nanoseconds = (int) (time) % 1000000;
        Thread.sleep(milliseconds, nanoseconds);
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void restart() {
        thread = new Thread(this);
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