package org.example.controllers;

import configs.PoloConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class PoloCreateController implements VehicleCreateController<PoloConfig> {
    @FXML
    private TextField peopleCount;

    @FXML
    private TextField loadWeight;

    @Override
    public PoloConfig buildConfig() {
        return new PoloConfig(Integer.parseInt(peopleCount.getText()),
                              Integer.parseInt(loadWeight.getText()));
    }
}