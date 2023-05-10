package game;

import render3D.Mesh;
import render3D.MeshBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePlayScreen extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    String[] files;
    int index = 0;
    String rootURL = ":\\";
    String URL;
    Mesh[] meshes;

    public enum GameAction{
        GO_BACK,
        CHOOSE_FILE,
        CHOOSE_FOLDER,
        SEARCH,
        SCROLL
    }

    public enum FileType{
        TEXT{
            @Override
            public Mesh getMesh() {
                return new MeshBuilder().readOBJ(
                        ".\\assets\\textfile3.obj",
                        ".\\assets\\textfilepalette.png");
            }
        },
        IMAGE {
            @Override
            public Mesh getMesh() {
                return new MeshBuilder().readOBJ(
                        ".\\assets\\imagefile.obj",
                        ".\\assets\\imagefilepalette.png");
            }
        },
        FOLDER {
            @Override
            public Mesh getMesh() {
                return new MeshBuilder().readOBJ(
                        ".\\assets\\folder.obj",
                        ".\\assets\\folderpalette.png");
            }
        };
        public abstract Mesh getMesh();
    }

    public GamePlayScreen(String[] files){
        this.files = files;
        this.setSize(Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT);
    }

    public void setFiles(String[] files){
        this.files = files;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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
}
