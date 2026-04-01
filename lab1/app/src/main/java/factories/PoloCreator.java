package factories;

import classes.Animatable;
import classes.Polo;
import configs.PoloConfig;
import lombok.Getter;
import org.example.models.SceneModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PoloCreator extends AbstractCreator<PoloConfig>{
    public PoloCreator(ApplicationContext context, SceneModel model){
        super(context, model);
    }

    @Getter
    private final String displayText = "Polo";
    @Getter
    private final Class<PoloConfig> configType = PoloConfig.class;

    @Override
    public Animatable createObject(PoloConfig config) {
            return new Polo(
                    scene.getCanvasWidth(),
                    scene.getCanvasHeight(),
                    scene.getGroundLevel(),
                    config.peopleCount(),
                    config.loadWeight());
    }
}
