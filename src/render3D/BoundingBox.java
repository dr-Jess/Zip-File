package render3D;

public class BoundingBox {

    private final double top;
    private final double bottom;
    private final double left;
    private final double right;
    private final double front;


    private final double back;
    public BoundingBox(double top, double bottom, double left, double right, double front, double back){
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.front = front;
        this.back = back;
    }


    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }


    public double getFront() {
        return front;
    }

    public double getBack() {
        return back;
    }

}
