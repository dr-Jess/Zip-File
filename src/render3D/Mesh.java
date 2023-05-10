package render3D;

import game.Scene;
import render2D.BoundingBox2;
import render2D.Polygon2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Mesh {
    private final MeshPolygon[] polygons;
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
        paintersAlgorithm(g);
        //zBuffer(g);
        //zBufferScale(g);
        //zBufferLines(g);

    }

    public void paintersAlgorithm(Graphics g){
        ArrayList<MeshPolygon> buffered = sortPolygons();

        for(int i = buffered.size()-1; i > -1; i--){
            Coordinate avg = buffered.get(i).getAveragePoint();
            Vector ref = new Vector(Coordinate.ORIGIN, avg);
            double check = buffered.get(i).getNormal().dotProduct(ref);
            //buffered.get(i).getNormal().getDz() <= 0
            if(check <= 0) {
                Polygon2 p2d = buffered.get(i).translateToCameraView();
                g.setColor(p2d.getColor());
                g.fillPolygon(p2d.getXPoints(), p2d.getYPoints(), p2d.numPoints());
                g.drawPolygon(p2d.getXPoints(), p2d.getYPoints(), p2d.numPoints());
            }
        }
    }

    public ArrayList<MeshPolygon> sortPolygons() {
        Arrays.sort(polygons);
        return new ArrayList<>(Arrays.asList(polygons));
    }

    double[][] zDepths = new double[Scene.SCREEN_WIDTH][Scene.SCREEN_HEIGHT];
    Color[][] screen = new Color[Scene.SCREEN_WIDTH][Scene.SCREEN_HEIGHT];


    public void zBuffer(Graphics g){
        double centerX = Scene.SCREEN_CENTER.getX();
        double centerY = Scene.SCREEN_CENTER.getY();


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

            Polygon2 translated = p.translateToCameraView();
            BoundingBox bounds = p.getBounds();
            BoundingBox2 bounds2D = translated.getBounds();
            double xMin = bounds.getLeft();
            double xMax = bounds.getRight();
            double xIncrement = 1 * (xMax - xMin) / ((Operator.limitNum(bounds2D.getRight(), 0, Scene.SCREEN_WIDTH) - Operator.limitNum(bounds2D.getLeft(), 0, Scene.SCREEN_WIDTH))+1);
            //xIncrement = 1;
            double yMin = bounds.getBottom();
            double yMax = bounds.getTop();
            double yIncrement = 1 * (yMax - yMin) / ((Operator.limitNum(bounds2D.getTop(), 0, Scene.SCREEN_HEIGHT) - Operator.limitNum(bounds2D.getBottom(), 0, Scene.SCREEN_HEIGHT))+1);
            //yIncrement = 1;
            double zMax = bounds.getFront();

            if(faceNormal.dotProduct(new Vector(Coordinate.ORIGIN,avg)) <= 0)
            if ((bounds2D.getRight() >= 0 || bounds2D.getLeft() <= Scene.SCREEN_WIDTH) && (bounds2D.getBottom() >= 0 || bounds2D.getTop() <= Scene.SCREEN_HEIGHT)) {
                if (zMax > 0) {
                    if(!Operator.almostEqual(xIncrement,0,1e-4))
                    for (double x = xMin; x < xMax; x += xIncrement) {
                        if(!Operator.almostEqual(yIncrement,0,1e-4))
                        for (double y = yMin; y < yMax; y += yIncrement) {
                            double zDepth = cz - (dx * (x - cx) + dy * (y - cy)) / dz;
                            if (zDepth >= 0) {
                                Coordinate check = new Coordinate(x, y, 0);
                                if (p.intersects(check)) {
                                    double zMod = 1 / (zDepth + 1);
                                    int screenX = (int) Math.round(x * Scene.PLANE_DISTANCE_FROM_CAMERA * zMod + centerX);
                                    int screenY = (int) Math.round(y * Scene.PLANE_DISTANCE_FROM_CAMERA * zMod + centerY);

                                    if (0 < screenX && screenX < Scene.SCREEN_WIDTH && 0 < screenY && screenY < Scene.SCREEN_HEIGHT) {
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

    public void zBufferScale(Graphics g){
        double centerX = Scene.SCREEN_CENTER.getX();
        double centerY = Scene.SCREEN_CENTER.getY();


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

            Polygon2 translated = p.translateToCameraView();
            BoundingBox bounds = p.getBounds();
            BoundingBox2 bounds2D = translated.getBounds();
            double xMin = bounds.getLeft();
            double xMax = bounds.getRight();
            double xIncrement = 0.1;// * (xMax - xMin) / ((Operator.limitNum(bounds2D.getRight(), 0, Scene.SCREEN_WIDTH) - Operator.limitNum(bounds2D.getLeft(), 0, Scene.SCREEN_WIDTH))+1);
            //xIncrement = 1;
            double yMin = bounds.getBottom();
            double yMax = bounds.getTop();
            double yIncrement = 0.1;// * (yMax - yMin) / ((Operator.limitNum(bounds2D.getTop(), 0, Scene.SCREEN_HEIGHT) - Operator.limitNum(bounds2D.getBottom(), 0, Scene.SCREEN_HEIGHT))+1);
            //yIncrement = 1;
            double zMax = bounds.getFront();

            if(faceNormal.dotProduct(new Vector(Coordinate.ORIGIN,avg)) <= 0)
                if ((bounds2D.getRight() >= 0 || bounds2D.getLeft() <= Scene.SCREEN_WIDTH) && (bounds2D.getBottom() >= 0 || bounds2D.getTop() <= Scene.SCREEN_HEIGHT)) {
                    if (zMax > 0) {
                        if(!Operator.almostEqual(xIncrement,0,1e-4))
                            for (double x = xMin; x < xMax; x += xIncrement) {
                                if(!Operator.almostEqual(yIncrement,0,1e-4))
                                    for (double y = yMin; y < yMax; y += yIncrement) {
                                        double zDepth = cz - (dx * (x - cx) + dy * (y - cy)) / dz;
                                        if (zDepth >= 0) {
                                            Coordinate check = new Coordinate(x, y, 0);
                                            if (p.intersects(check)) {
                                                double zMod = 0.8 / (zDepth + 1);
                                                int pixelSize = (int) (30/(0.1*zDepth+1)+1);
                                                //modify array to do pixel size
                                                int screenX = (int) Math.round(x * Scene.PLANE_DISTANCE_FROM_CAMERA * zMod + centerX);
                                                int screenY = (int) Math.round(y * Scene.PLANE_DISTANCE_FROM_CAMERA * zMod + centerY);

                                                if (0 < screenX && screenX < Scene.SCREEN_WIDTH && 0 < screenY && screenY < Scene.SCREEN_HEIGHT) {
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
                }
        }
        int pixelSize = (int) (30/(0.1*center.getZ()+1)+1);
        for(int x = 0; x < screen.length; x+=pixelSize+1){
            for(int y = 0; y < screen[0].length; y+=pixelSize+1){
                if(screen[x][y] != null) {
                    g.setColor(screen[x][y]);
                    g.fillPolygon(new int[]{x-pixelSize,x-pixelSize,x+pixelSize,x+pixelSize},
                            new int[]{y-pixelSize,y+pixelSize,y+pixelSize,y-pixelSize},
                            4);
                }
                zDepths[x][y] = Double.MAX_VALUE;
                screen[x][y] = null;
            }
        }

    }

    public void zBufferLines(Graphics g){
        double centerX = Scene.SCREEN_CENTER.getX();
        double centerY = Scene.SCREEN_CENTER.getY();


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

            Polygon2 translated = p.translateToCameraView();
            BoundingBox bounds = p.getBounds();
            BoundingBox2 bounds2D = translated.getBounds();
            double xMin = bounds.getLeft();
            double xMax = bounds.getRight();
            double xIncrement = 1.2 * (xMax - xMin) / (Operator.limitNum(bounds2D.getRight(), 0, Scene.SCREEN_WIDTH) - Operator.limitNum(bounds2D.getLeft(), 0, Scene.SCREEN_WIDTH));

            double yMin = bounds.getBottom();
            double yMax = bounds.getTop();
            double yIncrement = 1.2 * (yMax - yMin) / (Operator.limitNum(bounds2D.getTop(), 0, Scene.SCREEN_HEIGHT) - Operator.limitNum(bounds2D.getBottom(), 0, Scene.SCREEN_HEIGHT));

            double zMax = bounds.getFront();
            if(faceNormal.dotProduct(new Vector(Coordinate.ORIGIN,avg)) <= 0)
                if ((bounds2D.getRight() >= 0 || bounds2D.getLeft() <= Scene.SCREEN_WIDTH) && (bounds2D.getBottom() >= 0 || bounds2D.getTop() <= Scene.SCREEN_HEIGHT)) {
                    if (zMax > 0) {
                        if(!Operator.almostEqual(xIncrement,0,1e-4))
                            for (double x = xMin; x < xMax; x += xIncrement) {
                                if(!Operator.almostEqual(yIncrement,0,1e-4))
                                    for (double y = yMin; y < yMax; y += yIncrement) {
                                        double zDepth = cz - (dx * (x - cx) + dy * (y - cy)) / dz;
                                        if (zDepth >= 0) {
                                            Coordinate check = new Coordinate(x, y, 0);
                                            if (p.intersects(check)) {
                                                double zMod = 1 / (zDepth + 1);
                                                int screenX = (int) Math.round(x * Scene.PLANE_DISTANCE_FROM_CAMERA * 0.001 + centerX);
                                                int screenY = (int) Math.round(y * Scene.PLANE_DISTANCE_FROM_CAMERA * 0.001 + centerY);

                                                if (0 < screenX && screenX < Scene.SCREEN_WIDTH && 0 < screenY && screenY < Scene.SCREEN_HEIGHT) {
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
                }
        }


        for(int x = 0; x < screen.length; x++){
            Color color = null;
            int yStart = -1;
            for(int y = 0; y < screen[0].length; y++){
                if(screen[x][y] != null) {
                    if(yStart == -1){
                        yStart = y;
                        color = screen[x][y];
                    }
                    if(!screen[x][y].equals(color)){
                        g.setColor(color);
                        g.drawLine(x,yStart,x,y-1);
                        yStart = y;
                        color = screen[x][y];
                    }
                }
                else if (color != null){
                    g.setColor(color);
                    g.drawLine(x,yStart,x,y-1);
                    yStart = -1;
                    color = screen[x][y];
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
