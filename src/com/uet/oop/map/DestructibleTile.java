package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.object.powerups.PowerUp;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DestructibleTile extends Tile {
    protected PowerUp potentialPowerUp;
//    public boolean isDestroyed = false;

    public DestructibleTile(PowerUp potentialPowerUp, GameWindow gw, TextureManager textureManager) {
        this.tileType = TileType.DESTRUCTIBLE;
        this.potentialPowerUp = potentialPowerUp;
//        this.collideWithEntity = true;
        this.gw = gw;
        this.textureManager = textureManager;

        this.animations = new HashMap<>();
        this.currentAnimation = null;
        this.hitRect = new Rectangle(0, 0, gw.tileSize, gw.tileSize);

        setUpAnimations();

    }

    public void destroyed() {
        this.setAnimations("explodedBrick");
//        this.isDestroyed = true;

        if (this.potentialPowerUp.getType() == PowerUp.PowerUpType.NULL ||
            this.getTileType() == TileType.HAS_POWERUP) {
            this.tileType = TileType.PASSABLE;
        } else {
            this.tileType = TileType.HAS_POWERUP;
        }
        if (this.currentAnimation != null) {
            this.currentAnimation.resetAndStart();
        }
        /* TODO */
    }

    @Override
    protected void setUpAnimations() {
        // static brick
        BufferedImage[] brick = new BufferedImage[1];
        brick[0] = textureManager.getTexture("brick.png");
        Animation brickImg = new Animation(brick, 100, false);
        animations.put("brick", brickImg);

        // destroyed brick
        BufferedImage[] explodedBrick = new BufferedImage[6];
        explodedBrick[0] = textureManager.getTexture("brick_exploded.png");
        explodedBrick[1] = textureManager.getTexture("brick_exploded_1.png");
        explodedBrick[2] = textureManager.getTexture("brick_exploded_2.png");
        explodedBrick[3] = textureManager.getTexture("brick_exploded_3.png");
        explodedBrick[4] = textureManager.getTexture("brick_exploded_4.png");
        explodedBrick[5] = textureManager.getTexture("brick_exploded_5.png");
        Animation explodedBrickAnimation = new Animation(explodedBrick, 100, false);
        animations.put("explodedBrick", explodedBrickAnimation);

        // displayPowerup
        BufferedImage[] displayPowerupFrames = new BufferedImage[1];
        switch (potentialPowerUp.getType()) {
            case BOMB_UP ->
                displayPowerupFrames[0] = textureManager.getTexture("powerup_bombs.png");
            case FIRE_UP ->
                displayPowerupFrames[0] = textureManager.getTexture("powerup_flames.png");
            case SPEED_UP ->
                displayPowerupFrames[0] = textureManager.getTexture("powerup_speed.png");
            case NULL ->
                displayPowerupFrames[0] = textureManager.getTexture("grass.png");
        }
        Animation displayPowerup = new Animation(displayPowerupFrames, 100, false);
        animations.put("displayPowerup", displayPowerup);

        setAnimations("brick");
    }

    @Override
    protected void setAnimations(String animationName) {
        Animation nextAnimation = animations.get(animationName);
        if (nextAnimation != null && !nextAnimation.equals(currentAnimation)) {
            if (currentAnimation != null) {
                currentAnimation.stop();
            }
            currentAnimation = nextAnimation;
            currentAnimation.resetAndStart();

        } else if (nextAnimation != null && nextAnimation.equals(currentAnimation)) {
            if (!currentAnimation.isRunning()) {
                currentAnimation.start();
            }
        } else {
            System.err.println("Couldn't find brick's animation: " + animationName);
            currentAnimation = null;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        BufferedImage frameToRender = null;
        if (currentAnimation != null) {
            frameToRender = currentAnimation.getCurrentFrame();
        }
        if (frameToRender != null) {
            g.drawImage(frameToRender, position.getX(), position.getY(), gw.tileSize, gw.tileSize,null);
        } else { // if fails then
            g.setColor(Color.GRAY);
            g.fillRect(position.getX(), position.getY(), gw.tileSize, gw.tileSize);
        }
    }
}
