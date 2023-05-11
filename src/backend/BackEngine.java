package backend;
import files.*;


public class BackEngine {
    private ViewEngine viewEngine;
    private Directory root = new Directory("Home");
    private String password;
    private long startTime;
    public BackEngine(){
        viewEngine = new ViewEngine(this);
    }

    public Directory getRoot(){
        return root;
    }

    public void startGame(){
        password = FilesGenerator.generate(4,2,1,root);
        startTime = System.currentTimeMillis();
        viewEngine.startGame();
    }

    public long getTime(){
        return System.currentTimeMillis()-startTime;
    }

    public String getPassword(){
        return password;
    }

    public void endGame(){
        viewEngine.endGame(System.currentTimeMillis()-startTime);
    }

    public static void main(String[] args) {
        new BackEngine();
    }
}
