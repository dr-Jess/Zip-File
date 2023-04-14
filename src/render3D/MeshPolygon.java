package render3D;

import render.Operator;
import render2D.Polygon2D;

import java.awt.*;

public class MeshPolygon extends Polygon{
    Vector normal;


    public MeshPolygon(Vertex[] points, Coordinate center) {
        super(points, center);
        this.normal = avgOfVertexNormals(points);
        super.setColor(Color.YELLOW);
    }

    private Vector avgOfVertexNormals(Vertex[] vertices) {
        Vector temp = Vector.ZERO;

        for(Vertex v: vertices){
            temp = temp.add(v.getNormal());
        }
        double factor = 1.0 / vertices.length;

        return temp.multiply(factor);
    }

    public Vector getNormal(){
        return normal;
    }

    @Override
    public void incrementRotation(double xRotation, double yRotation, double zRotation) {
        super.incrementRotation(xRotation, yRotation, zRotation);
        normal = normal.incrementRotation(xRotation, yRotation, zRotation);
    }

    public Polygon2D translateToCameraView(){
        Polygon2D p = new Polygon2D(super.getPoints());
        p.setColor(this.getColor());
        return p;
    }

    public Color getColor(){
        Color c = super.getColor();
        double mod = 0.2;
        double y = -mod* (normal.getDy() / normal.getMagnitude()) + 1;
        int red = Operator.limitNum((int) (c.getRed() * y),0,255);
        int green = Operator.limitNum((int) (c.getGreen() * y),0,255);
        int blue = Operator.limitNum((int) (c.getBlue() * y),0,255);
        return new Color(red, green, blue);
    }

}
