package data;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.io.*;
import java.util.Scanner;

public class Tile {

    private float x, y, width, height;
    private Sprite sprite;
    private TileType type;
    private Rectangle rect;

    // Constructor
    public Tile(float x, float y, float width, float height, TileType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.sprite = new Sprite(type.textureName.getTexture());
        if (this.type != TileType.Air) {
            this.rect = new Rectangle(x, y, width, height);
        }
    }



    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Sprite getTexture() {
        return sprite;
    }

    public void setTexture(Sprite sprite) {
        this.sprite = sprite;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }


    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

}
