package classes;

import javafx.scene.image.Image;

import java.util.Objects;
import java.util.function.Function;

/**
 * Абстрактный класс транспортного средства.
 *
 * <p>Расширяет {@link ImageObject} и добавляет модель транспортной логики:
 * <ul>
 *     <li>Ограничение по количеству пассажиров</li>
 *     <li>Ограничение по грузоподъёмности</li>
 *     <li>Функции уменьшения скорости в зависимости от загрузки</li>
 * </ul>
 *
 * <p>Фактическое изменение скорости реализуется через масштабирование
 * внутреннего времени жизни объекта (elapsedTime).</p>
 */
public abstract class Vehicle extends ImageObject {

    /** Максимально допустимое количество пассажиров. */
    protected final int maxPeopleCNT;

    /** Текущее количество пассажиров в транспорте. */
    protected int peopleCNT;

    /** Максимальная грузоподъёмность (в килограммах). */
    protected final int loadCapacity;

    /** Текущий вес груза (в килограммах). */
    protected int loadWeight;

    /**
     * Функция уменьшения скорости в зависимости от веса груза.
     * Возвращает коэффициент в диапазоне [0,1].
     */
    protected final Function<Integer, Double> loadReductionFunction;

    /**
     * Функция уменьшения скорости в зависимости от количества пассажиров.
     * Возвращает коэффициент в диапазоне [0,1].
     */
    protected final Function<Integer, Double> peopleReductionFunction;

    /** Суммарный коэффициент уменьшения скорости (должен быть в [0,1]). */
    protected double reduction;

    /**
     * Конструктор транспортного средства.
     *
     * @param image                     изображение транспорта (не null)
     * @param startX                    начальная координата X
     * @param startY                    начальная координата Y
     * @param width                     габаритная ширина объекта (> 0)
     * @param height                    габаритная высота объекта (> 0)
     * @param canvasWidth               ширина канвы (> 0)
     * @param canvasHeight              высота канвы (> 0)
     * @param imageWidth                ширина изображения (> 0)
     * @param imageHeight               высота изображения (> 0)
     * @param xMotion                   функция движения по оси X (не null)
     * @param yMotion                   функция движения по оси Y (не null)
     * @param maxPeopleCNT              максимальное количество пассажиров (≥ 0)
     * @param peopleCNT                 текущее количество пассажиров (≥ 0, ≤ maxPeopleCNT)
     * @param loadCapacity              максимальная грузоподъёмность (≥ 0)
     * @param loadWeight                текущий вес груза (≥ 0, ≤ loadCapacity)
     * @param loadReductionFunction     функция уменьшения скорости от груза (не null)
     * @param peopleReductionFunction   функция уменьшения скорости от пассажиров (не null)
     *
     * @throws NullPointerException     если любая из функций или изображение равны null
     * @throws IllegalArgumentException если нарушены ограничения на значения
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
                      Function<Integer, Double> loadReductionFunction,
                      Function<Integer, Double> peopleReductionFunction) {

        // Инициализация базовой логики движения и отрисовки
        super(image, startX, startY, width, height,
                canvasWidth, canvasHeight,
                imageWidth, imageHeight,
                xMotion, yMotion);

        // Проверка функций уменьшения скорости
        this.peopleReductionFunction = Objects.requireNonNull(
                peopleReductionFunction,
                "Функция уменьшения скорости от пассажиров не может быть null");

        this.loadReductionFunction = Objects.requireNonNull(
                loadReductionFunction,
                "Функция уменьшения скорости от груза не может быть null");

        // Установка ограничений (сообщения на русском)
        this.maxPeopleCNT = validateNonNegative(maxPeopleCNT,
                "Максимальное количество пассажиров должно быть >= 0");

        setPeopleCNT(peopleCNT); // внутри проверка на неотрицательность и максимум

        this.loadCapacity = validateNonNegative(loadCapacity,
                "Грузоподъёмность должна быть >= 0");

        setLoadWeight(loadWeight); // внутри проверка на неотрицательность и максимум

        // Вычисление начального коэффициента уменьшения скорости
        this.reduction = produceReduction();
    }

    /**
     * Переопределённый метод движения.
     *
     * <p>Скорость изменяется через масштабирование времени:
     * чем больше reduction, тем медленнее "течёт время"
     * для данного объекта.</p>
     *
     * @param deltaSeconds количество секунд, прошедших с предыдущего кадра
     */
    @Override
    public void move(double deltaSeconds) {

        // Увеличиваем время жизни с учётом коэффициента скорости
        elapsedTime += deltaSeconds * (1 - getReduction());

        // Пересчитываем координаты через функции движения
        x = xMotion.apply(elapsedTime);
        y = yMotion.apply(elapsedTime);
    }

