package org.example.controllers;

import javafx.animation.AnimationTimer;

import java.util.function.Consumer;

public class RepeatingTimer {

    private final AnimationTimer timer;
    private boolean running = false;
    private long lastTime = 0;

    public RepeatingTimer(Consumer<Double> action) {
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running) return;

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double delta = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                action.accept(delta);
            }
        };
    }

    public void start() {
        running = true;
        lastTime = 0;
        timer.start();
    }

    public void stop() {
        running = false;
        timer.stop();
    }

    public void pause() {
        running = false;
    }
}