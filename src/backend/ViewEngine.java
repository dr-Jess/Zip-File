package backend;

import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class ViewEngine {
    BackEngine backEngine;
    JFrame frame;
    JPanel homeScreen, gamePlayScreen, endGameScreen;
    public ViewEngine(BackEngine backEngine){
        this.backEngine = backEngine;
        frame = new JFrame("Zip-File");
        homeScreen = new HomeScreen(backEngine);
        frame.setContentPane(homeScreen);
        frame.setPreferredSize(new Dimension(Scene.SCREEN_WIDTH, Scene.SCREEN_HEIGHT));
        frame.setLayout(null);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void startGame(){
        gamePlayScreen = new GamePlayScreen(backEngine.getRoot(), backEngine);
        frame.setContentPane(gamePlayScreen);
        gamePlayScreen.requestFocus();
        frame.pack();
    }

    public void endGame(long time){
        endGameScreen = new EndGameScreen(time, backEngine);
        frame.setContentPane(endGameScreen);
        frame.pack();
    }
}
