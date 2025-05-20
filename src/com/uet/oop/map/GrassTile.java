package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GrassTile extends Tile {

    public GrassTile(GameWindow gw, TextureManager textureManager) {
        this.tileType = TileType.PASSABLE;
        this.gw = gw;
        this.textureManager = textureManager;

        this.animations = new HashMap<>();
        this.currentAnimation = null;
        this.hitRect = new Rectangle(0, 0, gw.tileSize, gw.tileSize);
        setUpAnimations();
    }

    @Override
    protected void setUpAnimations() {
        // grass
        BufferedImage[] grass = new BufferedImage[1];
        grass[0] = textureManager.getTexture("grass.png");
        Animation grassAnimation = new Animation(grass, 100, false);
        animations.put("grass", grassAnimation);

        setAnimations("grass");
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
            System.err.println("Couldn't find grass's animation: " + animationName);
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
            g.setColor(Color.YELLOW);
            g.fillRect(position.getX(), position.getY(), gw.tileSize, gw.tileSize);
        }
    }
}
