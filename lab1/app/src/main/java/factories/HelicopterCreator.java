package factories;

import classes.Animatable;
import classes.Helicopter;
import classes.SceneObject;
import configs.HelicopterConfig;
import lombok.Getter;
import org.example.forms.FormResult;
import org.example.models.SceneModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HelicopterCreator extends AbstractCreator<HelicopterConfig> {
    public HelicopterCreator(ApplicationContext context, SceneModel scene) {
        super(context, scene);
    }

    @Getter
    private final Class<HelicopterConfig> configType = HelicopterConfig.class;
    @Getter
    private final String displayText = "Helicopter";

    @Override
    public Animatable createObject(HelicopterConfig config) {
        SceneObject.validateMaxEquals(config.lowerBound(), scene.getGroundLevel(),
                "Вертолёт не может лететь так низко");
        return new Helicopter(scene.getCanvasWidth(),
                scene.getCanvasHeight(),
                config.peopleCount(),
                config.loadWeight(),
                config.lowerBound(),
                config.upperBound());
    }
}
