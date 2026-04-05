package org.example.controllers;

import configs.LamborghiniConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class LamborghiniCreateController implements VehicleCreateController<LamborghiniConfig> {
    @FXML
    private TextField peopleCount;
    @FXML
    private TextField loadWeight;

    @Override
    public LamborghiniConfig buildConfig() {
        return new LamborghiniConfig(Integer.parseInt(peopleCount.getText()),
                                     Integer.parseInt(loadWeight.getText()));
    }
}
