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
    private final Map<Class<? extends Config>, VehicleCreator<? extends Config>> creators;

    public VehicleFactory(List<VehicleCreator<? extends Config>> creatorsList){
        this.creators = creatorsList.stream()
                .collect(Collectors.toMap(
                        VehicleCreator::getConfigType,
                        Function.identity()
                ));
    }

    @SuppressWarnings("unchecked")
    public <C extends Config> Animatable<C> create(C config){
        VehicleCreator<C> creator =
                (VehicleCreator<C>) creators.get(config.getClass());
        return creator.create(config);
    }

    @SuppressWarnings("unchecked")
    public <C extends Config> Animatable<C> rebuild(C config){
        VehicleCreator<C> creator =
                (VehicleCreator <C>) creators.get(config.getClass());
        return creator.rebuild(config);
    }
}
