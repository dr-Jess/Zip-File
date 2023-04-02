package render3D;

import render.Polygon;
import render2D.Polygon2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        ArrayList<MeshPolygon> buffered = zBuffer();

        for(int i = buffered.size()-1; i > -1; i--){
            //TODO z --> direction to camera
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

    public ArrayList<MeshPolygon> zBuffer() {
        Arrays.sort(polygons);
        ArrayList<MeshPolygon> temp = new ArrayList<>();
        for(MeshPolygon mp: polygons){
            temp.add(mp);
        }
        return temp;
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
