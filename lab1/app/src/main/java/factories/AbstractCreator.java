package factories;

import classes.Animatable;
import configs.Config;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.RequiredArgsConstructor;
import org.example.controllers.VehicleCreateController;
import org.example.forms.FormResult;
import org.example.models.SceneModel;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractCreator <C extends Config> implements VehicleCreator<C> {
    protected final ApplicationContext context;
    protected final SceneModel scene;
    public abstract Class<C> getConfigType();
    public abstract Animatable<C> create(C config);
    public Animatable<C> rebuild(C config){
        Animatable<C> res = create(config);
        if (config.isRebuild())
            res.rebuild(config);
        return res;
    }
    public FormResult<C> createForm() throws IOException{
        return createForm("/org/example/app/" + getDisplayText().toLowerCase() + "-create.fxml");
    }
    protected FormResult<C> createForm(String path) throws IOException{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(path)
            );
            loader.setControllerFactory(context::getBean);
            Node node = loader.load();
            VehicleCreateController<C> controller = loader.getController();
            return new FormResult<>(node, controller);
    }
}
