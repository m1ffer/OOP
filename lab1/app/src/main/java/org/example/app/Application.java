package org.example.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("app");
        stage.setScene(scene);
        stage.sizeToScene();   // окно принимает размер содержимого
        stage.setResizable(false); // запрещаем изменение размера
        stage.show();
    }
}
