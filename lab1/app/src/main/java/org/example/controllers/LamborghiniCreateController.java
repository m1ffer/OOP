package org.example.controllers;

import configs.LamborghiniConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class LamborghiniCreateController extends AbstractCreateController<LamborghiniConfig> {
    public LamborghiniCreateController(SceneModel scene) {
        super(scene);
    }
    @FXML
    private TextField peopleCount;
    @FXML
    private TextField loadWeight;

    @Override
    public LamborghiniConfig buildConfigObject() {
        return new LamborghiniConfig(Integer.parseInt(peopleCount.getText()),
                                     Integer.parseInt(loadWeight.getText()));
    }
}
