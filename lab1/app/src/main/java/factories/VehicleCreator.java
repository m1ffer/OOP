package factories;

import classes.Animatable;
import configs.Config;
import org.example.forms.FormResult;
import org.example.models.HasDisplayText;

import java.io.IOException;

public interface VehicleCreator <C extends Config> extends HasDisplayText {
    Class<C> getConfigType();
    Animatable create(C config);
    FormResult<C> createForm() throws IOException;
}
