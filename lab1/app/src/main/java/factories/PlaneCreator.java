package factories;

import classes.Animatable;
import classes.Plane;
import classes.SceneObject;
import configs.PlaneConfig;
import lombok.Getter;
import org.example.forms.FormResult;
import org.example.models.SceneModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PlaneCreator extends AbstractCreator<PlaneConfig>{
    public PlaneCreator(ApplicationContext context, SceneModel scene) {
        super(context, scene);
    }

    @Getter
    private final Class<PlaneConfig> configType = PlaneConfig.class;
    @Getter
    private final String displayText = "Plane";

    @Override
    public Animatable<PlaneConfig> create(PlaneConfig config) {
        SceneObject.validateMaxEquals(config.startY() + Plane.HEIGHT, scene.getGroundLevel(),
                "Самолет не может лететь так низко");
        return new Plane(scene.getCanvasWidth(),
                scene.getCanvasHeight(),
                config.peopleCount(),
                config.loadWeight(),
                config.startY());
    }
}
