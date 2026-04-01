package org.example.models;

import configs.Config;
import factories.VehicleCreator;
import javafx.scene.Node;
import lombok.RequiredArgsConstructor;
import org.example.alert.AlertUtil;
import org.example.controllers.VehicleCreateController;
import org.example.forms.FormResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreationModel {
    private final List<VehicleCreator<?>> creators;
    private final AnimationModel animationModel;
    private final SceneModel scene;
    
    private VehicleCreateController<?> currentController;

    public List<VehicleCreator<?>> getAvailableCreators() {
        return creators;
    }

    public Node selectCreator(HasDisplayText creator) {
        try {
            VehicleCreator<?> currentCreator = (VehicleCreator<?>) creator;
            FormResult<?> result = currentCreator.createForm();
            currentController = result.form();
            return result.node();
        }
        catch(Throwable e){
            AlertUtil.setProperty(scene.getError(), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void createVehicle() {
        try {
            Config config = currentController.buildConfig();
            animationModel.addVehicle(config);
            AlertUtil.setProperty(scene.getInfo(),
                    "Транспорт добавлен");
        }
        catch(NumberFormatException ignored){
            AlertUtil.setProperty(scene.getError(),
                    "Некорректные числовые значения");
        }
        catch(IllegalArgumentException e){
            AlertUtil.setProperty(scene.getError(),
                    e.getMessage());
        }
        catch(Throwable e){
            AlertUtil.setProperty(scene.getError(),
                    e.getMessage());
            e.printStackTrace();
        }
    }
}
