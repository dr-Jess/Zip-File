
package rendering;


import render.Operator;

/**
 * 3D Coordinate Class
 * The x-axis runs horizontally on the screen, the z-axis runs into and out of
 * the screen, and the y-axis runs vertically on the screen.
 * The XY plane is the screen.
 * The z-value of the coordinate is used to represent depth.
 */
public class Coordinate {
    private final double x, y, z;

    public enum Axis{
        X,
        Y,
        Z
    }

    public Coordinate(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }


    public double distanceFrom(Coordinate c){
        double deltaX = Math.pow(this.x-c.getX(),2);
        double deltaY = Math.pow(this.y-c.getY(),2);
        double deltaZ = Math.pow(this.z-c.getZ(),2);

        return Math.sqrt(deltaX+deltaY+deltaZ);
    }

    public static Coordinate rotateAbout(Coordinate origin, Coordinate other, double xRotation, double yRotation, double zRotation){
        Coordinate point = rotateAbout(origin, other, xRotation, Axis.X);
        point = rotateAbout(origin, point, yRotation, Axis.Y);
        point = rotateAbout(origin, point, zRotation, Axis.Z);
        return point;
    }

    private static Coordinate rotateAbout(Coordinate origin, Coordinate other, double increment, Axis axis){
        Coordinate point;
        if(axis == Axis.X){
            Coordinate rotateAbout = new Coordinate(other.getX(),origin.getY(),origin.getZ());
            double radius = other.distanceFrom(rotateAbout);
            if(Operator.almostEqual(radius, 0, 1e-10))
                return other;
            double angle = (Math.asin((other.getZ()-origin.getZ())/radius));
            point = new Coordinate(
                    other.getX(),
                    origin.getY()+radius * Math.cos(-angle+increment),
                    origin.getZ()+radius * Math.sin(angle-increment)
            );
            if(other.getY() < origin.getY()) {
                point = new Coordinate(
                        other.getX(),
                        origin.getY()-radius * Math.cos(angle + increment),
                        origin.getZ()+radius * Math.sin(angle + increment)
                );
            }
        }
        else if(axis == Axis.Y){
            Coordinate rotateAbout = new Coordinate(origin.getX(),other.getY(),origin.getZ());
            double radius = other.distanceFrom(rotateAbout);
            if(Operator.almostEqual(radius, 0, 1e-10))
                return other;
            double angle = (Math.asin((other.getZ()-origin.getZ())/radius));
            point = new Coordinate(
                    origin.getX()+radius * Math.cos(-angle+increment),
                    other.getY(),
                    origin.getZ()+radius * Math.sin(angle-increment)
            );
            if(other.getX() < origin.getX())
                point = new Coordinate(
                        origin.getX()-radius * Math.cos(angle+increment),
                        other.getY(),
                        origin.getZ()+radius * Math.sin(angle+increment)
                );
        }
        else{
            Coordinate rotateAbout = new Coordinate(origin.getX(),origin.getY(),other.getZ());
            double radius = other.distanceFrom(rotateAbout);
            if(Operator.almostEqual(radius, 0, 1e-10))
                return other;
            double angle = (Math.asin((other.getY()-origin.getY())/radius));
            point = new Coordinate(
                    origin.getX()+radius * Math.cos(-angle-increment),
                    origin.getY()+radius * Math.sin(angle+increment),
                    other.getZ()
            );
            if(other.getX() < origin.getX())
                point = new Coordinate(
                        origin.getX()-radius * Math.cos(angle-increment),
                        origin.getY()+radius * Math.sin(angle-increment),
                        other.getZ()
                );
        }
        return point;
    }

    public Coordinate translatedBy(double xIncrement, double yIncrement, double zIncrement){
        return new Coordinate(
                this.x + xIncrement,
                this.y + yIncrement,
                this.z + zIncrement
        );
    }


    public boolean equals(Object o){
        if (o instanceof Coordinate) {
            Coordinate other = (Coordinate) o;
            return Operator.almostEqual(this.x, other.getX(), 1e-4) &&
                    Operator.almostEqual(this.y, other.getY(), 1e-4) &&
                    Operator.almostEqual(this.z, other.getZ(), 1e-4);
        }
        return false;
    }

    public String toString(){
        return (double) x + " " + (double) y + " " + (double) z;
    }


}