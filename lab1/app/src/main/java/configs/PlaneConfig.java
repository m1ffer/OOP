package configs;

import classes.Plane;
import classes.SceneObject;

public record PlaneConfig(int peopleCount,
                          int loadWeight,
                          double startY) implements Config {
    public PlaneConfig{
        SceneObject.validateMax(peopleCount, Plane.MAX_PEOPLE_COUNT,
                "Превышено максимальное количество людей (" + Plane.MAX_PEOPLE_COUNT + ")");
        SceneObject.validatePositive(peopleCount,
                "Количество людей должно быть положительным");
        SceneObject.validateMax(loadWeight, Plane.LOAD_CAPACITY,
                "Превышен максимальный вес груза (" + Plane.LOAD_CAPACITY + ")");
        SceneObject.validateNonNegative(loadWeight,
                "Вес не может быть отрицательным");
        SceneObject.validatePositive(startY - Plane.HEIGHT,
                "Самолет не может лететь так высоко");
    }
}
