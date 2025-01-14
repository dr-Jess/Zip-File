package game;

import backend.BackEngine;
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
    String stopwatch = "";
    int renderRadius = 50;
    double t = 0;
    ArrayList<File> renderedFiles;
    ArrayList<Mesh> rendered;
    BufferedImage shadow;
    BufferedImage up,down,left,right;
    private float endOpacity = 1F;
    boolean rotating = false;
    boolean reading = false;

    boolean typing = false;
    boolean tellUserToUseNumerals = false;
    boolean incorrectGuess = false;
    String userEntry = "";
    File readingFile = null;

    ArrayList<File> filePath = new ArrayList<>();
    Directory root;
    BackEngine backEngine;

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

    public GamePlayScreen(Directory directory, final BackEngine backEngine){
        this.backEngine = backEngine;
        this.setFocusable(true);
        try {
            shadow = ImageIO.read(new java.io.File(".\\assets\\gameback.png"));
            up = ImageIO.read(new java.io.File(".\\assets\\up.png"));
            down = ImageIO.read(new java.io.File(".\\assets\\down.png"));
            left = ImageIO.read(new java.io.File(".\\assets\\left.png"));
            right = ImageIO.read(new java.io.File(".\\assets\\right.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.root = directory;
        this.currentDirectory = directory;
        this.files = directory.getChildren();
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

        Timer t2 = new Timer(20, new ActionListener() {
            double t = 0.05;
            @Override
            public void actionPerformed(ActionEvent e) {
                rendered.get(1).moveTo(rendered.get(1).getCenter().translatedBy(0, 0.05 * Math.sin(t), 0));
                t += 0.05;
                repaint();
            }
        });

        Timer watch = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long time = backEngine.getTime();
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
                stopwatch = minuteS + secondS + milliS;
                repaint();
            }
        });
        watch.start();
        this.addKeyListener(this);
        //t2.start();
        startFade();
    }

    public void setFiles(File[] files){
        this.files = files;
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
                    backEngine.endGame();
                }
                endOpacity= (float) (1-opacityRemaining);
                repaint();
                lastTime = currentTime;
            }
        });
        frameTimer.start();
    }

    public void resetRendered(){

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
    }

    public void resetRendered(File f){

        meshes = new Mesh[files.length];

        index = meshes.length / 2 + 1;

        for (int i = 0; i < files.length; i++) {
            meshes[i] = files[i].getType().getMesh();
            meshes[i].moveTo(meshes[i].getCenter().translatedBy(0, 0, 30));
            if(files[i] == f)
                index = i;
        }

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int sx = (Scene.SCREEN_WIDTH - shadow.getWidth())/2;
        int sy = (Scene.SCREEN_HEIGHT - shadow.getHeight())/2;
        g.drawImage(shadow,sx,sy,null);

        g.setFont(new Font("Courier New", Font.BOLD, 30));
        //g.fillRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);
        for(int i = 0; i < rendered.size(); i++){
            rendered.get(i).render(g);

            if(renderedFiles.get(i).hasBeenViewed())
                g.setColor(Color.GRAY);
            else
                g.setColor(Color.WHITE);
            FontMetrics metrics = g.getFontMetrics(new Font("Courier New", Font.BOLD, 30));
            String name = renderedFiles.get(i).getName();
            int width = metrics.stringWidth(name);
            int x = (int) (1.1*rendered.get(i).getyRotation()*Scene.SCREEN_WIDTH/2) + Scene.SCREEN_WIDTH/2 - width/2;
            int y = 200-(int) (0.1*Math.abs(rendered.get(i).getyRotation())*Scene.SCREEN_WIDTH/2);
            g.drawString(name,x,y);
        }
        g.setColor(Color.WHITE);
        FontMetrics metrics = g.getFontMetrics(new Font("Courier New", Font.BOLD, 30));
        Directory[] path = currentDirectory.getPath();
        String url = ":\\";
        for(Directory d: path){
            url += d.getName()+"\\";
        }
        url+= currentDirectory.getName();
        int width = metrics.stringWidth(url);
        int x = Scene.SCREEN_WIDTH/2 - width/2;
        int y = 50;
        g.drawString(url,x,y);
        g.drawString(stopwatch, (Scene.SCREEN_WIDTH - metrics.stringWidth(stopwatch))-20,50);
        if(reading){

            if(readingFile.getType() == FileType.IMAGE){
                Image i = (Image) readingFile;
                try {
                    BufferedImage b = ImageIO.read(new java.io.File(i.getImagePath()));
                    int height = b.getHeight();
                    int targetHeight = 400;
                    double ratio = ((double) targetHeight) / ((double) height);
                    b = resizeImage(b, (int) (b.getWidth() * ratio), (int) (height * ratio));
                    int bx = (Scene.SCREEN_WIDTH - b.getWidth())/2;
                    int by = (Scene.SCREEN_HEIGHT - b.getHeight())/2;
                    g.drawImage(b,bx,by, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if(readingFile.getType() == FileType.TEXT) {
                Text t = (Text) readingFile;
                String text = t.getText();
                g.setColor(new Color(30,30,30,240));
                g.fillRect(300,100,Scene.SCREEN_WIDTH-600,Scene.SCREEN_HEIGHT-200);
                g.setColor(Color.WHITE);
                metrics = g.getFontMetrics(new Font("Courier New", Font.BOLD, 30));
                String temp = "";
                int drawHeight = 150;

                for(char c: text.toCharArray()){
                    if(metrics.stringWidth(temp) > 500 || c == '\n'){
                        StringBuilder remainder = new StringBuilder();
                        String toPrint = temp;
                        if(temp.contains(" ")){
                            char[] cs = temp.toCharArray();
                            for(int i = cs.length-1; i >= 0; i--){
                                if(cs[i] == ' ') {
                                    toPrint = temp.substring(0,i);
                                    break;
                                }
                                else{
                                    remainder.insert(0, cs[i]);
                                }
                            }
                        }
                        g.drawString(toPrint, 350,drawHeight);
                        drawHeight += 40;
                        if(c != '\n')
                            temp = remainder.toString() + c;
                        else
                            temp = remainder.toString() + " ";

                    }else{
                        temp+=c;
                    }
                }
                if(!temp.equals("")){
                    g.drawString(temp, 350,drawHeight);
                }
            }else if(readingFile.getType() == FileType.ZIP) {
                Zip z = (Zip) readingFile;
                g.setFont(new Font("Courier New", Font.BOLD, 30));
                g.setColor(new Color(30,30,30,240));
                g.fillRect(300,100,Scene.SCREEN_WIDTH-600,Scene.SCREEN_HEIGHT-200);
                g.setColor(Color.WHITE);
                g.drawString("Start typing to enter the",350,150);
                g.drawString("4-digit passcode",350,190);
                g.drawString("Press [ENTER] to try passcode",350,230);
                g.fillRect(350,300,Scene.SCREEN_WIDTH-700,100);
                if(tellUserToUseNumerals){
                    g.drawString("Only numeric values are accepted",350,270);
                }
                if(incorrectGuess){
                    g.drawString("Incorrect! Try again.",350,270);
                }
                g.setColor(Color.BLACK);
                g.drawString(userEntry, 370,360);
            }
            g.setColor(Color.WHITE);
            int downX = Scene.SCREEN_WIDTH/2 - down.getWidth()/2 - metrics.stringWidth(" to close")/2;
            g.drawImage(down,downX,500,null);
            g.drawString(" to close", Scene.SCREEN_WIDTH/2 + down.getWidth()/2 - metrics.stringWidth(" to close")/2, 540);
        }else {
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            int upX = Scene.SCREEN_WIDTH / 2 - up.getWidth() / 2 - metrics.stringWidth(" to interact") / 2;
            g.drawImage(up, upX, 510, null);
            g.drawString(" to interact", Scene.SCREEN_WIDTH / 2 + up.getWidth() / 2 - metrics.stringWidth(" to interact") / 2, 540);

            int leftX = Scene.SCREEN_WIDTH / 6 - left.getWidth() / 2 - metrics.stringWidth(" to scroll left") / 2;
            g.drawImage(left, leftX, 580, null);
            g.drawString(" to scroll left", Scene.SCREEN_WIDTH / 6  + left.getWidth() / 2 - metrics.stringWidth(" to scroll left") / 2, 610);

            int rightX = Scene.SCREEN_WIDTH*5/6 + right.getWidth() / 2 - metrics.stringWidth(" to scroll right") / 2;
            g.drawImage(right, rightX, 580, null);
            g.drawString(" to scroll right", Scene.SCREEN_WIDTH*5/6 + right.getWidth()*3/2 - metrics.stringWidth(" to scroll right") / 2, 610);
            if(currentDirectory != root) {
                int downX = Scene.SCREEN_WIDTH / 2 - down.getWidth() / 2 - metrics.stringWidth(" to go back") / 2;
                g.drawImage(down, downX, 580, null);
                g.drawString(" to go back", Scene.SCREEN_WIDTH / 2 + down.getWidth() / 2 - metrics.stringWidth(" to go back") / 2, 610);
            }


        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,clamp(endOpacity, 0, 1)));
        g2d.fillRect(0,0,Scene.SCREEN_WIDTH,Scene.SCREEN_HEIGHT);

    }

    private float clamp(float val, float min, float max) {
        if(val < min) return min;
        else if(val > max) return max;
        else return val;
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private void rotate(final int direction){
        if(!rotating) {
            rotating = true;
            final File nextFile = getFile(index - direction * 2);
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
        if(!rotating && !reading) {
            //Left
            if (e.getKeyCode() == 37) {
                rotate(-1);
            }
            //Right
            else if (e.getKeyCode() == 39) {
                rotate(1);
            }
            //Up
            else if(e.getKeyCode() == 38){
                interactWithFile();
            }
        }
        if(!rotating) {
            //Down
            if (e.getKeyCode() == 40) {
                exitInteraction();
            }
        }
        if(typing){
            if(48 <= e.getKeyCode() && e.getKeyCode() <= 57){
                userEntry += e.getKeyChar();
                tellUserToUseNumerals = false;
                incorrectGuess = false;
            }else if(e.getKeyCode() == 10){
                if(userEntry.equals(backEngine.getPassword())) {
                    endFade();
                }else{
                    userEntry = "";
                    tellUserToUseNumerals = false;
                    incorrectGuess = true;
                }
            }else if(e.getKeyCode() == 8){
                if(userEntry.length() > 0) {
                    userEntry = userEntry.substring(0,userEntry.length()-1);
                }
            }
            else{
                incorrectGuess = false;
                tellUserToUseNumerals = true;
            }

        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void interactWithFile(){
        File f = getFile(index);
        f.viewFile();
        switch(f.getType()){
            case ZIP:
                typing = true;
                reading = true;
                readingFile = f;
                break;
            case TEXT:
            case IMAGE:
                reading = true;
                readingFile = f;
                break;
            case DIRECTORY:
                this.currentDirectory = (Directory) f;
                files = ((Directory)f ).getChildren();
                resetRendered();
                break;
        }
        repaint();
    }

    private void exitInteraction(){
        File f = getFile(index);
        if(reading){
                reading = false;
                typing = false;
                tellUserToUseNumerals = false;
                incorrectGuess = false;
                userEntry = "";
                readingFile = f;
        }
        else if(currentDirectory != root){
            Directory temp = currentDirectory;
            this.currentDirectory = currentDirectory.getParent();
            files = currentDirectory.getChildren();
            resetRendered(temp);
        }
        repaint();
    }
}
