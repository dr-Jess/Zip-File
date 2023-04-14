package render3D;

import render.Operator;

public class Vector {
    private final double dx, dy, dz;
    public static final Vector ZERO = new Vector(0,0,0);

    public static final Vector X = new Vector(1,0,0);
    public static final Vector Y = new Vector(0,1,0);
    public static final Vector Z = new Vector(0,0,1);

    public Vector(double dx, double dy, double dz){
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public Vector(Coordinate start, Coordinate end){
        this.dx = end.getX() - start.getX();
        this.dy = end.getY() - start.getY();
        this.dz = end.getZ() - start.getZ();
    }

    public double getDx(){
        return this.dx;
    }

    public double getDy(){
        return this.dy;
    }

    public double getDz(){
        return this.dz;
    }

    public double dotProduct(Vector other){
        double x = this.dx * other.getDx();
        double y = this.dy * other.getDy();
        double z = this.dz * other.getDz();
        return x + y + z;
    }

    public Vector crossProduct(Vector other){
        double ax = this.dx;
        double ay = this.dy;
        double az = this.dz;
        double bx = other.getDx();
        double by = other.getDy();
        double bz = other.getDz();

        return ZERO
                .add(X.multiply(ay * bz - az * by))
                .subtract(Y.multiply(ax * bz - az * bx))
                .add(Z.multiply(ax * by - ay * bx));
    }


    public double getMagnitude(){

        double deltaX = Math.pow(this.dx,2);
        double deltaY = Math.pow(this.dy,2);
        double deltaZ = Math.pow(this.dz,2);

        return Math.sqrt(deltaX+deltaY+deltaZ);
    }

    public Vector incrementRotation(double xRotation, double yRotation, double zRotation){
        Vector temp = this;
        double radius = this.getMagnitude();
        Coordinate c = this.toCoordinate();
        c = c.rotateAbout(Coordinate.ORIGIN,xRotation,yRotation,zRotation);
        return c.toVector(Coordinate.ORIGIN);
    }



    public Vector add(Vector v){
        return new Vector(
                dx + v.getDx(),
                dy + v.getDy(),
                dz + v.getDz()
        );
    }

    public Vector subtract(Vector v){
        return new Vector(
                dx - v.getDx(),
                dy - v.getDy(),
                dz - v.getDz()
        );
    }

    public Vector multiply(double factor){
        return new Vector(
                dx * factor,
                dy * factor,
                dz * factor
        );
    }

    public Coordinate toCoordinate(){
        return new Coordinate(dx,dy,dz);
    }

    public static double angleBetween(Vector a, Vector b){
        double dot = a.dotProduct(b);
        double aLen = a.getMagnitude();
        double bLen = b.getMagnitude();
        return Math.acos(dot / (aLen * bLen));
    }

    public boolean equals(Object o){
        if(o instanceof Vector){
            Vector v = (Vector) o;
            return Operator.almostEqual(dx, v.getDx(), 1e-10) &&
                    Operator.almostEqual(dy, v.getDy(), 1e-10) &&
                    Operator.almostEqual(dz, v.getDz(), 1e-10);
        }
        return false;
    }

    public String toString(){
        return dx + " " + dy + " " + dz;
    }

    public static void main(String[] args) {
        System.out.println(new Vector(1,0,0).crossProduct(new Vector(1,0,0)));
        double x = Math.PI;
        double y = Math.PI;
        double z = Math.PI;
        System.out.println(new Vector(1,0,0).incrementRotation(x,y,z));
        System.out.println(new Coordinate(2,3,1).rotateAbout(Coordinate.ORIGIN, x,y,z));
    }

}
