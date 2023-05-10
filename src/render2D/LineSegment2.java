package render2D;

public class LineSegment2 {
    public Coordinate2 start;
    public Coordinate2 end;

    public LineSegment2(Coordinate2 start, Coordinate2 end){
        this.start = start;
        this.end = end;
    }

    public Coordinate2 getStart(){
        return start;
    }

    public Coordinate2 getEnd(){
        return end;
    }
}
