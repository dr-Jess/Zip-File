package render;

import javax.swing.*;

public class TestFrame extends JFrame {
    public static void main(String[] args) {
        TestPanel p = new TestPanel();
        TestFrame f = new TestFrame();
        f.add(p);
        f.setSize(Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
        p.animate();
    }

}
