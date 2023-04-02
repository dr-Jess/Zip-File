package render2D;

import render.Operator;

public class Vector2D {
    private final int dx, dy;
    public static final Vector2D ZERO = new Vector2D(0,0);

    public static final Vector2D X = new Vector2D(1,0);
    public static final Vector2D Y = new Vector2D(0,1);

    public Vector2D(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public Vector2D(Coordinate2D start, Coordinate2D end){
        this.dx = end.getX() - start.getX();
        this.dy = end.getY() - start.getY();
    }

    public int getDx(){
        return this.dx;
    }

    public int getDy(){
        return this.dy;
    }

    public double dotProduct(Vector2D other){
        double x = this.dx * other.getDx();
        double y = this.dy * other.getDy();
        return x + y;
    }


    public double getMagnitude(){

        double deltaX = Math.pow(this.dx,2);
        double deltaY = Math.pow(this.dy,2);

        return Math.sqrt(deltaX+deltaY);
    }

    public Vector2D add(Vector2D v){
        return new Vector2D(
                dx + v.getDx(),
                dy + v.getDy()
        );
    }

    public Vector2D subtract(Vector2D v){
        return new Vector2D(
                dx - v.getDx(),
                dy - v.getDy()
        );
    }

    public Vector2D multiply(double factor){
        return new Vector2D(
                (int) (dx * factor),
                (int) (dy * factor)
        );
    }

    public Coordinate2D toCoordinate(){
        return new Coordinate2D(dx,dy);
    }

    public static double angleBetween(Vector2D a, Vector2D b){
        double dot = a.dotProduct(b);
        double aLen = a.getMagnitude();
        double bLen = b.getMagnitude();
        return Math.acos(dot / (aLen * bLen));
    }

    public boolean equals(Object o){
        if(o instanceof Vector2D){
            Vector2D v = (Vector2D) o;
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
