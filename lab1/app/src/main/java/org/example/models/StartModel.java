package org.example.models;

import configs.Config;
import lombok.RequiredArgsConstructor;
import org.example.alert.AlertUtil;
import org.springframework.stereotype.Component;
import serialization.DeserializationModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartModel {
    private final AnimationModel animationModel;
    private final SceneModel scene;
    private final DeserializationModel deserialization;
    public boolean deserializeTxt(){
        List<Config> configs = deserialization.deserializeConfigsTxt();
        if (!configs.isEmpty()){
            animationModel.addVehicles(configs);
            return true;
        }
        else{
            AlertUtil.setProperty(scene.getError(), "Отсутствуют файлы");
            return false;
        }
    }
    public boolean deserializeJson(){
        List<Config> configs = deserialization.deserializeConfigsJson();
        if (!configs.isEmpty()){
            animationModel.addVehicles(configs);
            return true;
        }
        else{
            AlertUtil.setProperty(scene.getError(), "Отсутствуют файлы");
            return false;
        }
    }
}
