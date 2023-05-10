package render2D;

import render3D.Operator;

public class Vector2 {
    private final int dx, dy;
    public static final Vector2 ZERO = new Vector2(0,0);

    public static final Vector2 X = new Vector2(1,0);
    public static final Vector2 Y = new Vector2(0,1);

    public Vector2(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public Vector2(Coordinate2 start, Coordinate2 end){
        this.dx = end.getX() - start.getX();
        this.dy = end.getY() - start.getY();
    }

    public int getDx(){
        return this.dx;
    }

    public int getDy(){
        return this.dy;
    }

    public double dotProduct(Vector2 other){
        double x = this.dx * other.getDx();
        double y = this.dy * other.getDy();
        return x + y;
    }


    public double getMagnitude(){

        double deltaX = Math.pow(this.dx,2);
        double deltaY = Math.pow(this.dy,2);

        return Math.sqrt(deltaX+deltaY);
    }

    public Vector2 add(Vector2 v){
        return new Vector2(
                dx + v.getDx(),
                dy + v.getDy()
        );
    }

    public Vector2 subtract(Vector2 v){
        return new Vector2(
                dx - v.getDx(),
                dy - v.getDy()
        );
    }

    public Vector2 multiply(double factor){
        return new Vector2(
                (int) (dx * factor),
                (int) (dy * factor)
        );
    }

    public Coordinate2 toCoordinate(){
        return new Coordinate2(dx,dy);
    }

    public static double angleBetween(Vector2 a, Vector2 b){
        double dot = a.dotProduct(b);
        double aLen = a.getMagnitude();
        double bLen = b.getMagnitude();
        return Math.acos(dot / (aLen * bLen));
    }

    public boolean equals(Object o){
        if(o instanceof Vector2){
            Vector2 v = (Vector2) o;
            return Operator.almostEqual(dx, v.getDx(), 1e-10) &&
                    Operator.almostEqual(dy, v.getDy(), 1e-10);
        }
        return false;
    }

    public String toString(){
        return dx + " " + dy;
    }

    public static void main(String[] args) {

    }

}
