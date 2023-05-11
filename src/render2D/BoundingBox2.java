package render2D;

public class BoundingBox2 {
    private final int top, bottom, left, right;
    public BoundingBox2(int top, int bottom, int left, int right){
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }


    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
