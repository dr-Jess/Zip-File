package backend;
import files.*;
import files.File;

import java.io.*;

public class FilesGenerator {
    private static java.io.File[] imageFiles = new java.io.File("assets" + java.io.File.separator + "image-files").listFiles();
    private static java.io.File[] textFiles = new java.io.File("assets" + java.io.File.separator + "text-files").listFiles();
    private static String[][] directoryNames = {{"~"},{},{},{}};

    /**
     * Generates a branching file system.
     * @param depth the depth of the tree
     * @param widthMin minimum number of non-directory files per parent
     * @param error range of potential width values (error = 3, width anywhere from min to min+3)
     * @param root the root file
     */
    public static void generate(int depth, int widthMin, int error, Directory root){
        generateTree(depth, widthMin, error, root);
    }

    private static void generateTree(int depth, int widthMin, int error, Directory root){
        if(depth>1){
            for (int i = 0; i<3; i++){
                Directory newDirectory = (Directory) fileSelector(FileType.DIRECTORY);
                root.addChild(newDirectory);
                generateTree(depth-1,widthMin,error,newDirectory);
            }
            int count = (int)(Math.random()*error)+widthMin;
            for (int i = 0; i<count; i++){
                root.addChild((Text) fileSelector(FileType.TEXT));
            }

        }else{
            int count = (int)(Math.random()*error)+widthMin;
            for (int i = 0; i<count; i++){
                root.addChild((Text) fileSelector(FileType.TEXT));
            }
        }
    }

    private static File fileSelector(FileType fileType){
        switch(fileType){
            case DIRECTORY:
                break;
        }
        return null;
    }
}
