package classes;

import configs.Config;
import configs.LamborghiniConfig;
import configs.PoloConfig;
import javafx.scene.image.Image;

import java.util.function.Function;

public class Lamborghini extends GroundVehicle implements Animatable<LamborghiniConfig>{
    private static final Image LAMBORGHINI_IMAGE = new Image(
            Lamborghini.class
                    .getResource("/images/lamborghini.png")
                    .toExternalForm()
    );;
    private static final double IMAGE_HEIGHT = 150;
    private static final double IMAGE_WIDTH = IMAGE_HEIGHT * 3.7547126437;
    private static final double WIDTH = IMAGE_WIDTH;
    private static final double HEIGHT = IMAGE_HEIGHT;
    public static final int MAX_PEOPLE_COUNT = 2;
    public static final int LOAD_CAPACITY = 500;
    private static final Function<Integer, Double> LOAD_REDUCTION_FUNCTION = loadWeight -> loadWeight / 4000.0;
    private static final Function<Integer, Double> PEOPLE_REDUCTION_FUNCTION = peopleCount -> LOAD_REDUCTION_FUNCTION.apply(peopleCount * 80);
    private static final double STOP_ACCELERATION = -0.9;
    private static final double START_ACCELERATION = 0.9;

    public Lamborghini(double canvasWidth,
                double canvasHeight,
                double groundLevel,
                int peopleCount,
                int loadWeight){
        super(
                new ImageConf(
                        LAMBORGHINI_IMAGE,
                        canvasWidth + VERY_SMALL_NUMBER,
                        groundLevel - HEIGHT,
                        WIDTH,
                        HEIGHT,
                        canvasWidth,
                        canvasHeight,
                        IMAGE_WIDTH,
                        IMAGE_HEIGHT
                ),
                new MotionConf(
                        elapsedTime -> - 250 * elapsedTime,
                        VehicleUtil.GROUND_VEHICLE_Y_MOTION
                ),
                new LoadConf(
                        MAX_PEOPLE_COUNT,
                        peopleCount,
                        LOAD_CAPACITY,
                        loadWeight
                ),
                new ReductionConf(
                        LOAD_REDUCTION_FUNCTION,
                        PEOPLE_REDUCTION_FUNCTION
                ),
                new GroundConf(
                        STOP_ACCELERATION,
                        START_ACCELERATION,
                        groundLevel
                )
        );
    }

    @Override
    public LamborghiniConfig snapshot() {
        return new LamborghiniConfig(this.peopleCount,
                this.loadWeight,
                this.x,
                this.y,
                this.elapsedTime,
                this.state,
                this.currentSpeedFactor,
                this.acceleration);
    }

    @Override
    public void rebuild(LamborghiniConfig config) {
        super.rebuild(config.x(),
                config.y(),
                config.elapsedTime(),
                config.state(),
                config.currentSpeedFactor(),
                config.acceleration());
    }
}
