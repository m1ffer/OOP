package org.example.controllers;

import classes.Helicopter;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import org.example.alert.AlertUtil;
import org.example.models.AnimationModel;
import org.example.models.SceneModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AnimationController implements Initializable {
    @FXML
    private Canvas canvas;
    @FXML
    private Button pauseButton;
    @FXML
    private Button saveTxt;
    @FXML
    private Button saveJson;
    private GraphicsContext gc;
    private final static Image BACKGROUND = new Image(
            AnimationController.class
                    .getResource("/images/background.jpg")
                    .toExternalForm()
    );
    private final SceneModel scene;
    private final AnimationModel animationModel;
    private final AnnotationConfigApplicationContext context;
    private static final double GROUND_LEVEL = 645;
    private RepeatingTimer timer;
    private final StringProperty error = new SimpleStringProperty("");
    private final StringProperty info = new SimpleStringProperty("");
    private final BooleanProperty running = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AlertUtil.bindAndShowErrors(error, scene.getError());
        AlertUtil.bindAndShowInfo(info, scene.getInfo());
        gc = canvas.getGraphicsContext2D();
        animationModel.setGc(gc);
        scene.setCanvasWidth(canvas.getWidth());
        scene.setCanvasHeight(canvas.getHeight());
        scene.setGroundLevel(GROUND_LEVEL);
        animationModel.initVehicles();
        timer = new RepeatingTimer(delta -> {
            if (animationModel.haveVehicles()) {
                clearCanvas();
                animationModel.draw();
                animationModel.update(delta);
            } else {
                closeApp();
            }
        });
        running.bindBidirectional(timer.getRunning());
        saveTxt.visibleProperty().bind(running.not());
        saveJson.visibleProperty().bind(running.not());
        saveTxt.managedProperty().bind(saveTxt.visibleProperty());
        saveJson.managedProperty().bind(saveJson.visibleProperty());
        timer.start();
    }
    private void clearCanvas(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(BACKGROUND, 0, 0,
                canvas.getWidth(), canvas.getHeight());
    }

    private void closeApp(){
        timer.stop();
        Platform.exit();
    }

    @FXML
    public void handlePause(){
        running.set(!running.get());
    }

    @FXML
    public void handleSaveText(){
        animationModel.saveTxt();
    }

    @FXML
    public void handleSaveJson(){
        animationModel.saveJson();
    }
}
