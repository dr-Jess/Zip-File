package render;

public class Operator {
    public static boolean almostEqual(double a, double b, double eps){
        return Math.abs(a-b)<eps;
    }
    public static int limitNum(int n, int lower, int upper){
        if(n < lower)
            return lower;
        if(n > upper)
            return upper;
        return n;
    }
}
