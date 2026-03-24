package classes;

import javafx.scene.canvas.GraphicsContext;

/**
 * Интерфейс для объектов транспорта, которые готовы к анимации
 * на {@link javafx.scene.canvas.Canvas}.
 * <p>
 * Реализующие классы должны уметь:
 * <ul>
 *     <li>двигаться во времени</li>
 *     <li>останавливаться и запускаться</li>
 *     <li>отрисовываться на канве</li>
 *     <li>сообщать о своём состоянии (остановлен ли объект, вышел ли за границы)</li>
 * </ul>
 */
public interface Animatable {

    /**
     * Запускает движение объекта.
     */
    void start();

    /**
     * Останавливает движение объекта.
     */
    void stop();

    /**
     * Проверяет, находится ли объект в остановленном состоянии.
     *
     * @return {@code true}, если объект остановлен, иначе {@code false}
     */
    boolean isStopped();

    /**
     * Проверяет, вышел ли объект за границы области отрисовки.
     *
     * @return {@code true}, если объект находится вне области видимости
     */
    boolean isOutOfBounds();

    /**
     * Проверяет, виден ли объект на экране.
     * <p>
     * По умолчанию объект считается видимым,
     * если он не вышел за границы области.
     *
     * @return {@code true}, если объект видим
     */
    default boolean isVisible(){
        return !isOutOfBounds();
    }

    /**
     * Обновляет положение объекта.
     *
     * @param deltaSeconds время (в секундах), прошедшее с предыдущего обновления
     */
    void move(double deltaSeconds);

    /**
     * Отрисовывает объект на канве.
     *
     * @param gc графический контекст канвы
     */
    void draw(GraphicsContext gc);

    /**
     * Устанавливает количество людей в объекте (например, пассажиров).
     *
     * @param peopleCNT количество людей
     */
    void setPeopleCNT(int peopleCNT);

    /**
     * Устанавливает вес груза.
     *
     * @param loadWeight вес груза
     */
    void setLoadWeight(int loadWeight);
}