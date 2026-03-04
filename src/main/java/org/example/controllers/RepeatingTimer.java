package org.example.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class RepeatingTimer {

    private final Timeline timeline;

    public RepeatingTimer(double seconds, Runnable action) {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(seconds), e -> action.run())
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
