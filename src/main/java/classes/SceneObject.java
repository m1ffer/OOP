package classes;

import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;
import java.util.function.Function;

/**
 * Базовый абстрактный класс для любого объекта,
 * который может отображаться на сцене.
 * <p>
 * Объект:
 *  - хранит текущую позицию
 *  - имеет габариты
 *  - перемещается по заданным функциям движения
 *  - умеет отрисовываться (реализуется в наследниках)
 */
public abstract class SceneObject {

    protected final double startX;
    protected double startY;

    /** Текущая координата по оси X (левый верхний угол объекта). */
    protected double x;

    /** Текущая координата по оси Y (левый верхний угол объекта). */
    protected double y;

    /** Ширина объекта (габаритный прямоугольник). */
    protected final double width;

    /** Высота объекта (габаритный прямоугольник). */
    protected final double height;

    /** Время "жизни" объекта в секундах с момента создания. */
    protected double elapsedTime = 0.0;

    /** Ширина канвы. Должна быть > 0. */
    protected double canvasWidth;

    /** Высота канвы. Должна быть > 0. */
    protected double canvasHeight;

    /**
     * Функция движения по оси X.
     * Принимает время (в секундах) и возвращает координату X.
     */
    protected final Function<Double, Double> xMotion;

    /**
     * Функция движения по оси Y.
     * Принимает время (в секундах) и возвращает координату Y.
     */
    protected final Function<Double, Double> yMotion;

    /**
     * Создаёт новый объект сцены.
     *
     * @param startX       начальная координата X
     * @param startY       начальная координата Y
     * @param width        ширина объекта (должна быть > 0)
     * @param height       высота объекта (должна быть > 0)
     * @param canvasWidth  ширина канвы (должна быть > 0)
     * @param canvasHeight высота канвы (должна быть > 0)
     * @param xMotion      функция движения по X
     * @param yMotion      функция движения по Y
     *
     * @throws IllegalArgumentException если размеры <= 0
     * @throws NullPointerException     если функции движения равны null
     */
    protected SceneObject(double startX,
                          double startY,
                          double width,
                          double height,
                          double canvasWidth,
                          double canvasHeight,
                          Function<Double, Double> xMotion,
                          Function<Double, Double> yMotion) {

        // Инициализация стартового положения
        this.startX = startX;
        this.startY = startY;
        this.x = startX;
        this.y = startY;

        // Установка габаритов с проверкой положительности
        this.width = validatePositive(width, "Ширина объекта должна быть > 0");
        this.height = validatePositive(height, "Высота объекта должна быть > 0");

        // Установка размеров канвы (с проверкой)
        setCanvasSize(canvasWidth, canvasHeight);

        // Проверка, что функции движения заданы (сообщение на русском)
        this.xMotion = Objects.requireNonNull(xMotion,
                "Функция движения по X не может быть null");
        this.yMotion = Objects.requireNonNull(yMotion,
                "Функция движения по Y не может быть null");
    }

    /**
     * Обновляет положение объекта на основе прошедшего времени.
     *
     * @param deltaSeconds количество секунд,
     *                     прошедших с предыдущего обновления
     */
    public void move(double deltaSeconds) {

        // Увеличиваем время существования объекта
        elapsedTime += deltaSeconds;

        // Вычисляем новые координаты
        x = startX + xMotion.apply(elapsedTime);
        y = startY + yMotion.apply(elapsedTime);
    }

    /**
     * Выполняет отрисовку объекта.
     *
     * @param gc графический контекст канвы
     */
    public abstract void draw(GraphicsContext gc);

    /**
     * Проверяет, вышел ли объект полностью за пределы канвы.
     *
     * @return true, если объект полностью вне видимой области
     */
    public boolean isOutOfBounds() {
        return x > canvasWidth ||
                x + width < 0 ||
                y > canvasHeight ||
                y + height < 0;
    }

    // =======================
    // ====== СЕТТЕРЫ ========
    // =======================

    /**
     * Устанавливает ширину канвы.
     *
     * @param canvasWidth ширина (> 0)
     * @throws IllegalArgumentException если значение <= 0
     */
    public void setCanvasWidth(double canvasWidth) {
        this.canvasWidth = validatePositive(canvasWidth, "Ширина канвы должна быть > 0");
    }

    /**
     * Устанавливает высоту канвы.
     *
     * @param canvasHeight высота (> 0)
     * @throws IllegalArgumentException если значение <= 0
     */
    public void setCanvasHeight(double canvasHeight) {
        this.canvasHeight = validatePositive(canvasHeight, "Высота канвы должна быть > 0");
    }

