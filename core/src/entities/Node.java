package entities;


import co.hzyy.celestial.screens.CelestialEngine;
import com.badlogic.gdx.graphics.g2d.Sprite;
import data.TileType;

public class Node extends Sprite {
    private float width = 8, height = 8;
    private TileType t;
    private float x,y;
    private Sprite s;


    public Node(TileType t, float x, float y) {
        this.t = t;
        this.x=x;
        this.y=y;
        this.s = new Sprite(t.getTexture());

    }


    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TileType getT() {
        return t;
    }

    public void setT(TileType t) {
        this.t = t;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

}
