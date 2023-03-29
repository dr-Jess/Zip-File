
package render;
//sorry

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

    public Coordinate rotateAbout(Coordinate origin, double xRotation, double yRotation, double zRotation){
        Coordinate point = rotatedAbout(origin, this, xRotation, Axis.X);
        point = rotatedAbout(origin, point, yRotation, Axis.Y);
        point = rotatedAbout(origin, point, zRotation, Axis.Z);
        return point;
    }

    private static Coordinate rotatedAbout(Coordinate origin, Coordinate other, double increment, Axis axis){
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

    public Coordinate getPointAt(double distance, double xAngle, double yAngle, double zAngle){
        return this.translatedBy(0,0,distance).rotateAbout(this, xAngle, yAngle, zAngle);
    }

    public boolean isInCameraView(){
        double width = Scene.SCREEN_WIDTH * (this.z / Scene.PLANE_DISTANCE_FROM_CAMERA);
        double height = Scene.SCREEN_HEIGHT * (this.z / Scene.PLANE_DISTANCE_FROM_CAMERA);
        return z > 0 &&
                -width/2 < this.x && this.x < width/2 &&
                -height/2 < this.y && this.y < height/2;
    }

    //TODO implement correctly
    public Coordinate2D translateToCameraView(){
        Coordinate mod = getTransformedPoint();
        double zMod = Scene.PLANE_DISTANCE_FROM_CAMERA/(this.z+ 0.5*Scene.PLANE_DISTANCE_FROM_CAMERA);
        double x2D = mod.getX() * Scene.PLANE_DISTANCE_FROM_CAMERA/(mod.getZ()+1) + Scene.SCREEN_CENTER.getX(); //Scene.SCREEN_CENTER.getX() + this.x * zMod;
        double y2D = mod.getY() * Scene.PLANE_DISTANCE_FROM_CAMERA/(mod.getZ()+1) + Scene.SCREEN_CENTER.getY();
        return new Coordinate2D((int) x2D, (int) y2D);
    }

    public Coordinate getTransformedPoint(){
        double tX = 0;
        double tY = 0;
        double tZ = 0;
        double aX = this.x;
        double aY = this.y;
        double aZ = this.z;
        double cX = Math.cos(tX);
        double cY = Math.cos(tY);
        double cZ = Math.cos(tZ);
        double sX = Math.sin(tX);
        double sY = Math.sin(tY);
        double sZ = Math.sin(tZ);
        return new Coordinate(
                cY * (sY*aY + cZ*aX) - sY*aZ,
                sX * (cY*aZ + sY*(sZ*aY + cZ*aX)) + cX*(cZ*aY - sZ*aX),
                cX * (cY*aZ + sY*(sZ*aY + cZ*aX)) - sX*(cZ*aY - sZ*aX)
        );
    }

    public Coordinate translateByDiff(Coordinate a, Coordinate b){
        double x = this.x + (b.getX() - a.getX());
        double y = this.y + (b.getY() - a.getY());
        double z = this.z + (b.getZ() - a.getZ());
        return new Coordinate(x,y,z);
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