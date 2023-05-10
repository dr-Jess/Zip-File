package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HomeScreen extends JPanel implements MouseListener, MouseMotionListener {
    private double titleY = 0.0;
    BufferedImage title;
    BufferedImage shadow;
    BufferedImage click;
    public HomeScreen(){
        setSize(Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        try {
            title = ImageIO.read(new File(".\\assets\\title.png"));
            shadow = ImageIO.read(new File(".\\assets\\shadow.png"));
            click = ImageIO.read(new File(".\\assets\\click.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Timer t = new Timer(1,new ActionListener(){
            double t = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                t += 0.05;//+0.05*Math.abs(Math.sin(t));
                titleY = Math.sin(t);
                t %= Math.PI*2;
                repaint();
            }
        });
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Courier New", Font.BOLD, 30));
        g.clearRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        int sx = (Scene.SCREEN_WIDTH - shadow.getWidth())/2+10;
        int sy = (Scene.SCREEN_HEIGHT - shadow.getHeight())/2-20;
        g.drawImage(shadow,sx,sy,null);
        int tx = (Scene.SCREEN_WIDTH - title.getWidth())/2;
        int ty = (Scene.SCREEN_HEIGHT - title.getHeight())/2-60;
        g.drawImage(title,tx,ty+(int)(20*titleY),null);
        int cx = (Scene.SCREEN_WIDTH - click.getWidth())/2;
        int cy = (Scene.SCREEN_HEIGHT - click.getHeight())/2+240-(int)(8*titleY);
        //g.drawImage(click,cx,cy,null);
        g.setColor(new Color(220,220,220));

        g.drawString("click anywhere to continue", cx-30,cy+15);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        repaint();
    }
}
