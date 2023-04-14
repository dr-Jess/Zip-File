package render;

import java.util.ArrayList;

public class Entity {
    private boolean collision = false;
    private ArrayList<Polygon> faces;

    public void rotateAbout(Coordinate origin, Coordinate other, double xRotation, double yRotation, double zRotation){
        for(Polygon p: faces){
            //p.rotateAbout(origin, other, xRotation, yRotation, zRotation);
        }
    }
}
