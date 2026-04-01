package classes;

import java.util.Objects;

/**
 * Абстрактный класс наземного транспортного средства.
 *
 * <p>Расширяет {@link Vehicle} и добавляет:</p>
 * <ul>
 *     <li>Модель движения по земле</li>
 *     <li>Состояния (движение, ускорение, остановка)</li>
 *     <li>Плавный разгон и торможение</li>
 *     <li>Фиксацию по уровню земли</li>
 * </ul>
 *
 * <p>По оси Y объект жёстко привязан к уровню земли.
 * Движение происходит только по оси X.</p>
 */
abstract public class GroundVehicle extends Vehicle {

    /**
     * Возможные состояния наземного транспорта.
     */
    protected enum GroundState {

        /** Движение с постоянной скоростью. */
        MOVING_CONSTANT,

        /** Разгон или торможение (есть ускорение). */
        ACCELERATING,

        /** Полная остановка. */
        STOPPED
    }

    /**
     * Уровень земли (координата Y).
     *
     * <p>Объект считается стоящим на земле,
     * если выполняется условие:
     * {@code y + height == groundLevel}</p>
     */
    protected final double groundLevel;

    /** Текущее состояние движения. */
    protected GroundState state;

    /**
     * Ускорение разгона (должно быть положительным).
     * Определяет, насколько быстро транспорт набирает скорость.
     */
    protected final double startAcceleration;

    /**
     * Ускорение торможения (должно быть отрицательным).
     * Определяет, насколько быстро транспорт замедляется.
     */
    protected final double stopAcceleration;

    /**
     * Текущий множитель скорости в диапазоне [0,1].
     *
     * <p>0 — полная остановка<br>
     * 1 — движение с максимальной скоростью</p>
     */
    protected double currentSpeedFactor;

    /**
     * Текущее ускорение (может быть положительным или отрицательным).
     */
    protected double acceleration;

    /**
     * Создаёт наземное транспортное средство.
     *
     * <p>Конструктор:</p>
     * <ul>
     *     <li>Инициализирует базовую транспортную логику</li>
     *     <li>Сохраняет уровень земли</li>
     *     <li>Проверяет, что объект "стоит колёсами на земле"</li>
     *     <li>Инициализирует состояние движения</li>
     * </ul>
     *
     * @param imageConf      конфигурация отображения (не null)
     * @param motionConf     конфигурация движения (не null)
     * @param loadConf       конфигурация загрузки (не null)
     * @param reductionConf  конфигурация уменьшения скорости (не null)
     * @param groundConf     конфигурация параметров наземного движения (не null)
     *
     * @throws NullPointerException     если groundConf равен null
     * @throws IllegalArgumentException если объект не стоит на земле
     */
    protected GroundVehicle(
            ImageConf imageConf,
            MotionConf motionConf,
            LoadConf loadConf,
            ReductionConf reductionConf,
            GroundConf groundConf
    ) {
        super(
                imageConf,
                motionConf,
                loadConf,
                reductionConf
        );

        // Проверка конфигурации
        Objects.requireNonNull(groundConf,
                "Конфигурация наземного движения не должна быть null.");

        // Установка ускорений
        this.startAcceleration = groundConf.startAcceleration();
        this.stopAcceleration = groundConf.stopAcceleration();

        // Установка уровня земли
        this.groundLevel = groundConf.groundLevel();

        // Проверка, что высота объекта не превышает уровень земли
        validateNonNegative(
                groundLevel - height,
                "Высота объекта превышает допустимый уровень земли."
        );

        // Проверка, что объект действительно стоит на земле
        if (Math.abs(y + height - groundLevel) > VERY_SMALL_NUMBER) {
            throw new IllegalArgumentException(
                    "Объект должен быть расположен колёсами на уровне земли."
            );
        }

        startY = groundLevel - height;

        // Начальное состояние — движение с полной скоростью
        currentSpeedFactor = 1;
        state = GroundState.MOVING_CONSTANT;
        System.out.println(startX);
    }

    /**
     * Обновляет положение наземного транспортного средства.
     *
     * <p>Алгоритм:</p>
     * <ul>
     *     <li>Если объект остановлен — движение не выполняется</li>
     *     <li>Если выполняется разгон или торможение —
     *         обновляется множитель скорости</li>
     *     <li>Координата X пересчитывается с учётом текущей скорости</li>
     *     <li>Координата Y жёстко фиксируется на уровне земли</li>
     * </ul>
     *
     * @param deltaSeconds время, прошедшее с предыдущего обновления (в секундах)
     */
    @Override
    public void move(double deltaSeconds) {

        switch (state) {

            case STOPPED:
                // Полная остановка — ничего не делаем
                break;

            case ACCELERATING:
                // Изменяем множитель скорости
                currentSpeedFactor += acceleration * deltaSeconds;

                // Ограничение диапазона [0,1]
                if (currentSpeedFactor <= 0) {
                    currentSpeedFactor = 0;
                    state = GroundState.STOPPED;
                    acceleration = 0;
                }

                if (currentSpeedFactor >= 1) {
                    currentSpeedFactor = 1;
                    state = GroundState.MOVING_CONSTANT;
                    acceleration = 0;
                }

                // Намеренный переход к MOVING_CONSTANT

            case MOVING_CONSTANT:

                // Итоговая скорость с учётом загрузки и текущего множителя
                double effectiveSpeed =
                        (1 - getReduction()) * currentSpeedFactor;

                // Масштабируем внутреннее время
                elapsedTime += deltaSeconds * effectiveSpeed;

                // Пересчёт координаты X
                x = startX + xMotion.apply(elapsedTime);
        }

        // Жёстко фиксируем объект на земле
        y = startY;
    }

    /**
     * Начать торможение.
     */
    @Override
    public void stop() {
        if (state != GroundState.STOPPED) {
            acceleration = stopAcceleration;
            state = GroundState.ACCELERATING;
        }
    }

    /**
     * Начать разгон.
     */
    @Override
    public void start() {
        if (state != GroundState.MOVING_CONSTANT) {
            acceleration = startAcceleration;
            state = GroundState.ACCELERATING;
        }
    }

    @Override
    public boolean isStopped(){
        return state == GroundState.STOPPED;
    }

    // =======================
    // ===== КОНФИГ  =========
    // =======================

    /**
     * Конфигурация наземного движения.
     *
     * @param stopAcceleration  ускорение торможения (должно быть ≤ 0)
     * @param startAcceleration ускорение разгона (должно быть > 0)
     * @param groundLevel       уровень земли (должен быть > 0)
     */
    public record GroundConf(
            double stopAcceleration,
            double startAcceleration,
            double groundLevel
    ) {
        public GroundConf {

            validatePositive(
                    startAcceleration,
                    "Ускорение разгона должно быть положительным."
            );

            validateMaxEquals(
                    stopAcceleration,
                    0.0,
                    "Ускорение торможения должно быть отрицательным или равно 0."
            );

            validatePositive(
                    groundLevel,
                    "Уровень земли должен быть положительным."
            );
        }
    }




}