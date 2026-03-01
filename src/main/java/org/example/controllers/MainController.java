package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import org.example.app.PngDrawer;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Canvas canvas;  // связан с Canvas из FXML

    private PngDrawer drawer;
    private boolean isDrawed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double imageWidth = canvas.getWidth();
        double imageHeight = canvas.getHeight();

        drawer = new PngDrawer("D:\\im.png", imageWidth, imageHeight);
        RepeatingTimer timer = new RepeatingTimer(1000, () ->{
            if (isDrawed){
                drawer.clear(canvas);
                isDrawed = false;
            }
            else{
                drawer.draw(canvas);
                isDrawed = true;
            }
        });
        timer.start();
    }
}