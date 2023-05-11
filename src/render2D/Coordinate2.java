package render2D;

public class Coordinate2 {
    private final int x;
    private final int y;

    public Coordinate2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String toString(){
        return this.x + " " + this.y;
    }
}
