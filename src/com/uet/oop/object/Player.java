package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.core.KeyboardHandler;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.Renderable;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends GameEntity implements Renderable {
    private int bombs;
    private int explosionRadius;
    private int invulnerabilityTimer;
    private List<PowerUp> currentPowerups;
    private String texturePath;

    GameWindow gw;
    KeyboardHandler keyH;

    private Map<String, Animation> animations;
    private Animation currentAnimation;
    private TextureManager textureManager;

    public Player(Position position, int movementSpeed, int hitPoints, GameWindow gw, KeyboardHandler keyH, TextureManager textureManager) {
        super(position, movementSpeed, hitPoints);
        bombs = 1;
        explosionRadius = 1;
        invulnerabilityTimer = 3000; // ms
        currentPowerups = new ArrayList<>();

        this.gw = gw;
        this.keyH = keyH;
        this.textureManager = textureManager;

        this.animations = new HashMap<>();
        this.currentAnimation = null;
    }

    public void setupAnimation() {
        String sheetName = "player_spritesheet"; // crucial
        int frameWidth = 16;
        int frameHeight = 16;
        int framesPerAnimation = 3;

        BufferedImage sheet = textureManager.getTexture(sheetName);
        if (sheet == null) {
            System.err.println("Couldn't load player sheet: " + sheetName);
        }
// ------------------------------------ hard code
        // cut player spritesheet - move left
        BufferedImage[] moveLeftFrames = new BufferedImage[framesPerAnimation];
        int moveLeftRow = 0;
        int moveLeftCol = 0;
        for (int i = moveLeftCol; i < framesPerAnimation + moveLeftCol; i++) {
            moveLeftFrames[i] = textureManager.getSubImage(sheetName, i * frameWidth, moveLeftRow * frameHeight, frameWidth, frameHeight);
        }
        Animation moveLeftAnimation = new Animation(moveLeftFrames, 200, true);
        animations.put("moveLeftAnimation", moveLeftAnimation);

        // cut player spritesheet - move right
        BufferedImage[] moveRightFrames = new BufferedImage[framesPerAnimation];
        int moveRightRow = 1;
        int moveRightCol = 0;
        for (int i = moveRightCol; i < framesPerAnimation + moveRightCol; i++) {
            moveRightFrames[i] = textureManager.getSubImage(sheetName, i * frameWidth, moveRightRow * frameHeight, frameWidth, frameHeight);
        }
        Animation moveRightAnimation = new Animation(moveRightFrames, 200, true);
        animations.put("moveRightAnimation", moveRightAnimation);

        // cut player spritesheet - move up
        BufferedImage[] moveUpFrames = new BufferedImage[framesPerAnimation];
        int moveUpRow = 1;
        int moveUpCol = 3;
        for (int i = moveUpCol; i < framesPerAnimation + moveUpCol; i++) {
            moveUpFrames[i - moveUpCol] = textureManager.getSubImage(sheetName, i * frameWidth, moveUpRow * frameHeight, frameWidth, frameHeight);
        }
        Animation moveUpAnimation = new Animation(moveUpFrames, 200, true);
        animations.put("moveUpAnimation", moveUpAnimation);

        // cut player spritesheet - move down
        BufferedImage[] moveDownFrames = new BufferedImage[framesPerAnimation];
        int moveDownRow = 0;
        int moveDownCol = 3;
        for (int i = moveDownCol; i < framesPerAnimation + moveDownCol; i++) {
            moveDownFrames[i - moveDownCol] = textureManager.getSubImage(sheetName, i * frameWidth, moveDownRow * frameHeight, frameWidth, frameHeight);
        }
        Animation moveDownAnimation = new Animation(moveDownFrames, 200, true);
        animations.put("moveDownAnimation", moveDownAnimation);
//-------------------------------------
        setAnimation("moveRightAnimation"); // default
    }

    public void setAnimation(String animationName) {
        Animation nextAnimation = animations.get(animationName);
        if (nextAnimation != null && nextAnimation != currentAnimation) {
            if (currentAnimation != null) {
                currentAnimation.stop();
            }
            currentAnimation = nextAnimation;
            currentAnimation.resetAndStart();

        } else if (nextAnimation != null && nextAnimation == currentAnimation) {
            if (!currentAnimation.isRunning()) {
                currentAnimation.start();
            }
        } else {
            System.err.println("Couldn't find player's animation: " + animationName);
            currentAnimation = null;
        }
    }

//    public void loadTexture() {
//        try {
//            texture = ImageIO.read(getClass().getResourceAsStream("/player/player.png"));
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void update() {
        movement();
        takeDamage();
        die();
    }

    @Override
    public void movement() {
        if (keyH.isKeyPressed(KeyEvent.VK_W)) {
            setAnimation("moveUpAnimation");
            position.setY(position.getY() - movementSpeed);
            currentAnimation.update();
        } else if (keyH.isKeyPressed(KeyEvent.VK_S)) {
            setAnimation("moveDownAnimation");
            position.setY(position.getY() + movementSpeed);
            currentAnimation.update();
        } else if (keyH.isKeyPressed(KeyEvent.VK_A)) {
            setAnimation("moveLeftAnimation");
            position.setX(position.getX() - movementSpeed);
            currentAnimation.update();
        } else if (keyH.isKeyPressed(KeyEvent.VK_D)) {
            setAnimation("moveRightAnimation");
            position.setX(position.getX() + movementSpeed);
            currentAnimation.update();
        }

//        if (currentAnimation != null) {
//            currentAnimation.update();
//        }

    }

    public void draw(Graphics2D g) {
        BufferedImage frameToRender = null;
        if (currentAnimation != null) {
            frameToRender = currentAnimation.getCurrentFrame();
        }
        if (frameToRender != null) {
            g.drawImage(frameToRender, position.getX(), position.getY(), gw.tileSize, gw.tileSize,null);
        } else { // if fails then
            g.setColor(Color.RED);
            g.fillRect(position.getX(), position.getY(), gw.tileSize, gw.tileSize);
        }
    }

    @Override
    public void takeDamage() {
        hitPoints -= 1;
        /* runs death animation */
        /* reset player state, position */
    }

    @Override
    public void die() { // should be renamed as endGame
        if (hitPoints == 0) {
            /* ENDGAME */
        }
    }

    public void placeBombs() {
        /* TODO */
    }
}
