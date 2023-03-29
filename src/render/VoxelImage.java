package render;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class VoxelImage {
    private Coordinate[] vertices;
    private Polygon[] faces;

    public VoxelImage(String objFilePath, String mtlFilePath){

        this.faces = parseOBJ(objFilePath);
    }

    /**
     *
     */
    private Polygon[] parseOBJ(String filePath){
        String[] data = readFileData(filePath);
        Model3D model = new Model3D();
        for(String line: data){
            String[] command = line.split(" ");
            parseAction(command, model);
        }
        return null;
    }

    private String[] readFileData(String filePath){
        String[] data = null;
        try{
            String text = new String(Files.readAllBytes(Paths.get(filePath)));
            data = text.split("\n");
        }catch(IOException e){
            ;
        }
        return data;
    }

    /**
     * Takes a .obj command and parses the input according to
     * the command given in input[0]. There's probably a better
     * way to do this.
     */
    private void parseAction(String[] input, Model3D model){
        switch(input[0]){
            case "o":
                model.setID(input[1]);
                break;
            case "v":
                double x = Double.parseDouble(input[1]);
                double y = Double.parseDouble(input[2]);
                double z = Double.parseDouble(input[3]);
                model.addVertex(new Coordinate(x,y,z));
                break;
            case "vt":
                double tx = Double.parseDouble(input[1]);
                double ty = Double.parseDouble(input[2]);
                model.addTextureCoordinate(new Coordinate(tx,ty,0));
                break;
            case "vn":
                double nx = Double.parseDouble(input[1]);
                double ny = Double.parseDouble(input[2]);
                double nz = Double.parseDouble(input[3]);
                model.addVertexNormal(new Coordinate(nx,ny,nz));
                break;
            case "f":
                int numPoints = input.length - 1;
                Coordinate[] points = new Coordinate[numPoints];
                for(int i = 1; i < input.length; i++){
                    String[] data = input[i].split("/");
                    int vertex = Integer.parseInt(data[0]);
                    points[i - 1] = model.getVertex(vertex);
                }
                model.addFace(new Polygon(points,new Coordinate(0,0,0)));
                break;
            case "l":
                break;
            default:
                break;
        }

    }    /**
     * Calculates the 'center of mass' of the model
     */
    private Coordinate calculateCentroid(Coordinate[] vertices){
        return null;
    }

    private void render(Graphics g){

    }


}
