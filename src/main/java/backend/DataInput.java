package backend;

public class DataInput {
    private double[][] xy;
    private double point;

    public DataInput() {
    }

    public DataInput(double[][] xy, double point) {
        this.xy = xy;
        this.point = point;
    }

    public double[][] getXY() {
        return xy;
    }

    public void setXY(double[][] xy) {
        this.xy = xy;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
