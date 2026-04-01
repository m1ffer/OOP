package org.example.controllers;

import configs.HelicopterConfig;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.SceneModel;

public class HelicopterCreateController extends AbstractCreateController<HelicopterConfig> {
    public HelicopterCreateController(SceneModel scene) {
        super(scene);
    }

    @FXML
    private TextField peopleCount;
    @FXML
    private TextField loadWeight;
    @FXML
    private TextField lowerBound;
    @FXML
    private TextField upperBound;

    @Override
    protected HelicopterConfig buildConfigObject() {
        return new HelicopterConfig(Integer.parseInt(peopleCount.getText()),
                                    Integer.parseInt(loadWeight.getText()),
                                    Double.parseDouble(lowerBound.getText()),
                                    Double.parseDouble(upperBound.getText()));
    }
}
