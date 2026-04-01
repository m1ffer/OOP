package org.example.controllers;

import configs.PoloConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;
import org.springframework.stereotype.Component;

@Component
public class PoloCreateController extends AbstractCreateController<PoloConfig> {
    public PoloCreateController(SceneModel scene) {
        super(scene);
    }

    @FXML
    private TextField peopleCount;

    @FXML
    private TextField loadWeight;

    @Override
    protected PoloConfig buildConfigObject() {
        return new PoloConfig(Integer.parseInt(peopleCount.getText()),
                              Integer.parseInt(loadWeight.getText()));
    }
}