package render2D;

import render3D.Coordinate;
import render3D.LineSegment;
import render3D.Operator;

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

    public boolean onLine(Coordinate2 check){
        if (check.getX() <= Math.max(start.getX(), end.getX()) && check.getX() >= Math.min(start.getX(), end.getX()) &&
                check.getY() <= Math.max(start.getY(), end.getY()) && check.getY() >= Math.min(start.getY(), end.getY()))
            return true;
        return false;
    }

    public boolean intersect2D(LineSegment2 other){
        Coordinate2 otherStart = other.getStart();
        Coordinate2 otherEnd = other.getEnd();

        int turn1 = tripletOrientation2D(start, otherEnd, otherStart);
        int turn2 = tripletOrientation2D(start, end, otherEnd);
        int turn3 = tripletOrientation2D(otherStart, otherEnd, start);
        int turn4 = tripletOrientation2D(otherStart, otherEnd, end);

        if (turn1 != turn2 && turn3 != turn4)
            return true;

        if (turn1 == 0 && this.onLine(otherStart))
            return true;

        if (turn2 == 0 && this.onLine(otherEnd))
            return true;

        if (turn3 == 0 && other.onLine(start))
            return true;

        if (turn4 == 0 && other.onLine(end))
            return true;

        return false;
    }

    /*
    returns direction of triplet ABC:
        0 if collinear
        1 if clockwise
        -1 if counterclockwise
     */
    private int tripletOrientation2D(Coordinate2 a, Coordinate2 b, Coordinate2 c){
        double turn = (b.getY() - a.getY()) * (c.getX()-b.getX()) -
                (b.getX() - a.getX()) * (c.getY() - b.getY());
        if (Operator.almostEqual(turn,0,1e-4))
            return 0;
        return (int) Math.signum(turn);
    }


    public boolean isEndpoint(Coordinate2 c){
        return c.equals(start) || c.equals(end);
    }
    public boolean meetsAtEndpoint(LineSegment2 other){
        return other.isEndpoint(this.start) || other.isEndpoint(this.end);
    }
}
