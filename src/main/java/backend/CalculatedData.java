package backend;

import java.util.Arrays;

public class CalculatedData {
    private final static String METHOD_NAME = null;
    private double[][] array = null;
    private double[] xy = null;

    public CalculatedData() {
    }

    public static class LagrangeData extends CalculatedData {
        private final static String METHOD_NAME = "Лагранж";
    }

    public static class NewtonData extends CalculatedData {
        private final static String METHOD_NAME = "Ньютон";
    }

    public static class NewtonForwardsData extends CalculatedData {
        private final static String METHOD_NAME = "Ньютон (равнуд. узлы; вперед)";
    }

    public static class NewtonBackwardsData extends CalculatedData {
        private final static String METHOD_NAME = "Ньютон (равнуд. узлы; назад)";
    }

    public double[][] getArray() {
        return array;
    }

    public void setArray(double[][] array) {
        this.array = array;
    }

    public double[] getXy() {
        return xy;
    }

    public void setXy(double[] xy) {
        this.xy = xy;
    }

    @Override
    public String toString() {
        return "CalculatedData{" +
                "array=" + Arrays.toString(array) +
                ", xy=" + Arrays.toString(xy) +
                '}';
    }
}
