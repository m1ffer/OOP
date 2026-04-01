package configs;

import classes.Polo;
import classes.SceneObject;

public record PoloConfig(int peopleCount,
                         int loadWeight) implements Config {
    public PoloConfig{
        SceneObject.validateMax(peopleCount, Polo.MAX_PEOPLE_COUNT,
                "Превышено максимальное количество людей (" + Polo.MAX_PEOPLE_COUNT + ")");
        SceneObject.validatePositive(peopleCount,
                "Количество людей должно быть положительным");
        SceneObject.validateMax(loadWeight, Polo.LOAD_CAPACITY,
                "Превышен максимальный вес груза (" + Polo.LOAD_CAPACITY + ")");
        SceneObject.validateNonNegative(loadWeight,
                "Вес не может быть отрицательным");
    }
}
