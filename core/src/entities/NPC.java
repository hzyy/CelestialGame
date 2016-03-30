package entities;

import co.hzyy.celestial.screens.CelestialEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import data.ToolType;

public class NPC  extends Sprite {

    private final CelestialEngine game;
    private float x, y;
    private Texture body;
    private Texture head;

    public NPC(float x, float y, final CelestialEngine game, Texture body, Texture head){
        this.game = game;
        this.x = x;
        this.y = y;
        this.body = body;
        this.head = head;
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

    public Texture getBody() {
        return body;
    }

    public void setBody(Texture body) {
        this.body = body;
    }

    public Texture getHead() {
        return head;
    }

    public void setHead(Texture head) {
        this.head = head;
    }

    public void draw() {

    }


    private void init() {

    }


    public void update(float delta) {

        //movement.y -= gravity * delta;

    }

}