    /**
     * Вычисляет суммарный коэффициент уменьшения скорости.
     *
     * @return значение в диапазоне [0,1]
     * @throws IllegalArgumentException если результат вне допустимого диапазона
     */
    private double produceReduction() {

        // Проверяем корректность функций и результата
        validateReductionFunctions(
                peopleReductionFunction, peopleCNT,
                loadReductionFunction, loadWeight
        );

        return peopleReductionFunction.apply(peopleCNT)
                + loadReductionFunction.apply(loadWeight);
    }

    // =======================
    // ===== СЕТТЕРЫ =========
    // =======================

    /**
     * Устанавливает количество пассажиров.
     *
     * @param peopleCNT новое количество пассажиров (≥ 0, ≤ maxPeopleCNT)
     * @throws IllegalArgumentException если значение отрицательное или превышает максимум
     */
    public void setPeopleCNT(int peopleCNT) {
        validateNonNegative(peopleCNT, "Количество пассажиров должно быть >= 0");
        this.peopleCNT = validateMax(
                peopleCNT, maxPeopleCNT,
                "Количество пассажиров превышает максимальное"
        );
        reduction = produceReduction();
    }

    /**
     * Устанавливает вес груза.
     *
     * @param loadWeight новый вес груза (≥ 0, ≤ loadCapacity)
     * @throws IllegalArgumentException если значение отрицательное или превышает грузоподъёмность
     */
    public void setLoadWeight(int loadWeight) {
        validateNonNegative(loadWeight, "Вес груза должен быть >= 0");
        this.loadWeight = validateMax(
                loadWeight, loadCapacity,
                "Вес груза превышает грузоподъёмность"
        );
        reduction = produceReduction();
    }

    // =======================
    // ===== ГЕТТЕРЫ =========
    // =======================

    /** @return максимальное количество пассажиров */
    public int getMaxPeopleCNT() {
        return maxPeopleCNT;
    }

    /** @return текущее количество пассажиров */
    public int getPeopleCNT() {
        return peopleCNT;
    }

    /** @return максимальная грузоподъёмность */
    public int getLoadCapacity() {
        return loadCapacity;
    }

    /** @return текущий вес груза */
    public int getLoadWeight() {
        return loadWeight;
    }

    /** @return текущий коэффициент уменьшения скорости (всегда в [0,1]) */
    public double getReduction() {
        return reduction;
    }

    // ==============================
    // ===== ВСПОМОГАТЕЛЬНОЕ ========
    // ==============================

    /**
     * Проверяет, что коэффициент уменьшения скорости находится в диапазоне [0,1].
     *
     * @param reduction проверяемый коэффициент
     * @param message   сообщение об ошибке
     * @return reduction, если он в допустимых пределах
     * @throws IllegalArgumentException если reduction ∉ [0,1]
     */
    public static double validateReduction(double reduction, String message) {
        if (reduction < 0 || reduction > 1)
            throw new IllegalArgumentException(message);
        return reduction;
    }

    /**
     * Проверяет, что функция уменьшения скорости возвращает допустимый коэффициент
     * для заданного аргумента.
     *
     * @param reductionFunction проверяемая функция
     * @param value             аргумент (количество пассажиров или вес груза)
     * @param message           сообщение об ошибке
     * @return переданная функция (для цепочек вызовов)
     * @throws IllegalArgumentException если результат функции вне [0,1]
     */
    public static Function<Integer, Double> validateReductionFunction(
            Function<Integer, Double> reductionFunction,
            int value,
            String message) {

        validateReduction(reductionFunction.apply(value), message);
        return reductionFunction;
    }

    /**
     * Проверяет обе функции уменьшения скорости и их суммарный результат.
     *
     * @param peopleReductionFunction функция от пассажиров
     * @param peopleCNT               текущее количество пассажиров
     * @param loadReductionFunction   функция от груза
     * @param loadWeight              текущий вес груза
     * @throws IllegalArgumentException если хотя бы одна из функций возвращает
     *                                  некорректное значение или сумма выходит за [0,1]
     */
    public static void validateReductionFunctions(
            Function<Integer, Double> peopleReductionFunction,
            int peopleCNT,
            Function<Integer, Double> loadReductionFunction,
            int loadWeight) {

        validateReductionFunction(
                peopleReductionFunction, peopleCNT,
                "Коэффициент уменьшения от пассажиров должен быть в [0,1]");

        validateReductionFunction(
                loadReductionFunction, loadWeight,
                "Коэффициент уменьшения от груза должен быть в [0,1]");

        validateReduction(
                peopleReductionFunction.apply(peopleCNT)
                        + loadReductionFunction.apply(loadWeight),
                "Суммарный коэффициент уменьшения должен быть в [0,1]"
        );
    }

