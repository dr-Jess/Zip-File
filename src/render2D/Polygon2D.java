package render2D;

import render3D.BoundingBox;
import render3D.Coordinate;

import java.awt.*;

public class Polygon2D {
    private Coordinate2D[] points;

    private Color color = new Color((int) (Math.random() * 255) ,(int) (Math.random() * 255) ,(int) (Math.random() * 255) );

    public Polygon2D(Coordinate2D[] points){
        this.points = points;
    }

    public Polygon2D(Coordinate[] points){
        Coordinate2D[] temp = new Coordinate2D[points.length];
        for(int i = 0; i < points.length; i++){
            temp[i] = points[i].translateToCameraView();
        }
        this.points = temp; //expandPolygon(temp);
    }
    public int[] getXPoints(){
        int[] temp = new int[points.length];
        for(int i = 0; i < points.length; i++){
            temp[i] = points[i].getX();
        }
        return temp;
    }

    public int[] getYPoints(){
        int[] temp = new int[points.length];
        for(int i = 0; i < points.length; i++){
            temp[i] = points[i].getY();
        }
        return temp;
    }

    public int numPoints(){
        return points.length;
    }

    public void setColor(Color c){
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    public BoundingBox2D getBounds(){
        int left = Integer.MAX_VALUE;
        int right = -Integer.MAX_VALUE;
        int top = -Integer.MAX_VALUE;
        int bottom = Integer.MAX_VALUE;
        for(Coordinate2D c: points){
            if(c.getX() > right)
                right = c.getX();
            if(c.getX() < left)
                left = c.getX();

            if(c.getY() > top)
                top = c.getY();
            if(c.getY() < bottom)
                bottom = c.getY();
        }
        return new BoundingBox2D(top,bottom,left,right);
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        for(Coordinate2D c: points){
            ret.append((int) c.getX()).append(" ").append((int) c.getY());
            ret.append(" ->\n");
        }
        return ret.toString();
    }
}
