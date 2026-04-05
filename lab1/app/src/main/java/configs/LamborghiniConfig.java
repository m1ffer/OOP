package configs;

import classes.GroundVehicle;
import classes.Lamborghini;
import classes.SceneObject;

public record LamborghiniConfig(int peopleCount,
                                int loadWeight,
                                Double x,
                                Double y,
                                Double elapsedTime,
                                GroundVehicle.GroundState state,
                                Double currentSpeedFactor,
                                Double acceleration) implements Config {
    public LamborghiniConfig{
        SceneObject.validateMax(peopleCount, Lamborghini.MAX_PEOPLE_COUNT,
                "Превышено максимальное количество людей (" + Lamborghini.MAX_PEOPLE_COUNT + ")");
        SceneObject.validatePositive(peopleCount,
                "Количество людей должно быть положительным");
        SceneObject.validateMax(loadWeight, Lamborghini.LOAD_CAPACITY,
                "Превышен максимальный вес груза (" + Lamborghini.LOAD_CAPACITY + ")");
        SceneObject.validateNonNegative(loadWeight,
                "Вес не может быть отрицательным");
    }
    public LamborghiniConfig(int peopleCount,
                             int loadWeight){
        this(peopleCount, loadWeight,
                null, null, null,
                null, null, null);
    }
    public boolean isRebuild(){
        return x != null;
    }
}
