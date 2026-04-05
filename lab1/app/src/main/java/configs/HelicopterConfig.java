package configs;

import classes.Helicopter;
import classes.SceneObject;

public record HelicopterConfig(int peopleCount,
                               int loadWeight,
                               double lowerBound,
                               double upperBound,
                               Double x,
                               Double y,
                               Double elapsedTime) implements Config {
    public HelicopterConfig{
        SceneObject.validateMax(peopleCount, Helicopter.MAX_PEOPLE_COUNT,
                "Превышено максимальное количество людей (" + Helicopter.MAX_PEOPLE_COUNT + ")");
        SceneObject.validatePositive(peopleCount,
                "Количество людей должно быть положительным");
        SceneObject.validateMax(loadWeight, Helicopter.LOAD_CAPACITY,
                "Превышен максимальный вес груза (" + Helicopter.LOAD_CAPACITY + ")");
        SceneObject.validateNonNegative(loadWeight,
                "Вес не может быть отрицательным");
        SceneObject.validateNonNegative(upperBound,
                "Ордината верхней границы должна быть неотрицательной");
        SceneObject.validatePositive(lowerBound,
                "Ордината нижней границы должна быть положительной");
        SceneObject.validatePositive(lowerBound - upperBound - Helicopter.HEIGHT,
                "Разница между нижней и верхней границей должна составлять хотя бы " + Helicopter.HEIGHT);
    }
    public HelicopterConfig(int peopleCount,
                            int loadWeight,
                            double lowerBound,
                            double upperBound){
        this(peopleCount, loadWeight,
                lowerBound, upperBound,
                null, null, null);
    }
    public boolean isRebuild(){
        return x != null;
    }
}
