package org.example.models;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SceneModel {
    @Getter
    private final StringProperty error = new SimpleStringProperty("");
    @Getter
    private final StringProperty info = new SimpleStringProperty("");
    private void setPropertyText(StringProperty property, String text){
        if (Platform.isFxApplicationThread())
            property.set(text);
        else
            Platform.runLater(() -> property.set(text));
    }
    public void setError(String errorMessage){
        setPropertyText(error, errorMessage);
    }
    public void setInfo(String infoMessage){
        setPropertyText(info, infoMessage);
    }
    @Getter @Setter
    private AnnotationConfigApplicationContext context;
    @Getter @Setter
    private double canvasWidth;
    @Getter @Setter
    private double canvasHeight;
    @Getter @Setter
    private double groundLevel;
}
