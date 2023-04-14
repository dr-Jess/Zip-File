package render3D;

import javafx.stage.Screen;

import java.awt.*;

public class ScreenPixel implements Comparable<ScreenPixel> {
    private final int x;
    private final int y;
    private final double depth;
    private final Color color;

    ScreenPixel(int x, int y, double depth, Color color){
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.color = color;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public double getDepth(){
        return this.depth;
    }

    public Color getColor(){
        return this.color;
    }

    @Override
    public int compareTo(ScreenPixel o) {
        return Double.compare(o.getDepth(), this.getDepth());
    }
}
