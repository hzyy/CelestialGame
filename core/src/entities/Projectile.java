package entities;

import com.badlogic.gdx.Gdx;

public class Projectile {
    private int sX;
    private int sY;

    private int dX = 0;
    private int dY = 0;
    private float locX= 0, locY=0;
    private float speed; // bullet speed
    private float dx;
    private float dy;

    public Projectile(int sX, int sY, int dX, int dY) {
        this.sX = sX;
        this.sY = sY;
        locX = sX;
        locY = sY;
        this.dX = dX;
        this.dY = dY;
        recalculateVector(dX, dY);

    }

    // calculators vector based on input dest X / Y
    public void recalculateVector(int dX, int dY) {
        float rad = (float) (Math.atan2((dY - (Gdx.graphics.getHeight() / 2)), -(dX - (Gdx.graphics.getWidth() / 2))) + 30);

        speed = 10;

        this.dx = (float) Math.sin(rad) * speed;
        this.dy = -(float) Math.cos(rad) * speed;
    }

    // re calculates the vector based on the current position
    public void recalculateVector() {
        recalculateVector(dX, dY);
    }

    // moves the bullet across screen
    public void move() {
        float x = locX;
        float y = locY;


        x += dx;
        y += dy;


        locX = x;
        locY = y;
    }

    public float getLocY() {
        return locY;
    }

    public void setLocY(float locY) {
        this.locY = locY;
    }

    public float getLocX() {
        return locX;
    }

    public void setLocX(float locX) {
        this.locX = locX;
    }

}