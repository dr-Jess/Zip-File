package game;

import backend.BackEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HomeScreen extends JPanel{
    private double titleY = 0.0;
    BackEngine backEngine;
    BufferedImage title;
    BufferedImage shadow;
    BufferedImage click;
    private float endOpacity = 0;
    public HomeScreen(BackEngine backEngine){
        this.backEngine = backEngine;
        this.setFocusable(true);
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
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                endFade();
            }
        });
    }

    private void endFade(){
        final Timer frameTimer = new Timer(1000/60,null);
        frameTimer.addActionListener(new ActionListener() {
            double opacityRemaining = 1;
            double lastTime = System.nanoTime();
            @Override
            public void actionPerformed(ActionEvent e) {
                double currentTime = System.nanoTime();
                opacityRemaining-=(currentTime-lastTime)/500000000;
                if(opacityRemaining<=-2){
                    opacityRemaining = 0;
                    endOpacity = 1;
                    repaint();
                    frameTimer.stop();
                    backEngine.startGame();
                }
                endOpacity= (float) (1-opacityRemaining);
                repaint();
                lastTime = currentTime;
            }
        });
        frameTimer.start();
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,clamp(endOpacity, 0, 1)));
        g2d.fillRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
    }

    private float clamp(float val, float min, float max) {
        if(val < min) return min;
        else if(val > max) return max;
        else return val;
    }
}
