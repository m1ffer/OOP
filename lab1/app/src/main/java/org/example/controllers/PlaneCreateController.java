package org.example.controllers;

import classes.Plane;
import configs.PlaneConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class PlaneCreateController extends AbstractCreateController<PlaneConfig> {
    public PlaneCreateController(SceneModel scene) {
        super(scene);
    }
    @FXML
    private TextField peopleCount;
    @FXML
    private TextField loadWeight;
    @FXML
    private TextField startY;

    @Override
    protected PlaneConfig buildConfigObject() {
        return new PlaneConfig(Integer.parseInt(peopleCount.getText()),
                               Integer.parseInt(loadWeight.getText()),
                               Double.parseDouble(startY.getText()));
    }
}
