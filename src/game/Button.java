package game;

import render2D.BoundingBox2;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

public abstract class Button extends UIComponent{
    private BoundingBox2 hitbox;
    private boolean visible = true;

    public Button(int x, int y, int width, int height) {
        hitbox = new BoundingBox2(x,y,x+width,x+height);
    }

    public void switchVisibility() {
        visible = !visible;
    };


    public abstract void draw(Graphics graphics, ImageObserver io);

    public boolean isVisible() {
        // TODO Auto-generated method stub
        return visible;
    }
}
