package backend;
import files.*;

public class BackEngine {
    private ViewEngine viewEngine;
    private Directory root = new Directory("Home");
    private String password;
    public BackEngine(){
        viewEngine = new ViewEngine(this);
    }

    public Directory getRoot(){
        return root;
    }

    public void startGame(){
        password = FilesGenerator.generate(4,2,1,root);
        viewEngine.startGame();
    }

    public String getPassword(){
        return password;
    }

    public void endGame(){

    }

    public static void main(String[] args) {
        new BackEngine();
    }
}
