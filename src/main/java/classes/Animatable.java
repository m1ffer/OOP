package classes;

import javafx.scene.canvas.GraphicsContext;

public interface Animatable {
    void start();
    void stop();
    boolean isStopped();
    boolean isOutOfBounds();
    default boolean isVisible(){
        return !isOutOfBounds();
    }
    void move(double deltaSeconds);
    void draw(GraphicsContext gc);
    void setPeopleCNT(int peopleCNT);
    void setLoadWeight(int loadWeight);
    double getX();
}
