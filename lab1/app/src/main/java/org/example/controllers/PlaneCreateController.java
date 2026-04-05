package org.example.controllers;

import classes.Plane;
import configs.PlaneConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class PlaneCreateController implements VehicleCreateController<PlaneConfig> {
    @FXML
    private TextField peopleCount;
    @FXML
    private TextField loadWeight;
    @FXML
    private TextField startY;

    @Override
    public PlaneConfig buildConfig() {
        return new PlaneConfig(Integer.parseInt(peopleCount.getText()),
                               Integer.parseInt(loadWeight.getText()),
                               Double.parseDouble(startY.getText()));
    }
}
