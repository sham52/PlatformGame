package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.math.BigDecimal;

import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {


    private SpriteBatch batch; // resimleri çizmek için kullanılacak nesne
    private OrthographicCamera camera; // kamera nesnesi

    Texture playerImg; // oyuncunun resmi
    Texture xpBarImg; // XP çubuğunun resmi
    Texture healerImg; // iyileştiricinin resmi
    Texture enemyImg; // düşmanların resmi
    Texture BackGround; // oyun sahnesinin arka planı

    Array<Enemy> enemies; // düşmanları depolamak için kullanılacak dizi
    Player player;

    Array<Player> Player; // oyuncuyu depolamak için kullanılacak dizi
    int control = 1; // kontrol değişkeni
    float[] memoryX = new float[5]; // son 5 düşmanın X koordinatlarını tutan dizi
    float[] memoryY = new float[5]; // son 5 düşmanın Y koordinatlarını tutan dizi

    // Şimdilik 5 düşman sayısına göre ayarlayacağımız için bir for döngüsü kullanacağız.
    // Bu nedenle X ve Y koordinatlarını ayrı ayrı tutacak iki dizi tanımlıyoruz.
    int[] createdEnemyCode = new int[5]; // oluşturulan son 5 düşmanın tür kodlarını tutan dizi

    int xpBarLoactionX = 50; // XP çubuğunun X koordinatı
    int xpBarLoactionY = 600; // XP çubuğunun Y koordinatı

    int healthPoint = 5; // oyuncunun sağlık puanı
    int HealerX = 100; // iyileştiricinin X koordinatı
    int HealerY = 240; // iyileştiricinin Y koordinatı
    private int enemySpeed = 2;
    private int playerSpeed = 12;

    @Override
    public void create() {
        // Kamera oluştur ve boyutları ayarla
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Giriş işlemci olarak bu sınıfı kullan
        Gdx.input.setInputProcessor(this);

        //Oyuncu
        // SpriteBatch, resimler, ve Array'lerin tanımlanması
        batch = new SpriteBatch();
        playerImg = new Texture("attack0.png");
        player = new Player();

        xpBarImg = new Texture("xpBar.png");
        healerImg = new Texture("Health.png");
        enemyImg = new Texture("enemy.png");
        BackGround = new Texture("BackGround.png");
        enemies = new Array<>();
        Player = new Array<>();
    }


    @Override
    public void render() {
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(BackGround, 0, 0);
        batch.draw(playerImg, player.getPlayerX(), player.getPlayerY());
        batch.draw(healerImg, HealerX, HealerY);

        if (control == 1) {
            for (int i = 0; i < 5; i++) {
                createdEnemyCode[i] = i;
                enemies.add(new Enemy(circleEnemyCreator(1), circleEnemyCreator(1), enemySpeed));
                batch.draw(enemyImg, circleEnemyCreator(1), circleEnemyCreator(1));
            }
            control = 0;
        }
        health();

//        player.playerMovement(playerX, playerY,playerSpeed);




        // Calculate distances between player and healer/enemies
        float distToHealer = Math.abs(HealerX - player.getPlayerX()) + Math.abs(HealerY - player.getPlayerY());
        float[] distToEnemies = new float[5];
        for (int i = 0; i < 5; i++) {
            distToEnemies[i] = Math.abs(memoryX[i] - player.getPlayerX()) + Math.abs(memoryY[i] - player.getPlayerY());
        }
        // Check if player is close to the healer
        if (distToHealer < 40) {
            // Heal the player
            if (healthPoint < 5) {
                healthPoint++;
            }
            // Generate new coordinates for the healer
            generateCoords();
        }

        // Attack the player if enemies are close
        for (int i = 0; i < 5; i++) {
            if (distToEnemies[i] < 40) {
                if (healthPoint > 0) {
                    healthPoint--;
                    memoryX[i] = circleEnemyCreator(0);
                    memoryY[i] = circleEnemyCreator(1);
                }
            }
            batch.draw(enemyImg, memoryX[i], memoryY[i]);
            // Move enemies towards the player
            if (memoryX[i] < player.getPlayerX()) {
                memoryX[i]++;
            } else if (memoryX[i] > player.getPlayerX()) {
                memoryX[i]--;
            }
            if (memoryY[i] < player.getPlayerY()) {
                memoryY[i]++;
            } else if (memoryY[i] > player.getPlayerY()) {
                memoryY[i]--;
            }

            // Avoid enemy overlap
            for (int x = 0; x < 5; x++) {
                for (int y = x + 1; y < 5; y++) {
                    float dx = memoryX[x] - memoryX[y];
                    float dy = memoryY[x] - memoryY[y];
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist < 20) {
                        float angle = (float) Math.atan2(dy, dx);
                        float moveX = (float) (20 * Math.cos(angle));
                        float moveY = (float) (20 * Math.sin(angle));
                        memoryX[x] += moveX;
                        memoryY[x] += moveY;
                        memoryX[y] -= moveX;
                        memoryY[y] -= moveY;
                    }
                }
            }

        }
        // Check if player is dead
        if (healthPoint == 0) {
//            System.out.println("Game Over");
        }
        health();
        batch.end();
    }

    public void health() {
        int hpCount = Math.min(healthPoint, 5);
        for (int i = 0; i < hpCount; i++) {
            float x = xpBarLoactionX + i * xpBarImg.getWidth();
            batch.draw(xpBarImg, x, xpBarLoactionY);
        }
    }

    @Override
    public void dispose() {
        // Oyun nesneleri dispose ediliyor
        batch.dispose();
        playerImg.dispose();
        xpBarImg.dispose();
        healerImg.dispose();
        enemyImg.dispose();
        BackGround.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        player.playerMovement(keycode);
        return true;
    }


    public void generateCoords() {
        HealerX = generateCoord(76, 11, 800, 100);
        HealerY = generateCoord(95, 11, 500, 100);
    }

    public int generateCoord(int range, int factor, int max, int min) {
        double randomX = Math.random() * range;
        double random = Math.random() * range;
        randomX *= random;
        int coord = ((int) ((int) random + randomX) + (int) randomX) * factor;
        if (coord > max || coord < min) {
            coord = (int) random * factor * 5;
            if (coord > max || coord < min) {
                coord = (int) random * factor * 10;
            }
        }
        return coord;
    }
//    public void playerMovement() {
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//            playerX += playerSpeed;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//            playerX -= playerSpeed;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
//            playerY += playerSpeed;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
//            playerY -= playerSpeed;
//        }
//    };

    public float circleEnemyCreator(int x) {
        float randomCircleNumX = (float) Math.sin(Math.random() * 100);
        float randomCircleNumY = (float) Math.sqrt(1 - (randomCircleNumX * randomCircleNumX));

        float zero = 0;

        if ((randomCircleNumX * randomCircleNumX) + (randomCircleNumY * randomCircleNumY) == 1) {
            if (x == 1) {
                BigDecimal bigDecimal = new BigDecimal(Float.toString(randomCircleNumX));
                zero = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() * 1000;
            }
            if (x == 0) zero = randomCircleNumY;
            BigDecimal bigDecimal = new BigDecimal(Float.toString(randomCircleNumY));
            zero = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() * 1000;
        }
        return zero;
    }

    //INPUT PROCESSOR METHODS
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

