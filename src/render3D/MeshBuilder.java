package render3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import render2D.Coordinate2D;

import javax.imageio.ImageIO;

public class MeshBuilder {
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Vector> vectors = new ArrayList<>();
    ArrayList<Coordinate2D> vertexTextures = new ArrayList<>();
    ArrayList<MeshPolygon> faces = new ArrayList<>();

    public Mesh readOBJ(String path){
        reset();
        Scanner scanner;
        try{
            scanner = new Scanner(new File(path));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            String[] args = command.split(" ");
            switch(args[0]){
                case "v":
                    double x = Double.parseDouble(args[1]);
                    double y = Double.parseDouble(args[2]);
                    double z = Double.parseDouble(args[3]);
                    addVertex(new Vertex(x, y, z));
                    break;
                case "vn":
                    double nx = Double.parseDouble(args[1]);
                    double ny = Double.parseDouble(args[2]);
                    double nz = Double.parseDouble(args[3]);
                    addVertexNormal(new Vector(nx, ny, nz));
                    break;
                case "vt":
                    double tx = Double.parseDouble(args[1]);
                    double ty = Double.parseDouble(args[2]);
                    addTextureCoordinate(new Coordinate2D(0,0));
                    break;
                case "f":
                    Vertex[] points = new Vertex[args.length - 1];
                    for(int i = 1; i < args.length; i++){
                        String[] args1 = args[i].split("/");
                        Vertex v = vertices.get(Integer.parseInt(args1[0])-1);
                        Coordinate2D vt;
                        Vector vn = vectors.get(Integer.parseInt(args1[2])-1);
                        v.setNormal(vn);
                        points[i-1] = v;
                    }
                    MeshPolygon p = new MeshPolygon(points, Coordinate.ORIGIN);
                    p.setColor(new Color(0,200,0));
                    addFace(p);
                    break;
            }
        }
        Mesh m = new Mesh(faces, Coordinate.ORIGIN);
        m.incrementRotation(-Math.PI/2,0,0);

        return m;
    }

    public Mesh readOBJ(String path, String texturePath){
        reset();
        Scanner scanner;
        BufferedImage texture;
        try{
            scanner = new Scanner(new File(path));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
        try{
            texture = ImageIO.read(new File(texturePath));
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            String[] args = command.split(" ");
            switch(args[0]){
                case "v":
                    double x = Double.parseDouble(args[1]);
                    double y = Double.parseDouble(args[2]);
                    double z = Double.parseDouble(args[3]);
                    addVertex(new Vertex(x, y, z));
                    break;
                case "vn":
                    double nx = Double.parseDouble(args[1]);
                    double ny = Double.parseDouble(args[2]);
                    double nz = Double.parseDouble(args[3]);
                    addVertexNormal(new Vector(nx, ny, nz));
                    break;
                case "vt":
                    double tx = Double.parseDouble(args[1]) * texture.getWidth();
                    double ty = Double.parseDouble(args[2]) * texture.getHeight();
                    if(tx >= texture.getWidth())
                        tx = texture.getHeight() - 1;
                    addTextureCoordinate(new Coordinate2D((int) tx, (int) ty));
                    break;
                case "f":
                    Vertex[] points = new Vertex[args.length - 1];
                    Coordinate2D[] colorRefs = new Coordinate2D[args.length - 1];
                    for(int i = 1; i < args.length; i++){
                        String[] args1 = args[i].split("/");
                        Vertex v = vertices.get(Integer.parseInt(args1[0])-1);
                        Coordinate2D vt = vertexTextures.get(Integer.parseInt(args1[1])-1);
                        Vector vn = vectors.get(Integer.parseInt(args1[2])-1);
                        v.setNormal(vn);
                        points[i-1] = v;
                        colorRefs[i-1] = vt;
                    }
                    MeshPolygon p = new MeshPolygon(points, Coordinate.ORIGIN);
                    int red = 0;
                    int green = 0;
                    int blue = 0;
                    for(Coordinate2D c: colorRefs){
                        Color temp = new Color(texture.getRGB(c.getX(), c.getY()));
                        red += temp.getRed();
                        green += temp.getGreen();
                        blue += temp.getBlue();
                    }
                    p.setColor(new Color(
                            red / colorRefs.length,
                            green / colorRefs.length,
                            blue / colorRefs.length
                    ));
                    addFace(p);
                    break;
            }
        }
        ArrayList<MeshPolygon> unique = new ArrayList<>();
        ArrayList<MeshPolygon> blacklist = new ArrayList<>();
        for(MeshPolygon mp: faces){
            if(!blacklist.contains(mp)){
                unique.add(mp);
                blacklist.add(mp);
            }else{
                unique.remove(mp);
                blacklist.add(mp);
            }
        }
        return new Mesh(unique, Coordinate.ORIGIN);
    }

    public void addVertex(Vertex v){
        vertices.add(v);
    }

    public void addVertexNormal(Vector v){
        vectors.add(v);
    }

    public void addTextureCoordinate(Coordinate2D c){
        vertexTextures.add(c);
    }

    public void addFace(MeshPolygon mp){
        faces.add(mp);
    }

    public void reset(){
        vertices = new ArrayList<>();
        vectors = new ArrayList<>();
        vertexTextures = new ArrayList<>();
        faces = new ArrayList<>();
    }
}
