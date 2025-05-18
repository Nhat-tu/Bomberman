package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java. awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class IndestructibleTile extends Tile {

    public IndestructibleTile(GameWindow gw, TextureManager textureManager) {
        this.tileType = TileType.INDESTRUCTIBLE;
        this.gw = gw;
        this.textureManager = textureManager;

        this.animations = new HashMap<>();
        this.currentAnimation = null;

        setUpAnimations();
    }

    @Override
    protected void setUpAnimations() {
        // wall
        BufferedImage[] wall = new BufferedImage[1];
        wall[0] = textureManager.getTexture("wall.png");
        Animation wallImg = new Animation(wall, 100, false);
        animations.put("wall", wallImg);

        setAnimations("wall");
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
            System.err.println("Couldn't find wall's animation: " + animationName);
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
            g.setColor(Color.MAGENTA);
            g.fillRect(position.getX(), position.getY(), gw.tileSize, gw.tileSize);
        }
    }
}
