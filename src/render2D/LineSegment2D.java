package render2D;

public class LineSegment2D {
    public Coordinate2D start;
    public Coordinate2D end;

    public LineSegment2D(Coordinate2D start, Coordinate2D end){
        this.start = start;
        this.end = end;
    }

    public Coordinate2D getStart(){
        return start;
    }

    public Coordinate2D getEnd(){
        return end;
    }
}
