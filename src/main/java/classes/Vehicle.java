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
     * Создаёт транспортное средство на основе конфигурационных объектов.
     *
     * <p>Все параметры, связанные с отображением, движением, загрузкой
     * и уменьшением скорости, передаются через соответствующие record-конфигурации.
     * Валидация значений выполняется внутри этих конфигураций.</p>
     *
     * <p>Конструктор:
     * <ul>
     *     <li>Инициализирует базовый класс {@link ImageObject}</li>
     *     <li>Устанавливает ограничения по пассажирам и грузу</li>
     *     <li>Сохраняет функции уменьшения скорости</li>
     *     <li>Вычисляет начальный коэффициент уменьшения скорости</li>
     * </ul>
     * </p>
     *
     * @param imageConf      конфигурация отображения объекта (не null)
     * @param motionConf     конфигурация функций движения (не null)
     * @param loadConf       конфигурация ограничений по пассажирам и грузу (не null)
     * @param reductionConf  конфигурация функций уменьшения скорости (не null)
     *
     * @throws NullPointerException если любой из переданных конфигурационных объектов равен null
     */
    protected Vehicle(ImageConf imageConf,
                      MotionConf motionConf,
                      LoadConf loadConf,
                      ReductionConf reductionConf) {

        super(
                Objects.requireNonNull(imageConf,
                        "Конфигурация изображения не должна быть null.").image(),
                imageConf.startX(),
                imageConf.startY(),
                imageConf.width(),
                imageConf.height(),
                imageConf.canvasWidth(),
                imageConf.canvasHeight(),
                imageConf.imageWidth(),
                imageConf.imageHeight(),
                Objects.requireNonNull(motionConf,
                        "Конфигурация движения не должна быть null.").xMotion(),
                motionConf.yMotion()
        );

        this.peopleReductionFunction =
                Objects.requireNonNull(reductionConf,
                                "Конфигурация уменьшения скорости не должна быть null.")
                        .peopleReductionFunction();

        this.loadReductionFunction =
                reductionConf.loadReductionFunction();

        // Ограничения по пассажирам
        this.maxPeopleCNT = Objects.requireNonNull(loadConf,
                "Конфигурация загрузки не должна быть null.").maxPeopleCNT();
        var peopleCNT = validateNonNegative(loadConf.peopleCNT(), "Количество пассажиров должно быть >= 0");
        this.peopleCNT = validateMax(
                peopleCNT, maxPeopleCNT,
                "Количество пассажиров превышает максимальное"
        );

        // Ограничения по грузу
        this.loadCapacity = loadConf.loadCapacity();
        var loadWeight = validateNonNegative(loadConf.loadWeight(), "Вес груза должен быть >= 0");
        this.loadWeight = validateMax(
                loadWeight, loadCapacity,
                "Вес груза превышает грузоподъёмность"
        );

        reduction = produceReduction();
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
        x = startX + xMotion.apply(elapsedTime);
        y = startY + yMotion.apply(elapsedTime);
    }

    public void stop(){
        throw new UnsupportedOperationException("У этого транспорта нет возможности остановиться");
    }

    public void start(){
        throw new UnsupportedOperationException("У этого транспорта нет возможности начать движение после остановки");
    }

    public boolean isStopped(){
        return false;
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
        if (!isStopped())
            throw new UnsupportedOperationException("Транспорт должен остановиться");
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
        if (!isStopped())
            throw new UnsupportedOperationException("Транспорт должен остановиться");
        this.loadWeight = validateMax(
                loadWeight, loadCapacity,
                "Вес груза превышает грузоподъёмность"
        );
        reduction = produceReduction();
    }

    // =======================
    // ===== ГЕТТЕРЫ =========
    // =======================

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

    // ==============================
    // ===== ВСПОМОГАТЕЛЬНОЕ ========
    // ==============================

    /** Допустимая погрешность сравнения вещественных чисел. */
    protected static final double VERY_SMALL_NUMBER = 0.01;
}