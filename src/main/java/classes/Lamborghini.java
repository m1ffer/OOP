package classes;

import javafx.scene.image.Image;

import java.util.function.Function;

public class Lamborghini extends GroundVehicle implements Animatable{
    private static final Image LAMBORGHINI_IMAGE = new Image(
            Lamborghini.class
                    .getResource("/images/lamborghini.png")
                    .toExternalForm()
    );;
    private static final double IMAGE_HEIGHT = 150;
    private static final double IMAGE_WIDTH = IMAGE_HEIGHT * 3.7547126437;
    private static final double WIDTH = IMAGE_WIDTH;
    private static final double HEIGHT = IMAGE_HEIGHT;
    private static final int MAX_PEOPLE_CNT = 2;
    private static final int LOAD_CAPACITY = 500;
    private static final Function<Integer, Double> LOAD_REDUCTION_FUNCTION = loadWeight -> loadWeight / 4000.0;
    private static final Function<Integer, Double> PEOPLE_REDUCTION_FUNCTION = peopleCNT -> LOAD_REDUCTION_FUNCTION.apply(peopleCNT * 80);
    private static final double STOP_ACCELERATION = -0.9;
    private static final double START_ACCELERATION = 0.9;

    public Lamborghini(double canvasWidth,
                double canvasHeight,
                double groundLevel,
                int peopleCNT,
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
                        MAX_PEOPLE_CNT,
                        peopleCNT,
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