    // =======================
    // ===== КОНФИГИ =========
    // =======================

    /**
     * Конфигурация отображения объекта на сцене.
     *
     * <p>Содержит параметры, необходимые для:
     * <ul>
     *     <li>позиционирования объекта</li>
     *     <li>задания габаритов</li>
     *     <li>задания размеров изображения</li>
     *     <li>передачи размеров канвы</li>
     * </ul>
     *
     * <p>Все размеры должны быть строго положительными.</p>
     */
    public record ImageConf(Image image,
                            double startX,
                            double startY,
                            double width,
                            double height,
                            double canvasWidth,
                            double canvasHeight,
                            double imageWidth,
                            double imageHeight) {

        /**
         * Canonical-конструктор с валидацией параметров.
         */
        public ImageConf {

            // Проверка изображения
            Objects.requireNonNull(image,
                    "Изображение не должно быть null.");

            // Проверка габаритов объекта
            validatePositive(width,
                    "Ширина объекта должна быть больше 0.");
            validatePositive(height,
                    "Высота объекта должна быть больше 0.");

            // Проверка размеров канвы
            validatePositive(canvasWidth,
                    "Ширина канвы должна быть больше 0.");
            validatePositive(canvasHeight,
                    "Высота канвы должна быть больше 0.");

            // Проверка размеров изображения
            validatePositive(imageWidth,
                    "Ширина изображения должна быть больше 0.");
            validatePositive(imageHeight,
                    "Высота изображения должна быть больше 0.");
        }
    }

    /**
     * Конфигурация функций движения объекта.
     *
     * <p>Каждая функция принимает время жизни объекта (в секундах)
     * и возвращает координату соответствующей оси.</p>
     */
    public record MotionConf(Function<Double, Double> xMotion,
                             Function<Double, Double> yMotion) {

        /**
         * Canonical-конструктор с проверкой функций.
         */
        public MotionConf {

            // Проверка функции движения по X
            Objects.requireNonNull(xMotion,
                    "Функция движения по оси X не должна быть null.");

            // Проверка функции движения по Y
            Objects.requireNonNull(yMotion,
                    "Функция движения по оси Y не должна быть null.");
        }
    }

    /**
     * Конфигурация загрузки транспортного средства.
     *
     * <p>Содержит ограничения и текущие значения:
     * <ul>
     *     <li>Количество пассажиров</li>
     *     <li>Вес груза</li>
     * </ul>
     *
     * <p>Значения не могут быть отрицательными,
     * а текущие значения не должны превышать максимальные.</p>
     */
    public record LoadConf(int maxPeopleCNT,
                           int peopleCNT,
                           int loadCapacity,
                           int loadWeight) {

        /**
         * Canonical-конструктор с полной валидацией.
         */
        public LoadConf {

            // Проверка ограничений по пассажирам
            validateNonNegative(maxPeopleCNT,
                    "Максимальное количество пассажиров не может быть отрицательным.");

            validateNonNegative(peopleCNT,
                    "Количество пассажиров не может быть отрицательным.");

            validateMax(peopleCNT, maxPeopleCNT,
                    "Количество пассажиров превышает допустимый максимум.");

            // Проверка ограничений по грузу
            validateNonNegative(loadCapacity,
                    "Грузоподъёмность не может быть отрицательной.");

            validateNonNegative(loadWeight,
                    "Вес груза не может быть отрицательным.");

            validateMax(loadWeight, loadCapacity,
                    "Вес груза превышает допустимую грузоподъёмность.");
        }
    }

    /**
     * Конфигурация функций уменьшения скорости транспортного средства.
     *
     * <p>Каждая функция принимает:
     * <ul>
     *     <li>Количество пассажиров</li>
     *     <li>Вес груза</li>
     * </ul>
     * И возвращает коэффициент уменьшения скорости в диапазоне 0..1.</p>
     */
    public record ReductionConf(Function<Integer, Double> loadReductionFunction,
                                Function<Integer, Double> peopleReductionFunction) {

        /**
         * Canonical-конструктор с проверкой функций.
         */
        public ReductionConf {

            // Проверка функции уменьшения скорости от груза
            Objects.requireNonNull(loadReductionFunction,
                    "Функция уменьшения скорости от груза не должна быть null.");

            // Проверка функции уменьшения скорости от пассажиров
            Objects.requireNonNull(peopleReductionFunction,
                    "Функция уменьшения скорости от пассажиров не должна быть null.");
        }
    }
}