package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.object.powerups.PowerUp;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Portal extends DestructibleTile {

    public Portal(PowerUp potentialPowerup, GameWindow gw, TextureManager textureManager) {
        super(potentialPowerup, gw, textureManager);
        this.tileType = TileType.PORTAL;
        this.gw = gw;
        this.textureManager = textureManager;

        this.animations = new HashMap<>();
        this.currentAnimation = null;
        this.hitRect = new Rectangle(0, 0, gw.tileSize, gw.tileSize);
        setUpAnimations();
    }

    @Override
    protected void setUpAnimations() {
        // static brick
        BufferedImage[] brick = new BufferedImage[1];
        brick[0] = textureManager.getTexture("brick.png");
        Animation brickImg = new Animation(brick, 100, false);
        animations.put("brick", brickImg);

        // destroyed brick
        BufferedImage[] explodedBrick = new BufferedImage[7];
        explodedBrick[0] = textureManager.getTexture("brick_exploded.png");
        explodedBrick[1] = textureManager.getTexture("brick_exploded_1.png");
        explodedBrick[2] = textureManager.getTexture("brick_exploded_2.png");
        explodedBrick[3] = textureManager.getTexture("brick_exploded_3.png");
        explodedBrick[4] = textureManager.getTexture("brick_exploded_4.png");
        explodedBrick[5] = textureManager.getTexture("brick_exploded_5.png");
        explodedBrick[6] = textureManager.getTexture("portal.png");
        Animation portalUnderBrick = new Animation(explodedBrick, 100, false);
        animations.put("explodedBrick", portalUnderBrick);

        // display portal
        BufferedImage[] portal = new BufferedImage[1];
        portal[0] = textureManager.getTexture("portal.png");
        Animation portalAnim = new Animation(portal, 100, false);
        animations.put("displayPortal", portalAnim);


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
            System.err.println("Couldn't find portal's animation: " + animationName);
            currentAnimation = null;
        }
    }

    @Override
    public void destroyed() {
        this.setAnimations("explodedBrick");
        this.tileType = TileType.PORTAL;

        if (this.currentAnimation != null) {
            this.currentAnimation.resetAndStart();
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
            g.setColor(Color.BLACK);
            g.fillRect(position.getX(), position.getY(), gw.tileSize, gw.tileSize);
        }
    }
}
