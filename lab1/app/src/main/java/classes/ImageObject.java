package classes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;
import java.util.function.Function;

/**
 * Класс объекта сцены, который отображается как изображение.
 * <p>
 * Наследует поведение движения и габаритов из SceneObject
 * и реализует отрисовку картинки.
 */
public abstract class ImageObject extends SceneObject {

    /** Загруженное изображение. */
    protected final Image image;

    /**
     * Ширина самой картинки.
     * Может отличаться от width (габаритов объекта),
     * если вокруг изображения предполагается фон.
     */
    protected final double imageWidth;

    /**
     * Высота самой картинки.
     * Может отличаться от height (габаритов объекта).
     */
    protected final double imageHeight;

    /**
     * Создаёт объект изображения.
     *
     * @param image         загруженное изображение (не null)
     * @param startX        начальная координата X
     * @param startY        начальная координата Y
     * @param width         габаритная ширина объекта (> 0)
     * @param height        габаритная высота объекта (> 0)
     * @param canvasWidth   ширина канвы (> 0)
     * @param canvasHeight  высота канвы (> 0)
     * @param imageWidth    ширина картинки (> 0)
     * @param imageHeight   высота картинки (> 0)
     * @param xMotion       функция движения по X
     * @param yMotion       функция движения по Y
     *
     * @throws IllegalArgumentException если любой размер <= 0
     * @throws NullPointerException     если image или функции движения равны null
     */
    public ImageObject(Image image,
                       double startX,
                       double startY,
                       double width,
                       double height,
                       double canvasWidth,
                       double canvasHeight,
                       double imageWidth,
                       double imageHeight,
                       Function<Double, Double> xMotion,
                       Function<Double, Double> yMotion) {

        // Вызов конструктора родителя (там уже проверены width, height,
        // canvasWidth, canvasHeight, xMotion, yMotion)
        super(startX, startY, width, height,
                canvasWidth, canvasHeight,
                xMotion, yMotion);

        // Проверка изображения на null
        this.image = Objects.requireNonNull(image,
                "Изображение не может быть null");

        // Проверка размеров картинки через публичный метод validatePositive
        this.imageWidth = validatePositive(imageWidth,
                "Ширина изображения должна быть > 0");
        this.imageHeight = validatePositive(imageHeight,
                "Высота изображения должна быть > 0");
    }

    /**
     * Отрисовывает изображение на канве.
     * <p>
     * Картинка рисуется в точке (x, y)
     * с размерами imageWidth и imageHeight.
     *
     * @param gc графический контекст Canvas
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y, imageWidth, imageHeight);
    }
}