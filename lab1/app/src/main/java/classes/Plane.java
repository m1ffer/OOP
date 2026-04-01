package classes;

import javafx.scene.image.Image;

import configs.PlaneConfig;
import java.util.function.Function;

public class Plane extends AirVehicle implements Animatable{
    public static final Image PLANE_IMAGE = new Image(
            Plane.class
                    .getResource("/images/plane.png")
                    .toExternalForm()
    );;
    public static final double IMAGE_HEIGHT = 100;
    public static final double IMAGE_WIDTH = IMAGE_HEIGHT * 4.35;
    public static final double WIDTH = IMAGE_WIDTH;
    public static final double HEIGHT = IMAGE_HEIGHT;
    public static final int MAX_PEOPLE_COUNT = 100;
    public static final int LOAD_CAPACITY = 25000;
    public static final Function<Integer, Double> LOAD_REDUCTION_FUNCTION = loadWeight -> (double) loadWeight / (LOAD_CAPACITY * 10);
    public static final Function<Integer, Double> PEOPLE_REDUCTION_FUNCTION = peopleCount -> LOAD_REDUCTION_FUNCTION.apply(peopleCount * 80);

    public Plane(double canvasWidth,
                      double canvasHeight,
                      int peopleCount,
                      int loadWeight,
                      double startY){
        super(
                new ImageConf(
                        PLANE_IMAGE,
                        canvasWidth + VERY_SMALL_NUMBER,
                        startY,
                        WIDTH,
                        HEIGHT,
                        canvasWidth,
                        canvasHeight,
                        IMAGE_WIDTH,
                        IMAGE_HEIGHT
                ),
                new MotionConf(
                        elapsedTime -> - 250 * elapsedTime,
                        elapsedTime -> - 125 / Math.sqrt(3) * elapsedTime
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
                new FlightConf(
                        startY + HEIGHT + VERY_SMALL_NUMBER,
                        - HEIGHT - VERY_SMALL_NUMBER
                )
        );
    }
}
