package configs;

import classes.Lamborghini;
import classes.SceneObject;

public record LamborghiniConfig(int peopleCount,
                                int loadWeight) implements Config {
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
}
