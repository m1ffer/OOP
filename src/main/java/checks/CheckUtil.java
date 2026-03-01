package checks;

public class CheckUtil {
    private CheckUtil(){}

    public static class MinBoundException extends IllegalArgumentException{}
    public static class MaxBoundException extends IllegalArgumentException{}

    public static int checkMinBound(int val, int minBound){
        if (val < minBound)
            throw new MinBoundException();
        return val;
    }

}
