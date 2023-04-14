package render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PixelImage {
    public static final int PIXEL_SIZE = 1;
    private Polygon[] polygons;
    private Coordinate center;

    public PixelImage(BufferedImage b, Coordinate center){
        Coordinate corner = center.translatedBy(-b.getWidth()/2 * PIXEL_SIZE, -b.getHeight()/2 * PIXEL_SIZE, 0);
        ArrayList<Polygon> polys = new ArrayList<Polygon>();
        for(int i = 0; i < b.getWidth(); i++){
            for(int j = 0; j < b.getHeight(); j++){
                if(b.getRGB(i,j) != 0) {
                    int x = (int) corner.getX() + i * PIXEL_SIZE - PIXEL_SIZE/2;
                    int y = (int) corner.getY() + j * PIXEL_SIZE + PIXEL_SIZE/2;
                    int z = (int) corner.getZ();
                    Polygon p  = new Polygon(new Coordinate[]{
                            new Coordinate(x, y, z),
                            new Coordinate(x + PIXEL_SIZE, y, z),
                            new Coordinate(x + PIXEL_SIZE, y + PIXEL_SIZE, z),
                            new Coordinate(x, y + PIXEL_SIZE, z),
                    }, center);
                    p.setColor(new Color((b.getRGB(i, j))));
                    polys.add(p);
                }
            }
        }
        Polygon[] temp = new Polygon[polys.size()];
        for(int i = 0; i < polys.size(); i++){
            temp[i] = polys.get(i);
        }
        this.polygons = temp;
        this.center = center;
    }

    public void incrementRotation(double xRotation, double yRotation, double zRotation){
        for(Polygon p: polygons){
            p.rotateAbout(center,xRotation, yRotation, zRotation);
        }
    }

    public void rotateAbout(Coordinate origin, double xRotation, double yRotation, double zRotation){
        for(Polygon poly: polygons){
            poly.rotateAbout(origin, xRotation, yRotation, zRotation);
        }
        this.center = this.center.rotateAbout(origin, xRotation, yRotation, zRotation);
    }

    public void render(Graphics g){
        Polygon2D[] polys = new Polygon2D[polygons.length];
        for(int i = 0;  i < polygons.length; i++){
            polys[i] = polygons[i].translateToCameraView();
            polys[i].setColor(polygons[i].getColor());
        }
        int index = 0;
        for(Polygon2D p: polys){
            if(polygons[index].getAverageZ() > 0) {
                g.setColor(p.getColor());
                g.fillPolygon(p.getXPoints(), p.getYPoints(), p.numPoints());
            }
            index++;
        }
        Coordinate2D center2d = center.translateToCameraView();
        g.drawOval(center2d.getX()-5, center2d.getY()-5, 10,10);
    }

    public void moveTo(Coordinate translatedBy) {
        for(Polygon p: polygons){
            p.moveTo(translatedBy);
        }
        this.center = translatedBy;
    }

    public Coordinate getCenter() {
        return center;
    }
}
