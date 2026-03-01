package org.example.app;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Класс для отрисовки PNG-изображения на JavaFX Canvas.
 * Поля:
 * - imagePath – путь к файлу изображения;
 * - width, height – ширина и высота, с которыми изображение будет нарисовано.
 */
public class PngDrawer {
    private final String imagePath;
    private final double width;
    private final double height;

    /**
     * Конструктор.
     * @param imagePath путь к PNG-файлу (локальный файл или URL)
     * @param width желаемая ширина изображения на канве
     * @param height желаемая высота изображения на канве
     */
    public PngDrawer(String imagePath, double width, double height) {
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
    }

    /**
     * Рисует изображение на переданной канве.
     * @param canvas целевая канва (javafx.scene.canvas.Canvas)
     */
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Формируем корректный URL для загрузки
        String url = imagePath.startsWith("file:") || imagePath.startsWith("http:") || imagePath.startsWith("https:")
                ? imagePath
                : "file:" + imagePath;

        // Загружаем изображение синхронно (backgroundLoading = false)
        Image image = new Image(url, width, height, false, false, false);

        // Проверяем, загрузилось ли изображение без ошибок
        if (!image.isError()) {
            // Рисуем изображение в верхнем левом углу (0,0) с заданными размерами
            gc.drawImage(image, 0, 0, width, height);
        } else {
            // При ошибке загрузки заполняем область красным цветом (можно изменить логику)
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, width, height);
            System.err.println("Не удалось загрузить изображение: " + imagePath);
        }
    }

    /**
     * Очищает всю область канвы белым цветом.
     * @param canvas целевая канва
     */
    public void clear(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
