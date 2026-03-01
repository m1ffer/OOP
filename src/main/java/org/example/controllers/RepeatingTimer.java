package org.example.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class RepeatingTimer {

    private final Timeline timeline;

    public RepeatingTimer(long millis, Runnable action) {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(millis), e -> action.run())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void pause() {
        timeline.pause();
    }
}
