package org.example.models;

import classes.Animatable;
import configs.Config;
import factories.VehicleFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.alert.AlertUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnimationModel {
    private final List<Config> configs = new ArrayList<>();
    private final List<Animatable> vehicles = new ArrayList<>();
    private final VehicleFactory factory;
    private final SceneModel scene;
    private Animatable vehicle = null;
    private boolean hasAppeared = false;
    @Setter
    private GraphicsContext gc;
    public boolean haveVehicles(){
        return vehicle != null || !vehicles.isEmpty();
    }
    public void stop(){
        try {
            vehicle.stop();
        }
        catch(Throwable e){
            scene.setError(e.getMessage());
        }
    }
    public void start(){
        vehicle.start();
    }
    public boolean isStopped(){
        return vehicle.isStopped();
    }
    public void setPeopleCount(int peopleCount){
        vehicle.setPeopleCNT(peopleCount);
    }
    public void setLoadWeight(int loadWeight){
        vehicle.setLoadWeight(loadWeight);
    }
    public void addVehicle(Config config){
        configs.add(config);
    }
    public void initVehicles(){
        if (!configs.isEmpty()) {
            configs.forEach(i -> {
                try {
                    vehicles.add(factory.create(i));
                }
                catch(IllegalArgumentException e){
                    AlertUtil.setProperty(scene.getError(), "Не удалось создать транспорт: " + e.getMessage());
                }
                catch(Throwable e){
                    AlertUtil.setProperty(scene.getError(), "Что-то пошло не так");
                    e.printStackTrace();
                }
            });
            if (!vehicles.isEmpty())
                vehicle = vehicles.removeFirst();
        }
        else{
            AlertUtil.setProperty(scene.getError(), "Вы не добавили никакого транспорта");
        }
    }
    public void update(double delta){
        if (vehicle == null)
            vehicle = vehicles.removeFirst();
        vehicle.move(delta);
        if (!hasAppeared && vehicle.isVisible())
            hasAppeared = true;
        if (hasAppeared && !vehicle.isVisible()){
            vehicle = null;
            hasAppeared = false;
        }
    }
    public void draw(){
        vehicle.draw(gc);
    }
}
