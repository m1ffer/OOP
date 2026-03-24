package classes;

import javafx.scene.image.Image;

import java.util.function.Function;

public class Helicopter extends AirVehicle implements Animatable{
    private static final Image HELICOPTER_IMAGE = new Image(
            Helicopter.class
                    .getResource("/images/helicopter.png")
                    .toExternalForm()
    );;
    private static final double IMAGE_HEIGHT = 100;
    private static final double IMAGE_WIDTH = IMAGE_HEIGHT * 3.255721393;
    private static final double WIDTH = IMAGE_WIDTH;
    private static final double HEIGHT = IMAGE_HEIGHT;
    private static final int MAX_PEOPLE_CNT = 8;
    private static final int LOAD_CAPACITY = 8000;
    private static final Function<Integer, Double> LOAD_REDUCTION_FUNCTION = loadWeight -> loadWeight / 15000.0;
    private static final Function<Integer, Double> PEOPLE_REDUCTION_FUNCTION = peopleCNT -> LOAD_REDUCTION_FUNCTION.apply(peopleCNT * 80);

    public Helicopter(double canvasWidth,
                       double canvasHeight,
                       int peopleCNT,
                       int loadWeight,
                       double lowerBound,
                       double upperBound){
        super(
                new ImageConf(
                        HELICOPTER_IMAGE,
                        - WIDTH - VERY_SMALL_NUMBER,
                        (upperBound + lowerBound - HEIGHT) / 2,
                        WIDTH,
                        HEIGHT,
                        canvasWidth,
                        canvasHeight,
                        IMAGE_WIDTH,
                        IMAGE_HEIGHT
                ),
                new MotionConf(
                        elapsedTime -> 125 * elapsedTime,
                        elapsedTime ->
                                        (lowerBound - HEIGHT - upperBound) / 2 * Math.sin(elapsedTime)
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
                new FlightConf(
                        lowerBound,
                        upperBound
                )
        );
    }
}
