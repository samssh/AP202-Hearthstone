package controller;

import lombok.SneakyThrows;

import java.util.List;

public class Loop implements Runnable, Updatable {
    private final int fps;
    private boolean running = false;
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

    @SneakyThrows
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
                int milliseconds = (int) (ns_per_update * (1 - delta)) / 1000000;
                int nanoseconds = (int) (ns_per_update * (1 - delta)) % 1000000;
                Thread.sleep(milliseconds, nanoseconds);
            }
            while (running && delta >= 1) {
                update();
                delta--;
            }
        }
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
        thread.join();
    }
}