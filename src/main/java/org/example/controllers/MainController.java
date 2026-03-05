package org.example.controllers;

import classes.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private Canvas canvas;  // связан с Canvas из FXML
    private GraphicsContext gc;
    private final static Image BACKGROUND = new Image(
            Helicopter.class
                    .getResource("/images/background.jpg")
                    .toExternalForm()
    );;
    private static final double GROUND_LEVEL = 645;

    private static final double TICK_TIME = 0.01;
    private double stopTime, moveTime;
    private MovingState movingState;
    private boolean isAppeared;
    private Animatable vehicle;
    private Queue<Timings> timings;
    private Queue<AnimatableAndActions> vehicles;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();

        getAnimatableAndActions();

        movingState = MovingState.MOVING;
        isAppeared = false;

        if (vehicles.isEmpty())
            Platform.exit();

        timings = vehicles.element().timings();
        stopTime = moveTime = 0;
        if (!timings.isEmpty()){
            stopTime = timings.element().stopTime();
            moveTime = timings.element().moveTime();
        }
        vehicle = vehicles.remove().animatable();

        new RepeatingTimer(TICK_TIME, () -> {
            clearCanvas();
            vehicle.draw(gc);
            if (!timings.isEmpty()){
                switch (movingState){
                    case MovingState.MOVING -> {
                        moveTime -= TICK_TIME;
                        if (moveTime <= 0){
                            vehicle.stop();
                            movingState = (MovingState.STOPPING);
                        }
                    }
                    case MovingState.STOPPING -> {
                        if (vehicle.isStopped()) {
                            movingState = (MovingState.STOPPED);
                            Arrays.stream(timings.element().actions()).
                                    forEach(i ->{
                                            switch (i.action()) {
                                                case (Action.SET_LOAD_WEIGHT) -> vehicle.setLoadWeight(i.value());
                                                case (Action.SET_PEOPLE_CNT) -> vehicle.setPeopleCNT(i.value());
                                            };
                                    });
                        }
                    }
                    case MovingState.STOPPED -> {
                        stopTime -= TICK_TIME;
                        if (stopTime <= 0){
                            vehicle.start();
                            timings.remove();
                            if (!timings.isEmpty()){
                                moveTime = timings.peek().moveTime();
                                stopTime = timings.peek().stopTime();
                            }
                            movingState = (MovingState.MOVING);
                        }
                    }
                }
            }
            if (!isAppeared && vehicle.isVisible()) {
                isAppeared = (true);
            }
            if (isAppeared && vehicle.isOutOfBounds()){
                if (vehicles.isEmpty()){
                    Platform.exit();
                }
                else {
                    vehicle = (vehicles.peek().animatable());
                    timings = (vehicles.remove().timings());
                    if (!timings.isEmpty()){
                        stopTime = timings.element().stopTime();
                        moveTime = timings.element().moveTime();
                    }
                    movingState = MovingState.MOVING;
                    isAppeared = false;
                }
            }
            vehicle.move(TICK_TIME);
        }).start();


    }

    private void getAnimatableAndActions() {
        vehicles = new ArrayDeque<>();
        //... Добавить объекты
        /*addPolo(1, 0,
                new Timings(
                     3, 2,
                    new ActionRecord(Action.SET_LOAD_WEIGHT, 200)),
                new Timings(2, 2,
                        new ActionRecord(Action.SET_LOAD_WEIGHT, 0)));
        addPolo(2, 0);
        addLamborghini(1, 0);
        addPlane(10, 1000);
        addHelicopter(4, 200);*/
        addLamborghini(2, 100,
                new Timings(3, 2));
    }

    private void addPolo(int peopleCNT, int loadWeight, Timings... timings){
        vehicles.add(new AnimatableAndActions(
                new Polo(
                        canvas.getWidth(),
                        canvas.getHeight(),
                        GROUND_LEVEL,
                        peopleCNT,
                        loadWeight
                ),
                timings
        ));
    }

    private void addLamborghini(int peopleCNT, int loadWeight, Timings... timings){
        vehicles.add(new AnimatableAndActions(
                new Lamborghini(
                        canvas.getWidth(),
                        canvas.getHeight(),
                        GROUND_LEVEL,
                        peopleCNT,
                        loadWeight
                ),
                timings
        ));
    }

    private void addPlane(int peopleCNT, int loadWeight){
        vehicles.add(
                new AnimatableAndActions(
                        new Plane(
                                canvas.getWidth(),
                                canvas.getHeight(),
                                peopleCNT,
                                loadWeight,
                                GROUND_LEVEL - Plane.HEIGHT * 2.5
                        )
                )
        );
    }

    private void addHelicopter(int peopleCNT, int loadWeight){
        vehicles.add(
                new AnimatableAndActions(
                        new Helicopter(
                                canvas.getWidth(),
                                canvas.getHeight(),
                                peopleCNT,
                                loadWeight,
                                GROUND_LEVEL - 400,
                                5
                        )
                )
        );
    }

    private void clearCanvas(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(BACKGROUND, 0, 0,
                canvas.getWidth(), canvas.getHeight());
    }

    private enum MovingState{
        MOVING,
        STOPPING,
        STOPPED
    }

    private enum Action{
        SET_LOAD_WEIGHT,
        SET_PEOPLE_CNT
    }

    private record ActionRecord(Action action, int value){}

    private record Timings(double moveTime, double stopTime, ActionRecord... actions){}

    private static class AnimatableAndActions{
        private final Animatable animatable;
        private final Queue<Timings> timings;

        public Animatable animatable(){
            return animatable;
        }

        public Queue<Timings> timings(){
            return timings;
        }

        public AnimatableAndActions(Animatable animatable, Timings... timings){
            this.animatable = animatable;
            this.timings = new ArrayDeque<>(Arrays.asList(timings));
        }
    }

}