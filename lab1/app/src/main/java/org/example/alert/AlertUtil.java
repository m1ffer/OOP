package org.example.alert;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

public class AlertUtil {
    private AlertUtil(){}


    public static void bindAndShowErrors(StringProperty observer, StringProperty listened){
        observer.bind(listened);
        observer.addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank())
                showError(newValue);
        });
    }

    public static void bindAndShowInfo(StringProperty observer, StringProperty listened){
        observer.bind(listened);
        observer.addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank())
                showInfo(newValue);
        });
    }

    public static void setProperty(StringProperty property, String text){
        run(() -> {
            property.set(text);
            property.set("");
        });
    }

    public static void showError(String message) {
        run(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void showInfo(String message) {
        run(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private static void run(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }
}
