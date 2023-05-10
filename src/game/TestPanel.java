package game;

import render3D.*;
import render3D.Polygon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class TestPanel extends JPanel {
    double close = 100;
    int x = 0;
    int y = 0;

    int z = 0;
    int rad = 10;

    double xrotationSpeed = 0.0;
    double zrotationSpeed = 0.0;
    double far = close+rad;
    render3D.Polygon p;

    //Mesh m = new MeshBuilder().readOBJ("C:\\Users\\jonat\\Desktop\\obj\\textFile3.obj","C:\\Users\\jonat\\Desktop\\obj\\textfilepalette.png");
    //Mesh m = new MeshBuilder().readOBJ("C:\\Users\\jonat\\Desktop\\obj\\model_out.obj");
    Mesh m = new Mesh(
            new MeshPolygon[]{
                    new MeshPolygon(
                            new Vertex[]{
                                    new Vertex(1,1,100,new Vector(0,0,-1)),
                                    new Vertex(1,-1,100,new Vector(0,0,-1)),
                                    new Vertex(-1,-1,100,new Vector(0,0,-1)),
                                    new Vertex(-1,1,100,new Vector(0,0,-1))
                            }
                            ,new Coordinate(0,0,100)
                    )
            },
            new Coordinate (0,0,100)
            );
    public TestPanel(){
        p = new Polygon(new Coordinate[]{
                new Coordinate(-rad,rad,100),
                new Coordinate(-rad,-rad,100),
                new Coordinate(rad,-rad,100),
                new Coordinate(rad,rad,100)},
                new Coordinate(0,0,100));
        final JSlider ySlider = new JSlider();
        ySlider.setSnapToTicks(true);
        ySlider.setMajorTickSpacing(10);
        ySlider.setPaintTicks(true);
        ySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                m.moveTo(new Coordinate(m.getCenter().getX(),(ySlider.getMaximum()/2-x),m.getCenter().getZ()));
                x = ySlider.getValue();
                System.out.println("xslider " + x );
            }
        });
        this.add(ySlider);
        final JSlider zSlider = new JSlider();
        zSlider.setSnapToTicks(true);
        zSlider.setMajorTickSpacing(10);
        zSlider.setPaintTicks(true);
        zSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                z = zSlider.getValue();
                m.moveTo(new Coordinate(m.getCenter().getX(),m.getCenter().getY(),z*5));
                System.out.println("zslider " + z*5);
            }
        });
        this.add(zSlider);

        final JSlider xrotationSlider = new JSlider();
        xrotationSlider.setSnapToTicks(true);
        xrotationSlider.setMajorTickSpacing(10);
        xrotationSlider.setPaintTicks(true);
        xrotationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                xrotationSpeed = (xrotationSlider.getMaximum()/2-xrotationSlider.getValue())/600.0;
            }
        });
        this.add(xrotationSlider);

        final JSlider zrotationSlider = new JSlider();
        zrotationSlider.setSnapToTicks(true);
        zrotationSlider.setMajorTickSpacing(10);
        zrotationSlider.setPaintTicks(true);
        zrotationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                zrotationSpeed = (zrotationSlider.getMaximum()/2-zrotationSlider.getValue())/600.0;
            }
        });
        this.add(zrotationSlider);

        final JComboBox<String> modelChoice = new JComboBox<>(new String[]{"TextFile","Horse","Frog","Pikachu","Shark"});

        modelChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) modelChoice.getSelectedItem();
                switch(s){
                    case "Shark":
                        m = new MeshBuilder().readOBJ("C:\\Users\\jonat\\Desktop\\obj\\model_out1.obj");
                        m.moveTo(new Coordinate(0,40,200));
                        break;
                    case "Frog":
                        m = new MeshBuilder().readOBJ("C:\\Users\\jonat\\Desktop\\obj\\model_out2.obj");
                        m.moveTo(new Coordinate(0,40,200));
                        break;
                    case "Horse":
                        m = new MeshBuilder().readOBJ("C:\\Users\\jonat\\Desktop\\obj\\model_out3.obj");
                        m.moveTo(new Coordinate(0,40,200));
                        break;
                    case "Pikachu":
                        m = new MeshBuilder().readOBJ("C:\\Users\\jonat\\Desktop\\obj\\model_out4.obj");
                        m.moveTo(new Coordinate(0,40,200));
                        break;
                    case "TextFile":
                        m = new MeshBuilder().readOBJ(".\\assets\\textfile.obj","C:\\Users\\jonat\\Desktop\\obj\\textfilepalette.png");
                        m.moveTo(new Coordinate(0,0,200));
                        break;
                }
            }
        });
        this.add(modelChoice);

        m.moveTo(new Coordinate(0,0,200));
    }
    @Override
    protected void paintComponent(Graphics g) {
        //g.clearRect(0,0, Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        //pi.g);
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
        BufferedImage canvas = new BufferedImage(Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = canvas.getGraphics();
        g.setColor(Color.BLACK);
        final Graphics2D g2 = (Graphics2D) g;
        m.render(graphics);
        g.drawImage(canvas,0,0,null);
        repaint();
        //poly.g);

    }

    public void animate(){
        final TestPanel testPanel = this;
        Timer timer = new Timer(2, new ActionListener() {
            double v = 0;
            int xV = 0;
            int yV = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(true) {
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
                m.incrementRotation(xrotationSpeed,zrotationSpeed,0);
            }
        });
        timer.start();
    }
}
