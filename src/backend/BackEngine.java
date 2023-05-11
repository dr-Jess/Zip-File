package backend;

public class BackEngine {
    ViewEngine viewEngine;
    public BackEngine(){
        viewEngine = new ViewEngine(this);
    }

    public void startGame(){
        viewEngine.startGame();
    }

    public static void main(String[] args) {
        new BackEngine();
    }
}
