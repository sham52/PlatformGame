package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.graphics.Texture;

public class Player extends Rectangle {
    private float playerX;
    private float playerY;
    private int healthPoint;
    private Texture image;
    private int playerSpeed;

    public Player(float x, float y, int healthPoint, Texture image, int speed) {
        this.playerX = x;
        this.playerY = y;
        this.healthPoint = healthPoint;
        this.image = image;
        this.playerSpeed = speed;
    }

    public Player() {
        this.playerX = 0;
        this.playerY = 0;
        this.healthPoint = 0;
        this.playerSpeed = 0;
    }

    // getters and setters for the player variables
    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }


    public void increasePlayerX() {
        this.playerX++;
    }

    public void increasePlayerX(float add) {
        this.playerX += add;
    }

    public void decreasePlayerX() {
        this.playerX--;
    }

    public void increasePlayerY() {
        this.playerY++;
    }

    public void decreasePlayerY() {
        this.playerY--;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public int getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(int speed) {
        this.playerSpeed = speed;
    }

    // move the player based on the given input
    public void playerMovement(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= speed;
        }
//        public boolean keyDown(int keycode) {
//            switch (keycode) {
//                case Input.Keys.A:
//                case Input.Keys.LEFT:
//                    playerX--;
//                    if (playerX < 0) {
//                        playerX = 0;
//                    }
//                    break;
//                case Input.Keys.D:
//                case Input.Keys.RIGHT:
//                    playerX++;
//                    if (playerX >= width) {
//                        playerX = width - 1;
//                    }
//                    break;
//                case Input.Keys.W:
//                case Input.Keys.UP:
//                    playerY++;
//                    if (playerY >= height) {
//                        playerY = height - 1;
//                    }
//                    break;
//                case Input.Keys.S:
//                case Input.Keys.DOWN:
//                    playerY--;
//                    if (playerY < 0) {
//                        playerY = 0;
//                    }
//                    break;
//            }
//            return true;
//        }
    }


}
