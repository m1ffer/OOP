package classes;

import javafx.scene.image.Image;

import java.util.Objects;
import java.util.function.Function;

/**
 * Абстрактный класс транспортного средства.
 * <p>
 * Расширяет SceneObject и добавляет:
 *  - максимальную скорость
 *  - ограничения по пассажирам
 *  - ограничения по грузу
 *  - функцию уменьшения скорости в зависимости от загрузки
 * <p>
 * Движение происходит через масштабирование времени
 * в зависимости от коэффициента скорости.
 */
public abstract class Vehicle extends ImageObject {

    /** Максимальное количество пассажиров. */
    protected final int maxPeopleCNT;

    /** Текущее количество пассажиров. */
    protected int peopleCNT;

    /** Максимальная грузоподъёмность (в кг). */
    protected final int loadCapacity;

    /** Текущий вес груза (в кг). */
    protected int loadWeight;

    protected final Function <Integer, Double> loadReductionFunction;
    protected final Function <Integer, Double> peopleReductionFunction;
    protected double reduction;

    /**
     * Конструктор транспортного средства.
     */
    protected Vehicle(Image image,
                      double startX,
                      double startY,
                      double width,
                      double height,
                      double canvasWidth,
                      double canvasHeight,
                      double imageWidth,
                      double imageHeight,
                      Function<Double, Double> xMotion,
                      Function<Double, Double> yMotion,
                      int maxPeopleCNT,
                      int peopleCNT,
                      int loadCapacity,
                      int loadWeight,
                      Function <Integer, Double> loadReductionFunction,
                      Function <Integer, Double> peopleReductionFunction) {
        super(image, startX, startY, width, height,
                canvasWidth, canvasHeight,
                imageWidth, imageHeight,
                xMotion, yMotion);

        this.peopleReductionFunction = Objects.requireNonNull(
                peopleReductionFunction,
                "");
        this.loadReductionFunction = Objects.requireNonNull(
                loadReductionFunction,
                "");

        this.maxPeopleCNT = validateNonNegative(maxPeopleCNT, "");
        setPeopleCNT(peopleCNT);

        this.loadCapacity = validateNonNegative(loadCapacity, "");
        setLoadWeight(loadWeight);

        this.reduction = produceReduction();
    }

    @Override
    public void move(double deltaSeconds) {
        elapsedTime += deltaSeconds * (1 - getReduction());
        x = xMotion.apply(elapsedTime);
        y = yMotion.apply(elapsedTime);
    }

    private double produceReduction(){
        validateReductionFunctions(peopleReductionFunction, peopleCNT,
                loadReductionFunction, loadWeight);
        return peopleReductionFunction.apply(peopleCNT) +
                loadReductionFunction.apply(loadWeight);
    }

    // =======================
    // ===== СЕТТЕРЫ =========
    // =======================

    public void setPeopleCNT(int peopleCNT) {
        validateNonNegative(peopleCNT, "");
        this.peopleCNT = validateMax(peopleCNT, maxPeopleCNT, "");
        reduction = produceReduction();
    }

    public void setLoadWeight(int loadWeight){
        validateNonNegative(loadWeight, "");
        this.loadWeight = validateMax(loadWeight, loadCapacity, "");
        reduction = produceReduction();
    }

    // =======================
    // ===== ГЕТТЕРЫ =========
    // =======================

    public int getMaxPeopleCNT() {
        return maxPeopleCNT;
    }

    public int getPeopleCNT() {
        return peopleCNT;
    }

    public int getLoadCapacity() {
        return loadCapacity;
    }

    public int getLoadWeight() {
        return loadWeight;
    }

    public double getReduction(){
        return reduction;
    }

    // ==============================
    // ===== ВСПОМОГАТЕЛЬНОЕ ========
    // ==============================

    public static double validateMax(double value, double max, String message){
        if (value > max)
            throw new IllegalArgumentException(message);
        return value;
    }

    public static int validateMax(int value, int max, String message){
        return (int) validateMax((double) value, (double) max, message);
    }
    
    public static double validateReduction(double reduction, String message){
        if (reduction < 0 || reduction > 1)
            throw new IllegalArgumentException(message);
        return reduction;
    }
    
    public static Function<Integer, Double> validateReductionFunction(
            Function <Integer, Double> reductionFunction, int value,
            String message
    ){
        validateReduction(reductionFunction.apply(value), message);
        return reductionFunction;
    }
    
    public static void validateReductionFunctions(
            Function <Integer, Double> peopleReductionFunction, int peopleCNT,
            Function <Integer, Double> loadReductionFunction, int loadWeight
    ){
        validateReductionFunction(peopleReductionFunction, peopleCNT, "");
        validateReductionFunction(loadReductionFunction, loadWeight, "");
        validateReduction(
                peopleReductionFunction.apply(peopleCNT) 
                +
                loadReductionFunction.apply(loadWeight),
                ""
        );
    }
    
}