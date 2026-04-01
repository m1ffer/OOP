package factories;

import classes.Animatable;
import configs.Config;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class VehicleFactory {
    private final Map<Class<? extends Config>, VehicleCreator<?>> creators;
    public VehicleFactory(List<VehicleCreator<?>> creatorsList){
        this.creators = creatorsList.stream()
                .collect(Collectors.toMap(
                        VehicleCreator::getConfigType,
                        Function.identity()
                ));
    }
    @SuppressWarnings("unchecked")
    public <C extends Config> Animatable create(C config){
        VehicleCreator<C> creator =
                (VehicleCreator<C>) creators.get(config.getClass());
        return creator.create(config);
    }
}
