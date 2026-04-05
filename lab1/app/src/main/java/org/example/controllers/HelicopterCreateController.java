package org.example.controllers;

import configs.HelicopterConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class HelicopterCreateController implements VehicleCreateController<HelicopterConfig> {

    @FXML
    private TextField peopleCount;
    @FXML
    private TextField loadWeight;
    @FXML
    private TextField lowerBound;
    @FXML
    private TextField upperBound;

    @Override
    public HelicopterConfig buildConfig() {
        return new HelicopterConfig(Integer.parseInt(peopleCount.getText()),
                                    Integer.parseInt(loadWeight.getText()),
                                    Double.parseDouble(lowerBound.getText()),
                                    Double.parseDouble(upperBound.getText()));
    }
}
