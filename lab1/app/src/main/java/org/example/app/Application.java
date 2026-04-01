package org.example.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.configurations.AppConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private AnnotationConfigApplicationContext context = null;
    @Override
    public void init(){
        try {
            context = new AnnotationConfigApplicationContext(AppConfiguration.class);
            context.registerShutdownHook();
        }
        catch(Throwable e){
            if (context != null && context.isActive())
                context.close();
            throw e;
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/org/example/app/creation-window.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("app");
        stage.setScene(scene);
        stage.sizeToScene();   // окно принимает размер содержимого
        stage.show();
    }
    @Override
    public void stop(){
        context.close();
    }
}
