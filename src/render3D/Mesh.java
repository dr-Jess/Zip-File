package render3D;

import render.Operator;
import render.Scene;
import render2D.BoundingBox2D;
import render2D.Coordinate2D;
import render2D.Polygon2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Mesh {
    private MeshPolygon[] polygons;
    Coordinate center;

    public Mesh(MeshPolygon[] polygons, Coordinate center){
        this.polygons = polygons;
        this.center = center;
    }

    public Mesh(ArrayList<MeshPolygon> polygons, Coordinate center){
        MeshPolygon[] temp = new MeshPolygon[polygons.size()];
        for(int i = 0; i < polygons.size(); i++){
            temp[i] = polygons.get(i);
        }
        this.polygons = temp;
        this.center = center;
    }

    public void render(Graphics g){
        //paintersAlgorithm(g);
        zBuffer(g);

    }

    public void paintersAlgorithm(Graphics g){
        ArrayList<MeshPolygon> buffered = sortPolygons();

        for(int i = buffered.size()-1; i > -1; i--){
            Coordinate avg = buffered.get(i).getAveragePoint();
            Vector ref = new Vector(Coordinate.ORIGIN, avg);
            double check = buffered.get(i).getNormal().dotProduct(ref);
            //buffered.get(i).getNormal().getDz() <= 0
            if(check <= 0) {
                Polygon2D p2d = buffered.get(i).translateToCameraView();
                g.setColor(p2d.getColor());
                g.fillPolygon(p2d.getXPoints(), p2d.getYPoints(), p2d.numPoints());
                g.drawPolygon(p2d.getXPoints(), p2d.getYPoints(), p2d.numPoints());
            }
        }
    }

    public ArrayList<MeshPolygon> sortPolygons() {
        Arrays.sort(polygons);
        ArrayList<MeshPolygon> temp = new ArrayList<>();
        for(MeshPolygon mp: polygons){
            temp.add(mp);
        }
        return temp;
    }

    double[][] zDepths = new double[Scene.SCREEN_WIDTH][Scene.SCREEN_HEIGHT];
    Color[][] screen = new Color[Scene.SCREEN_WIDTH][Scene.SCREEN_HEIGHT];


    public void zBuffer(Graphics g){
        //TODO implement zbuffer code

        for(MeshPolygon p: polygons) {
            Color c = p.getColor();

            Coordinate avg = p.getAveragePoint();
            double cx = avg.getX();
            double cy = avg.getY();
            double cz = avg.getZ();

            Vector faceNormal = p.getNormal();
            double dx = faceNormal.getDx();
            double dy = faceNormal.getDy();
            double dz = faceNormal.getDz();

            Polygon2D translated = p.translateToCameraView();
            BoundingBox bounds = p.getBounds();
            BoundingBox2D bounds2D = translated.getBounds();
            double xMin = bounds.getLeft();
            double xMax = bounds.getRight();
            double xIncrement = 0.7 * (xMax - xMin) / (Operator.limitNum(bounds2D.getRight(),0,Scene.SCREEN_WIDTH) - Operator.limitNum(bounds2D.getLeft(),0,Scene.SCREEN_WIDTH));
            double yMin = bounds.getBottom();
            double yMax = bounds.getTop();
            double yIncrement = 0.7 * (yMax - yMin) / (Operator.limitNum(bounds2D.getTop(),0,Scene.SCREEN_HEIGHT) - Operator.limitNum(bounds2D.getBottom(),0,Scene.SCREEN_HEIGHT));
            double zMin = bounds.getBack();
            double zMax = bounds.getFront();
            if((bounds2D.getRight() >= 0 || bounds2D.getLeft() <= Scene.SCREEN_WIDTH) && (bounds2D.getBottom() >= 0 || bounds2D.getTop() <= Scene.SCREEN_HEIGHT))
            //if(!Operator.almostEqual(faceNormal.dotProduct(new Vector(Coordinate.ORIGIN,avg)),0,1e-4))
            if (zMax > 0) {
                //System.out.println("X: " + xMin + " " + xMax + " " + xIncrement + " " + bounds2D.getRight() + " " + bounds2D.getLeft() + " " + (Operator.limitNum(bounds2D.getRight(),0,Scene.SCREEN_WIDTH) - Operator.limitNum(bounds2D.getLeft(),0,Scene.SCREEN_WIDTH)));
                //System.out.println("Y: " + yMin + " " + yMax + " " + yIncrement + " " + bounds2D.getTop() + " " + bounds2D.getBottom() + " " + + (Operator.limitNum(bounds2D.getTop(),0,Scene.SCREEN_HEIGHT) - Operator.limitNum(bounds2D.getBottom(),0,Scene.SCREEN_HEIGHT)));
                //System.out.println("Z: " + zMin + " " + zMax);
                for (double x = xMin; x < xMax; x += xIncrement) {
                    for (double y = yMin; y < yMax; y += yIncrement) {;
                        double zDepth = cz - (dx * (x - cx) + dy * (y - cy)) / dz;
                        if (zDepth >= 0) {
                            Coordinate check = new Coordinate(x, y, 0);
                            if (p.intersects(check)) {
                                double zMod = 1 / (zDepth + 1);
                                int screenX = (int) Math.round(x * Scene.PLANE_DISTANCE_FROM_CAMERA * zMod  + Scene.SCREEN_CENTER.getX());
                                int screenY = (int) Math.round(y * Scene.PLANE_DISTANCE_FROM_CAMERA * zMod + Scene.SCREEN_CENTER.getY());

                                if (0 < screenX && screenX < Scene.SCREEN_WIDTH && 0 < screenY && screenY < Scene.SCREEN_HEIGHT)
                                    if (zDepths[screenX][screenY] > zDepth) {
                                        zDepths[screenX][screenY] = zDepth;
                                        screen[screenX][screenY] = c;
                                    }
                            }
                        }
                    }
                }

            }
        }

        for(int x = 0; x < screen.length; x++){
            for(int y = 0; y < screen[0].length; y++){
                if(screen[x][y] != null) {
                    g.setColor(screen[x][y]);
                    g.drawPolygon(new int[]{x,x},new int[]{y,y},2);
                }
                zDepths[x][y] = Double.MAX_VALUE;
                screen[x][y] = null;
            }
        }
    }

    public void moveTo(Coordinate translatedBy) {
        for(MeshPolygon p: polygons){
            p.moveTo(translatedBy);
        }
        this.center = translatedBy;
    }

    public Coordinate getCenter() {
        return this.center;
    }

    public void incrementRotation(double xRotation, double yRotation, double zRotation){
        for(MeshPolygon p: polygons){
            p.incrementRotation(xRotation, yRotation, zRotation);
        }
    }
}
