package game;

import render2D.Coordinate2;
import render3D.Coordinate;
import render3D.Mesh;

import java.awt.*;
import java.util.ArrayList;

public class Scene {
    public static final Coordinate ORIGIN = new Coordinate(0,0,0);
    public static final int FIELD_OF_VIEW = 90;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final double PLANE_DISTANCE_FROM_CAMERA = (0.5 * Scene.SCREEN_WIDTH) / Math.tan(Math.toRadians(0.5 * Scene.FIELD_OF_VIEW));
    public static final Coordinate2 SCREEN_CENTER = new Coordinate2(SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

    private ArrayList<Mesh> objects = new ArrayList<Mesh>();

    public Scene(){

    }

    public void render(Graphics g){

    }

    private void getObjectsInView(){

    }

    public void rotateView(double xRotation, double yRotation, double zRotation){

    }

}
