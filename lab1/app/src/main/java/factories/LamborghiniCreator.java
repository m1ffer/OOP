package factories;

import classes.Animatable;
import classes.Lamborghini;
import configs.LamborghiniConfig;
import lombok.Getter;
import org.example.models.SceneModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LamborghiniCreator extends AbstractCreator<LamborghiniConfig>{
    public LamborghiniCreator(ApplicationContext context, SceneModel scene) {
        super(context, scene);
    }
    @Getter
    private final String displayText = "Lamborghini";
    @Getter
    private final Class<LamborghiniConfig> configType = LamborghiniConfig.class;

    @Override
    public Animatable<LamborghiniConfig> create(LamborghiniConfig config) {
        return new Lamborghini(scene.getCanvasWidth(),
                scene.getCanvasHeight(),
                scene.getGroundLevel(),
                config.peopleCount(),
                config.loadWeight());
    }
}
