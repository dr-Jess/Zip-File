package render;

public class Operator {
    public static boolean almostEqual(double a, double b, double eps){
        return Math.abs(a-b)<eps;
    }
}
