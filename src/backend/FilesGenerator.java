package backend;
import files.*;
import files.File;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FilesGenerator {
    private static ArrayList<java.io.File> imageFiles = new ArrayList<java.io.File>(List.of(new java.io.File("assets" + java.io.File.separator + "image-files").listFiles()));
    private static ArrayList<java.io.File> textFiles = new ArrayList<java.io.File>(List.of(new java.io.File("assets" + java.io.File.separator + "text-files").listFiles()));
    private static ArrayList<String> textNames = new ArrayList<String>(List.of("friend", "bait", "thunder", "slip", "insurance", "start", "act", "juice", "jump", "ink", "dime", "cat", "calculator", "secretary", "love", "fly", "walk", "riddle", "planes", "trip", "wall", "bridge", "reward", "end", "throat", "weather", "cracker", "hobbies", "hair", "spot", "join", "cows", "toothpaste", "pie", "magic", "key", "yak", "vase", "stretch", "smash", "squirrel", "flower", "cherry", "territory", "station", "rhythm", "waves", "decision", "hot", "camp", "rain", "cellar", "popcorn", "sand", "governor", "mine", "pull", "mark", "sofa", "authority", "grandfather", "rail", "development", "scene", "effect", "house", "pollution", "coat", "hall", "van", "weight", "friction", "yarn", "box", "society", "cart", "wash", "plastic", "dinner", "sponge", "stamp", "cause", "kittens", "root", "branch", "air", "mist", "flesh", "activity", "table", "spy", "fireman", "cough", "bedroom", "fire", "shelf", "metal", "children", "behavior", "comparison"));

    /**
     * Generates a branching file system.
     * @param depth the depth of the tree
     * @param widthMin minimum number of non-directory files per parent
     * @param error range of potential width values (error = 3, width anywhere from min to min+3)
     * @param root the root file
     */
    public static void generate(int depth, int widthMin, int error, Directory root){
        //full trinary tree + text gibberish
        generateTree(depth, widthMin, error, root);
        //trimming branches
        Directory[][] layers = {{},{},{},{root}};
        generateLayers(depth-1,layers);
        for(int i = layers.length-1;i>0;i--){
            Directory remove = layers[i-1][(int)(Math.random()*layers[i-1].length)];
            remove.getParent().removeChild(remove);
            generateLayers(depth-1,layers);
        }
        //adding image files
        for(int i = layers.length-2; i>=0; i--){
            int imageCount = layers[i+1].length;
            int startingIndex = (int)(Math.random()*layers[i].length);
            for(int j = 1; j<=imageCount; j++){
                layers[i][(startingIndex+(3*j))%layers[i].length].addChild((Image) fileSelector(FileType.IMAGE,layers[i][(startingIndex+(3*j))%layers[i].length]));
            }
        }
        //adding zip file
        Zip zipFile = new Zip(root,"Unlock me!.zip");
        root.addChild(zipFile);
        //adding solution text files
        int directoriesCount = -1;
        for(Directory[] layer: layers){
            directoriesCount+=layer.length;
        }
        int[] solutionIndices = new int[4];
        for (int i=0;i<4;i++){
            while(solutionIndices[i]!=0){
                int random = (int)(Math.random()*directoriesCount)+1;
                boolean repeat = false;
                for (int j=0;j<i;j++){
                    if (random==solutionIndices[j]){
                        repeat = true;
                    }
                }
                if (!repeat){
                    solutionIndices[i] = random;
                }
            }
        }
        int currentIndex = 1;
        int pinIndex = 0;
        for (int i=layers.length-2;i>=0;i--){
            for (int j=0; j<layers[i].length;j++){
                for (int solutionIndex: solutionIndices){
                    if (currentIndex == solutionIndex){
                        int index = (int) (Math.random() * textNames.size());
                        String name = textNames.get(index) + ".txt";
                        textNames.remove(index);
                        layers[i][j].addChild(new Text(layers[i][j],name, "Character"+(pinIndex+1)+"of solution: "+(zipFile.getPassword().charAt(pinIndex)+"")));
                        pinIndex++;
                    }
                }
                currentIndex++;
            }
        }
    }

    private static void generateTree(int depth, int widthMin, int error, Directory root){
        if(depth>1){
            for (int i = 0; i<3; i++){
                Directory newDirectory = (Directory) fileSelector(FileType.DIRECTORY,root);
                root.addChild(newDirectory);
                generateTree(depth-1,widthMin,error,newDirectory);
            }
            int count = (int)(Math.random()*error)+widthMin;
            for (int i = 0; i<count; i++){
                root.addChild((Text) fileSelector(FileType.TEXT,root));
            }

        }else{
            int count = (int)(Math.random()*error)+widthMin;
            for (int i = 0; i<count; i++){
                root.addChild((Text) fileSelector(FileType.TEXT,root));
            }
        }
    }

    private static void generateLayers(int depth, Directory[][] layers){
        for(Directory branch: layers[depth]){
            for(File leaf: branch.getChildren()){
                if(leaf.getType()==FileType.DIRECTORY){
                    Directory[] newLayer = new Directory[layers[depth-1].length+1];
                    System.arraycopy(layers[depth-1],0,newLayer,0,layers[depth-1].length);
                    newLayer[newLayer.length-1] = (Directory) leaf;
                    layers[depth-1] = newLayer;
                }
            }
        }
        if(depth>1){
            generateLayers(depth-1,layers);
        }
    }

    private static File fileSelector(FileType fileType, Directory parent){
        File returnFile = null;
        switch(fileType){
            case DIRECTORY -> {
                int index = (int)(Math.random()* textNames.size());
                String name = textNames.get(index);
                textNames.remove(index);
                returnFile = new Directory(parent, name);
            }
            case TEXT -> {
                int index = (int) (Math.random() * textNames.size());
                String name = textNames.get(index) + ".txt";
                textNames.remove(index);
                index = (int) (Math.random() * textNames.size());
                String text = null;
                try {
                    text = new String(Files.readAllBytes(textFiles.get(index).toPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textFiles.remove(index);
                returnFile = new Text(parent, name, text);
            }
            case IMAGE -> {
                int index = (int) (Math.random() * textNames.size());
                java.io.File image = imageFiles.get(index);
                String path = image.getPath();
                String name = image.getName();
                imageFiles.remove(index);
                returnFile = new Image(parent,name,path);
            }
        }
        return returnFile;
    }
}
