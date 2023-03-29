package render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Polyhedron {


    public enum Shape {
        CUBE{
            @Override
            Polygon[] getFaces(double sideLength, Coordinate center) {
                double distance = 0.5 * sideLength;
                Polygon[] faces = {
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance, distance,distance),
                                center.translatedBy(distance,-distance,distance),
                                center.translatedBy(-distance,-distance,distance),
                                center.translatedBy(-distance,distance,distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance, distance,-distance),
                                center.translatedBy(distance,-distance,-distance),
                                center.translatedBy(-distance,-distance,-distance),
                                center.translatedBy(-distance,distance,-distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance,distance,distance),
                                center.translatedBy(distance,-distance,distance),
                                center.translatedBy(distance,-distance,-distance),
                                center.translatedBy(distance,distance,-distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(-distance,distance,distance),
                                center.translatedBy(-distance,-distance,distance),
                                center.translatedBy(-distance,-distance,-distance),
                                center.translatedBy(-distance,distance,-distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance,distance,distance),
                                center.translatedBy(-distance,distance,distance),
                                center.translatedBy(-distance,distance,-distance),
                                center.translatedBy(distance,distance,-distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance,-distance,distance),
                                center.translatedBy(-distance,-distance,distance),
                                center.translatedBy(-distance,-distance,-distance),
                                center.translatedBy(distance,-distance,-distance),
                        },center)
                };
                faces[0].setColor(Color.RED);
                faces[1].setColor(Color.BLUE);
                faces[2].setColor(Color.GREEN);
                faces[3].setColor(Color.CYAN);
                faces[4].setColor(Color.MAGENTA);
                faces[5].setColor(Color.YELLOW);
                return faces;
            }
        },
        TETRAHEDRON{
            @Override
            Polygon[] getFaces(double sideLength, Coordinate center) {
                return new Polygon[0];
            }
        },
        IMAGE_CUBE{
            @Override
            Polygon[] getFaces(double sideLength, Coordinate center) {
                double distance = 0.5 * sideLength;
                BufferedImage b = null;
                try{
                    b = ImageIO.read(new File("C:\\Users\\jonat\\Downloads\\garf.png"));
                } catch(Exception e){
                    System.out.println("AA");
                }
                Polygon[] faces = {
                        new TexturedPolygon(new Coordinate[]{
                                center.translatedBy(distance, distance,distance),
                                center.translatedBy(distance,-distance,distance),
                                center.translatedBy(-distance,-distance,distance),
                                center.translatedBy(-distance,distance,distance),
                        },center, b),
                        new TexturedPolygon(new Coordinate[]{
                                center.translatedBy(distance, distance,-distance),
                                center.translatedBy(distance,-distance,-distance),
                                center.translatedBy(-distance,-distance,-distance),
                                center.translatedBy(-distance,distance,-distance),
                        },center, b),
                        new TexturedPolygon(new Coordinate[]{
                                center.translatedBy(distance,distance,distance),
                                center.translatedBy(distance,-distance,distance),
                                center.translatedBy(distance,-distance,-distance),
                                center.translatedBy(distance,distance,-distance),
                        },center, b),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(-distance,distance,distance),
                                center.translatedBy(-distance,-distance,distance),
                                center.translatedBy(-distance,-distance,-distance),
                                center.translatedBy(-distance,distance,-distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance,distance,distance),
                                center.translatedBy(-distance,distance,distance),
                                center.translatedBy(-distance,distance,-distance),
                                center.translatedBy(distance,distance,-distance),
                        },center),
                        new Polygon(new Coordinate[]{
                                center.translatedBy(distance,-distance,distance),
                                center.translatedBy(-distance,-distance,distance),
                                center.translatedBy(-distance,-distance,-distance),
                                center.translatedBy(distance,-distance,-distance),
                        },center)
                };
                faces[0].setColor(Color.RED);
                faces[1].setColor(Color.BLUE);
                faces[2].setColor(Color.GREEN);
                faces[3].setColor(Color.CYAN);
                faces[4].setColor(Color.MAGENTA);
                faces[5].setColor(Color.YELLOW);
                return faces;
            }
        },;
        abstract Polygon[] getFaces(double sideLength, Coordinate center);
    }
    private Polygon[] faces;
    private Coordinate center;


    public Polyhedron(Polygon[] faces, Coordinate center){
        this.faces = faces;
        this.center = center;
    }

    public Polyhedron(Shape s, double sideLength, Coordinate center){
        this.faces = s.getFaces(sideLength, center);
        this.center = center;
    }

    public void rotateAbout(Coordinate origin, double xRotation, double yRotation, double zRotation){
        for(Polygon face: faces){
            face.rotateAbout(origin, xRotation, yRotation, zRotation);
        }
        this.center = this.center.rotateAbout(origin, xRotation, yRotation, zRotation);
    }

    public void incrementRotation(double xRotation, double yRotation, double zRotation){
        for(Polygon p: faces){
            p.incrementRotation(xRotation, yRotation, zRotation);
        }
    }

    double[] zBuffered = new double[6];
    ArrayList<Polygon> visiblePolygons = new ArrayList<>();

    public void render(Graphics g){
        Polygon2D[] polys = new Polygon2D[faces.length];
        ArrayList<Polygon> sorted = zBuffer();
        for(int i = 0;  i < faces.length; i++){
            polys[i] = sorted.get(i).translateToCameraView();
            polys[i].setColor(sorted.get(i).getColor());
        }
        int index = 0;
        for(Polygon2D p: polys){
            if(sorted.get(index).getAverageZ() > 0) {
                g.setColor(p.getColor());
                if(sorted.get(index) instanceof TexturedPolygon)
                    ((TexturedPolygon) sorted.get(index)).render(g);
                else
                    g.fillPolygon(p.getXPoints(), p.getYPoints(), p.numPoints());
            }
            index++;
        }
    }

    //TODO implement perspective z-buffer code
    public ArrayList<Polygon> zBuffer() {
        double near = -100;
        double far = 1000;
        double[] zPrimes = new double[6];
        for (int i = 0; i < 6; i++) {
            zPrimes[i] = 15 * ((far + near) / (far - near)) + (1/faces[i].getAverageDistanceFromCamera()) * (-2 * (far * near) / (far - near));

            //zPrimes[i] = 2.0 * (zPrimes[i] - near) / (far - near) - 1;
        }
        ArrayList<Polygon> visible = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            visible.add(faces[i]);
        }
        int n = zPrimes.length;
        for (int i = 1; i < n; ++i) {
            double key = zPrimes[i];
            int j = i - 1;

            while (j >= 0 && zPrimes[j] > key) {
                zPrimes[j + 1] = zPrimes[j];
                j = j - 1;
            }
            zPrimes[j + 1] = key;
            visible.add(j + 1, visible.remove(i));
        }
        return visible;
    }
    public Coordinate getCenter() {
        return center;
    }

    public void moveTo(Coordinate translatedBy) {
        for(Polygon p: faces){
            p.moveTo(translatedBy);
        }
        this.center = translatedBy;
    }
}
