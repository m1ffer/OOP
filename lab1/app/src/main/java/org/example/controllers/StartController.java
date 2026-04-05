package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.example.app.Application;
import org.example.models.StartModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StartController {
    private final StartModel startModel;
    private final ApplicationContext context;
    @FXML
    private Button startTxt;
    @FXML
    public void handleStartFromTxt() {
        if(startModel.deserializeTxt())
            startAnimation();
    }
    @FXML
    public void handleStartFromJson(){
        if(startModel.deserializeJson())
            startAnimation();
    }
    @FXML
    public void handleConfigure(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/org/example/app/creation-window.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) startTxt.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();   // окно принимает размер содержимого
            stage.show();
        } catch (IOException ignored) {}
    }

    private void startAnimation(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/org/example/app/view.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) startTxt.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();   // окно принимает размер содержимого
            stage.setResizable(false); // запрещаем изменение размера
            stage.show();
        } catch (IOException ignored) {}
    }
}
