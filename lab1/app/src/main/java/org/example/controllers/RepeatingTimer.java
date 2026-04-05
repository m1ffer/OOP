package org.example.controllers;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;

import java.util.function.Consumer;

public class RepeatingTimer {

    private final AnimationTimer timer;
    @Getter
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private long lastTime = 0;

    public RepeatingTimer(Consumer<Double> action) {
        running.addListener((ignored) -> {
            lastTime = 0;
        });
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running.get()) return;

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
        run();
        timer.start();
    }

    public void stop() {
        pause();
        timer.stop();
    }

    public void pause() {
        running.set(false);
    }
    public void run(){
        running.set(true);
    }
}