package render;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class TestPanel extends JPanel {
    double close = 100;
    int x = 0;
    int y = 0;

    int z = 0;
    int rad = 10;

    double rotationSpeed = 0.0;
    double far = close+rad;
    Polygon p;
    Polyhedron poly = new Polyhedron(Polyhedron.Shape.CUBE, rad, new Coordinate(0,0,close + rad/2));

    PixelImage pi;


    public TestPanel(){
        p = new Polygon(new Coordinate[]{
                new Coordinate(-rad,rad,100),
                new Coordinate(-rad,-rad,100),
                new Coordinate(rad,-rad,100),
                new Coordinate(rad,rad,100)},
                new Coordinate(0,0,100));
        final JSlider xSlider = new JSlider();
        xSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                x = xSlider.getValue();
                poly.moveTo(new Coordinate((xSlider.getMaximum()/2-x),poly.getCenter().getY(),poly.getCenter().getZ()));
                pi.moveTo(new Coordinate((xSlider.getMaximum()/2-x),pi.getCenter().getY(),pi.getCenter().getZ()));
            }
        });
        this.add(xSlider);
        final JSlider zSlider = new JSlider();
        zSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                z = zSlider.getValue();
                poly.moveTo(new Coordinate(poly.getCenter().getX(),poly.getCenter().getY(),z*5));
                pi.moveTo(new Coordinate(poly.getCenter().getX(),poly.getCenter().getY(),z*5));
            }
        });
        this.add(zSlider);
        final JSlider rotationSlider = new JSlider();
        rotationSlider.setSnapToTicks(true);
        rotationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rotationSpeed = (rotationSlider.getMaximum()/2-rotationSlider.getValue())/100.0;
                rotationSpeed = (rotationSlider.getMaximum()/2-rotationSlider.getValue())/100.0;
            }
        });
        this.add(rotationSlider);
        try{
            pi = new PixelImage(ImageIO.read(new File("C:\\Users\\jonat\\Downloads\\pixil-frame-0 (8).png")), new Coordinate(0,0,close + rad/2));
        }catch(Exception e){}
    }
    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        //pi.render(g);
        /*
        BufferedImage b = null;
        try{
            b = ImageIO.read(new File("C:\\Users\\jonat\\Downloads\\pixil-frame-0 (8).png"));
        } catch(Exception e){
            System.out.println("AA");
        }
        for(int i = 0; i < b.getWidth(); i++){
            for(int j = 0; j < b.getHeight(); j++){
                if(b.getRGB(i,j) != 0) {
                    g.setColor(new Color((b.getRGB(i, j))));
                    int x = i * 20;
                    int y = j * 20;
                    System.out.println(x + " " + y);
                    int[] xp = {0, 1000, 1000, 0};
                    int[] yp = {0, 0, 1000, 1000};
                    g.fillPolygon(new int[]{x, x + 20, x + 20, x}, new int[]{y, y, y + 20, y + 20}, 4);
                }
            }
        }

         */
        g.setColor(Color.BLACK);
        //poly.render(g);

    }

    public void animate(){
        final TestPanel testPanel = this;
        Timer timer = new Timer(2, new ActionListener() {
            double v = 0;
            int xV = 0;
            int yV = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(poly.getCenter().getZ() + v < 0 || poly.getCenter().getZ() + v > 100+rad*3) {
                    v = -v;
                }
                close += v;
                far += v;
                if(x < -30 || x > 30)
                    xV = -xV;
                if(y < -100 || y > 100)
                    yV = -yV;
                x += xV;
                y += yV;
                //poly.moveTo(poly.getCenter().translatedBy(xV,yV,v));
                p.incrementRotation(0,0.02,0.02);
                poly.incrementRotation(0,0.02 * rotationSpeed,0.02 * rotationSpeed);
                pi.incrementRotation(0,0.02 * rotationSpeed,0.02 * rotationSpeed);
                testPanel.repaint();
            }
        });
        timer.start();
    }
}
