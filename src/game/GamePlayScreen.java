package game;

import files.Image;
import render3D.Coordinate;
import render3D.Mesh;
import render3D.MeshBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import files.*;

public class GamePlayScreen extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    File[] files;
    Mesh[] meshes;
    int index = 0;
    String rootURL = ":\\";
    String URL = ":\\";
    int renderRadius = 50;
    double t = 0;
    ArrayList<File> renderedFiles;
    ArrayList<Mesh> rendered;
    BufferedImage shadow;
    boolean rotating = false;
    boolean reading = false;
    File readingFile = null;

    ArrayList<File> filePath = new ArrayList<>();
    Directory root;

    Directory currentDirectory;

    public enum GameAction{
        GO_BACK,
        CHOOSE_FILE,
        CHOOSE_FOLDER,
        SEARCH,
        SCROLL
    }

    public GamePlayScreen(File[] files){
        try {
            shadow = ImageIO.read(new java.io.File(".\\assets\\gameback.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.files = files;
        this.setSize(Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);
        meshes = new Mesh[files.length];
        for(int i = 0; i < files.length; i++){
            meshes[i] = files[i].getType().getMesh();
            meshes[i].moveTo(meshes[i].getCenter().translatedBy(0,0,30));
        }
        index = meshes.length/2+1;
        renderedFiles = new ArrayList<>(Arrays.asList(getFile(index+1),getFile(index),getFile(index-1)));
        rendered = new ArrayList<>(Arrays.asList(
                renderedFiles.get(0).getType().getMesh(),
                renderedFiles.get(1).getType().getMesh(),
                renderedFiles.get(2).getType().getMesh()));
        for(Mesh m: rendered){
            m.moveTo(new Coordinate(0,8,renderRadius));
        }
        rendered.get(0).rotateAbout(Coordinate.ORIGIN,0,-Math.PI/6,0);
        rendered.get(1).rotateAbout(Coordinate.ORIGIN,0,0,0);
        rendered.get(2).rotateAbout(Coordinate.ORIGIN,0,Math.PI/6,0);
        Timer t = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotate(-1);
            }
        });
        //t.start();

        Timer t2 = new Timer(20, new ActionListener() {
            double t = 0.05;
            @Override
            public void actionPerformed(ActionEvent e) {
                rendered.get(1).moveTo(rendered.get(1).getCenter().translatedBy(0,0.05*Math.sin(t),0));
                t+=0.05;
                repaint();
            }
        });
        //t2.start();
        this.addKeyListener(this);
    }

    public GamePlayScreen(Directory directory){
        try {
            shadow = ImageIO.read(new java.io.File(".\\assets\\gameback.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.root = directory;
        this.currentDirectory = directory;
        this.files = directory.getChildren();

        {
            this.setSize(Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);

            meshes = new Mesh[files.length];

            for (int i = 0; i < files.length; i++) {
                meshes[i] = files[i].getType().getMesh();
                meshes[i].moveTo(meshes[i].getCenter().translatedBy(0, 0, 30));
            }

            index = meshes.length / 2 + 1;
            renderedFiles = new ArrayList<>(Arrays.asList(getFile(index + 1), getFile(index), getFile(index - 1)));
            rendered = new ArrayList<>(Arrays.asList(
                    renderedFiles.get(0).getType().getMesh(),
                    renderedFiles.get(1).getType().getMesh(),
                    renderedFiles.get(2).getType().getMesh()));

            for (Mesh m : rendered) {
                m.moveTo(new Coordinate(0, 8, renderRadius));
            }

            rendered.get(0).rotateAbout(Coordinate.ORIGIN, 0, -Math.PI / 6, 0);
            rendered.get(1).rotateAbout(Coordinate.ORIGIN, 0, 0, 0);
            rendered.get(2).rotateAbout(Coordinate.ORIGIN, 0, Math.PI / 6, 0);
            Timer t = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rotate(-1);
                }
            });

            //t.start();

            Timer t2 = new Timer(20, new ActionListener() {
                double t = 0.05;

                @Override
                public void actionPerformed(ActionEvent e) {
                    rendered.get(1).moveTo(rendered.get(1).getCenter().translatedBy(0, 0.05 * Math.sin(t), 0));
                    t += 0.05;
                    repaint();
                }
            });
        }
        //t2.start();
    }

    public void setFiles(File[] files){
        this.files = files;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int sx = (Scene.SCREEN_WIDTH - shadow.getWidth())/2;
        int sy = (Scene.SCREEN_HEIGHT - shadow.getHeight())/2;
        g.drawImage(shadow,sx,sy,null);

        //g.fillRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        for(int i = 0; i < rendered.size(); i++){
            rendered.get(i).render(g);


            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 30));
            FontMetrics metrics = g.getFontMetrics(new Font("Courier New", Font.BOLD, 30));
            String name = renderedFiles.get(i).getName();
            int width = metrics.stringWidth(name);
            int x = (int) (1.1*rendered.get(i).getyRotation()*Scene.SCREEN_WIDTH/2) + Scene.SCREEN_WIDTH/2 - width/2;
            int y = 200-(int) (0.1*Math.abs(rendered.get(i).getyRotation())*Scene.SCREEN_WIDTH/2);
            g.drawString(name,x,y);
        }
        FontMetrics metrics = g.getFontMetrics(new Font("Courier New", Font.BOLD, 30));
        int width = metrics.stringWidth(URL);
        int x = Scene.SCREEN_WIDTH/2 - width/2;
        int y = 50;
        g.drawString(URL,x,y);
        if(reading){

        }

    }

    private void rotate(final int direction){
        if(!rotating) {
            rotating = true;
            final File nextFile = getFile(index - direction * 2);
            System.out.println(index + " " + (index - direction * 2));
            final Mesh next = getFile(index - direction * 2).getType().getMesh();
            next.moveTo(new Coordinate(0, 8, renderRadius));
            next.rotateAbout(Coordinate.ORIGIN, 0, direction * Math.PI / 3, 0);
            index -= direction;
            if (direction == -1) {
                rendered.add(0, next);
                renderedFiles.add(0, nextFile);
            } else {
                rendered.add(next);
                renderedFiles.add(nextFile);
            }

            final Timer t = new Timer(20, null);
            t.addActionListener(new ActionListener() {
                int count = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Mesh m : rendered) {
                        m.rotateAbout(Coordinate.ORIGIN, 0, -direction * 0.05 * Math.PI / 6, 0);
                    }
                    count++;
                    repaint();
                    if (count >= 20) {
                        t.stop();

                        if (direction == -1) {
                            rendered.remove(rendered.size() - 1);
                            renderedFiles.remove(renderedFiles.size() - 1);
                        }
                        if (direction == 1) {
                            rendered.remove(0);
                            renderedFiles.remove(0);
                        }
                        rotating = false;
                    }
                }
            });
            t.start();
        }
    }

    public Mesh getMesh(int N){
        while(N < 0){
            N+=meshes.length;
        }
        int index = N%meshes.length;
        return meshes[index];
    }

    public File getFile(int N){
        while(N < 0){
            N+=files.length;
        }
        int index = N%files.length;
        return files[index];
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }


    /*
    37 -- Left
    38 -- Up
    39 -- Right
    40 -- Down
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());;
        if(!rotating && !reading) {
            //Left
            if (e.getKeyCode() == 37) {
                rotate(1);
            }
            //Right
            else if (e.getKeyCode() == 39) {
                rotate(-1);
            }
            //Up
            else if(e.getKeyCode() == 38){
                interactWithFile();
            }
            //Down
            else if(e.getKeyCode() == 40){
                exitInteraction();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void interactWithFile(){
        File f = getFile(index);
        switch(f.getType()){
            case ZIP:
            case TEXT:
            case IMAGE:
                reading = true;
                readingFile = f;
                break;
            case DIRECTORY:
                this.currentDirectory = (Directory) f;
                files = ((Directory)f ).getChildren();
                break;
        }
        repaint();
    }

    private void exitInteraction(){
        File f = getFile(index);
        switch(f.getType()){
            case ZIP:
            case TEXT:
            case IMAGE:
                reading = false;
                readingFile = f;
                break;
            default:
                this.currentDirectory = ((Directory) f).getParent();
                files = currentDirectory.getChildren();
        }
        repaint();
    }
}
