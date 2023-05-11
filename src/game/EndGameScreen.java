package game;

import backend.BackEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EndGameScreen extends JPanel{
    private double titleY = 0.0;
    BufferedImage title;
    BufferedImage shadow;
    String stopWatch;
    BackEngine backEngine;
    float endOpacity = 1F;
    long time;
    public EndGameScreen(long time, BackEngine backEngine){
        this.backEngine = backEngine;
        this.setFocusable(true);
        this.time = time;
        try {
            title = ImageIO.read(new File(".\\assets\\win.png"));
            shadow = ImageIO.read(new File(".\\assets\\shadow.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.time = backEngine.getTime();
        int minutes = (int) (time/60000);
        int seconds = (int)((time%60000)/1000);
        int millis = (int) (((time%60000)%1000));
        String minuteS = minutes + ":";
        String secondS = seconds + ":";
        if (seconds<10){
            secondS = "0" + secondS;
        }
        String milliS = millis + "";
        if (millis<10){
            milliS = "00" + millis;
        } else if (millis<100) {
            milliS = "0" + millis;
        }
        stopWatch = "Your time: " + minuteS + secondS + milliS;
        startFade();
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
    private void startFade(){
        final Timer frameTimer = new Timer(1000/60,null);
        frameTimer.addActionListener(new ActionListener() {
            double opacityRemaining = 1;
            double lastTime = System.nanoTime();
            @Override
            public void actionPerformed(ActionEvent e) {
                double currentTime = System.nanoTime();
                opacityRemaining-=(currentTime-lastTime)/500000000;
                if(opacityRemaining<=0){
                    frameTimer.stop();
                    opacityRemaining = 0;
                    endOpacity = 0;
                    repaint();
                }
                endOpacity= (float) (opacityRemaining);
                repaint();
                lastTime = currentTime;
            }
        });
        frameTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(10,10,10));
        g.clearRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        g.fillRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        int sx = (Scene.SCREEN_WIDTH - shadow.getWidth())/2+10;
        int sy = (Scene.SCREEN_HEIGHT - shadow.getHeight())/2-20;
        g.drawImage(shadow,sx,sy,null);
        int tx = (Scene.SCREEN_WIDTH - title.getWidth())/2;
        int ty = (Scene.SCREEN_HEIGHT - title.getHeight())/2-50;
        g.drawImage(title,tx,ty+(int)(20*titleY),null);
        g.setFont(new Font("Courier New", Font.BOLD, 30));
        g.setColor(Color.white);
        FontMetrics metrics = g.getFontMetrics(new Font("Courier New", Font.BOLD, 30));
        int cx = (Scene.SCREEN_WIDTH - metrics.stringWidth(stopWatch))/2;
        int cy = (Scene.SCREEN_HEIGHT - metrics.getHeight())/2+270;
        g.drawString(stopWatch,cx,cy-(int)(4*titleY));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,endOpacity));
        g2d.fillRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
    }

}
