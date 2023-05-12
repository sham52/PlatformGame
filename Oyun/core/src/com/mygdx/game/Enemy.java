package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends Rectangle {
    private float speed;
    private Texture enemyImg;
    Enemy(float x, float y, float speed) {
        super(x, y, 30, 30);
        this.speed = speed;
    }
    
    public void update(float delta) {
        y += speed * delta;
    }
}
