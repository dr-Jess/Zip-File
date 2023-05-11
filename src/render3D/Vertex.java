package render3D;

public class Vertex extends Coordinate{
    Vector normal;

    public Vertex(double x, double y, double z, Vector normal) {
        super(x, y, z);
        this.normal = normal;
    }

    public Vertex(double x, double y, double z) {
        super(x, y, z);
        this.normal = Vector.ZERO;
    }

    public void setNormal(Vector v){
        this.normal = v;
    }
    public Vector getNormal(){
        return normal;
    }


}
