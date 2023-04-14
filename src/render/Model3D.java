package render;

import java.util.ArrayList;

public class Model3D {
    private final ArrayList<Coordinate> vertices;
    private final ArrayList<Coordinate> vertexNormals;
    private final ArrayList<Coordinate> textureCoordinates;
    private final ArrayList<Polygon> faces;

    private String ID;

    public Model3D(){
        vertices = new ArrayList<>();
        vertexNormals = new ArrayList<>();
        textureCoordinates = new ArrayList<>();
        faces = new ArrayList<>();
        ID = "";
    }

    public void addVertex(Coordinate c){
        this.vertices.add(c);
    }

    public void addVertexNormal(Coordinate c){
        this.vertexNormals.add(c);
    }

    public void addTextureCoordinate(Coordinate c){
        this.textureCoordinates.add(c);
    }

    public void addFace(Polygon p){
        this.faces.add(p);
    }

    public Coordinate getVertex(int index){
        return vertices.get(index);
    }

    public Coordinate getVertexNormal(int index){
        return vertexNormals.get(index);
    }

    public Coordinate getTextureCoordinate(int index){
        return textureCoordinates.get(index);
    }

    public Polygon getFace(int index){
        return faces.get(index);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
