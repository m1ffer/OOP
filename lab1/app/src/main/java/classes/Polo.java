package classes;

import javafx.scene.image.Image;

import configs.PoloConfig;
import java.util.function.Function;

public class Polo extends GroundVehicle implements Animatable{

    private static final Image POLO_IMAGE = new Image(
            Polo.class
                    .getResource("/images/polo.png")
                    .toExternalForm()
    );;
    private static final double IMAGE_HEIGHT = 200;
    private static final double IMAGE_WIDTH = IMAGE_HEIGHT * 2.7606177606;
    private static final double WIDTH = IMAGE_WIDTH;
    private static final double HEIGHT = IMAGE_HEIGHT;
    public static final int MAX_PEOPLE_COUNT = 5;
    public static final int LOAD_CAPACITY = 1000;
    private static final Function<Integer, Double> LOAD_REDUCTION_FUNCTION = loadWeight -> loadWeight / 2000.0;
    private static final Function<Integer, Double> PEOPLE_REDUCTION_FUNCTION = peopleCount -> LOAD_REDUCTION_FUNCTION.apply(peopleCount * 80);
    private static final double STOP_ACCELERATION = -0.5;
    private static final double START_ACCELERATION = 0.5;

    public Polo(double canvasWidth,
                double canvasHeight,
                double groundLevel,
                int peopleCount,
                int loadWeight){
        super(
                new ImageConf(
                        POLO_IMAGE,
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
                        elapsedTime -> - 150 * elapsedTime,
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

}
