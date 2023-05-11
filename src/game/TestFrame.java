package game;

import files.Directory;
import files.File;
import files.Image;
import files.Text;

import javax.swing.*;

public class TestFrame extends JFrame {
    public static void main(String[] args) {
        HomeScreen h = new HomeScreen();
        TestPanel p = new TestPanel();
        TestFrame f = new TestFrame();
        Directory d = new Directory("root");
        Directory d1 = new Directory(d,"d1");
        Directory d2= new Directory(d1,"d2");
        d.addChild(d1);
        d1.addChild(d2);
        GamePlayScreen g = new GamePlayScreen(new File[]{
                d,
                new Image(d,"image",""),
                new Image(d,"image4",""),
                new Text(d, "text","")
        });
        f.addKeyListener(g);
        f.add(g);
        f.setSize(Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
        p.animate();
    }

}
