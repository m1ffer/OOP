package classes;

import javafx.scene.image.Image;

import configs.HelicopterConfig;
import java.util.function.Function;

public class Helicopter extends AirVehicle implements Animatable<HelicopterConfig>{
    public static final Image HELICOPTER_IMAGE = new Image(
            Helicopter.class
                    .getResource("/images/helicopter.png")
                    .toExternalForm()
    );;
    public static final double IMAGE_HEIGHT = 100;
    public static final double IMAGE_WIDTH = IMAGE_HEIGHT * 3.255721393;
    public static final double WIDTH = IMAGE_WIDTH;
    public static final double HEIGHT = IMAGE_HEIGHT;
    public static final int MAX_PEOPLE_COUNT = 8;
    public static final int LOAD_CAPACITY = 8000;
    public static final Function<Integer, Double> LOAD_REDUCTION_FUNCTION = loadWeight -> loadWeight / 15000.0;
    public static final Function<Integer, Double> PEOPLE_REDUCTION_FUNCTION = peopleCount -> LOAD_REDUCTION_FUNCTION.apply(peopleCount * 80);

    public Helicopter(double canvasWidth,
                       double canvasHeight,
                       int peopleCount,
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
                        lowerBound,
                        upperBound
                )
        );
    }

    @Override
    public HelicopterConfig snapshot(){
        return new HelicopterConfig(peopleCount,
                loadWeight,
                lowerBound,
                upperBound,
                x,
                y,
                elapsedTime);
    }

    @Override
    public void rebuild(HelicopterConfig config) {
        super.rebuild(config.x(),
                config.y(),
                config.elapsedTime());
    }
}