    /**
     * Устанавливает размеры канвы.
     *
     * @param canvasWidth  ширина (> 0)
     * @param canvasHeight высота (> 0)
     */
    public void setCanvasSize(double canvasWidth, double canvasHeight) {
        setCanvasWidth(canvasWidth);
        setCanvasHeight(canvasHeight);
    }

    // =======================
    // ====== ГЕТТЕРЫ ========
    // =======================

    /**
     * Возвращает ширину объекта.
     *
     * @return ширина объекта
     */
    public double getWidth() {
        return width;
    }

    /**
     * Возвращает высоту объекта.
     *
     * @return высота объекта
     */
    public double getHeight() {
        return height;
    }

    // ==============================
    // ===== ВСПОМОГАТЕЛЬНОЕ ========
    // ==============================

    /**
     * Проверяет, что значение не меньше заданного минимума.
     *
     * @param value   проверяемое значение
     * @param min     минимально допустимое значение (включительно)
     * @param message сообщение об ошибке, если проверка не пройдена
     * @param <T>     тип значения, должен реализовывать {@link Comparable}
     * @return переданное значение, если оно ≥ min
     * @throws IllegalArgumentException если value < min
     */
    public static <T extends Comparable<T>> T validateMin(T value, T min, String message){
        if (value.compareTo(min) < 0)
            throw new IllegalArgumentException(message);
        return value;
    }

    /**
     * Проверяет, что значение строго больше заданного минимума (равенство не допускается).
     *
     * @param value   проверяемое значение
     * @param min     минимально допустимое значение (исключительно)
     * @param message сообщение об ошибке, если проверка не пройдена
     * @param <T>     тип значения, должен реализовывать {@link Comparable}
     * @return переданное значение, если оно > min
     * @throws IllegalArgumentException если value ≤ min
     */
    public static <T extends Comparable<T>> T validateMinEquals(T value, T min, String message){
        if (value.compareTo(min) < 0 || value.compareTo(min) == 0)
            throw new IllegalArgumentException(message);
        return value;
    }

    /**
     * Проверяет, что значение не больше заданного максимума.
     *
     * @param value   проверяемое значение
     * @param max     максимально допустимое значение (включительно)
     * @param message сообщение об ошибке, если проверка не пройдена
     * @param <T>     тип значения, должен реализовывать {@link Comparable}
     * @return переданное значение, если оно ≤ max
     * @throws IllegalArgumentException если value > max
     */
    public static <T extends Comparable<T>> T validateMax(T value, T max, String message){
        if (value.compareTo(max) > 0)
            throw new IllegalArgumentException(message);
        return value;
    }

    /**
     * Проверяет, что значение строго меньше заданного максимума (равенство не допускается).
     *
     * @param value   проверяемое значение
     * @param max     максимально допустимое значение (исключительно)
     * @param message сообщение об ошибке, если проверка не пройдена
     * @param <T>     тип значения, должен реализовывать {@link Comparable}
     * @return переданное значение, если оно < max
     * @throws IllegalArgumentException если value ≥ max
     */
    public static <T extends Comparable<T>> T validateMaxEquals(T value, T max, String message){
        if (value.compareTo(max) > 0 || value.compareTo(max) == 0)
            throw new IllegalArgumentException(message);
        return value;
    }

    /**
     * Проверяет, что число с плавающей точкой строго положительное (> 0).
     *
     * @param value   проверяемое значение
     * @param message сообщение об ошибке, если проверка не пройдена
     * @return value, если оно > 0
     * @throws IllegalArgumentException если value ≤ 0
     */
    public static double validatePositive(double value, String message){
        return validateMin(value, 0.0, message);
    }

    /**
     * Проверяет, что целое число строго положительное (> 0).
     *
     * @param value   проверяемое значение
     * @param message сообщение об ошибке, если проверка не пройдена
     * @return value, если оно > 0
     * @throws IllegalArgumentException если value ≤ 0
     */
    public static int validatePositive(int value, String message){
        return (int) validatePositive((double) value, message);
    }

    /**
     * Проверяет, что число с плавающей точкой неотрицательное (≥ 0).
     *
     * @param value   проверяемое значение
     * @param message сообщение об ошибке, если проверка не пройдена
     * @return value, если оно ≥ 0
     * @throws IllegalArgumentException если value < 0
     */
    public static double validateNonNegative(double value, String message){
        return validateMin(value, 0.0, message);
    }

    /**
     * Проверяет, что целое число неотрицательное (≥ 0).
     *
     * @param value   проверяемое значение
     * @param message сообщение об ошибке, если проверка не пройдена
     * @return value, если оно ≥ 0
     * @throws IllegalArgumentException если value < 0
     */
    public static int validateNonNegative(int value, String message){
        return (int) validateNonNegative((double) value, message);
    }
}