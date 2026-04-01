package org.example.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.example.alert.AlertUtil;
import org.example.app.Application;
import org.example.models.CreationModel;
import org.example.models.HasDisplayText;
import org.example.models.SceneModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CreationController {
    private final SceneModel scene;
    private final CreationModel creationModel;
    private final ApplicationContext context;
    private final StringProperty error = new SimpleStringProperty("");
    private final StringProperty info = new SimpleStringProperty("");

    @FXML
    private ComboBox<HasDisplayText> vehicleComboBox;

    @FXML
    private VBox configContainer;

    @FXML
    public void initialize() {
        AlertUtil.bindAndShowErrors(error, scene.getError());
        AlertUtil.bindAndShowInfo(info, scene.getInfo());
        vehicleComboBox.getItems().addAll(
                creationModel.getAvailableCreators()
        );

        vehicleComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(HasDisplayText item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getDisplayText());
            }
        });

        vehicleComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(HasDisplayText item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getDisplayText());
            }
        });

        vehicleComboBox.setOnAction(e -> onSelected());
    }

    private void onSelected() {
        Node form = creationModel.selectCreator(vehicleComboBox.getValue());
        if (form != null)
            configContainer.getChildren().setAll(form);
        else
            System.out.println("Форма null");
    }

    @FXML
    private void addVehicle() {
        creationModel.createVehicle();
    }
    @FXML
    private void startAnimation(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/org/example/app/view.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) configContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.sizeToScene();   // окно принимает размер содержимого
            stage.setResizable(false); // запрещаем изменение размера
            stage.show();
        }
        catch(IOException e){}
    }
}