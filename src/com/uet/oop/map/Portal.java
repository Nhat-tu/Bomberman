package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Portal extends Tile {

    public Portal(GameWindow gw, TextureManager textureManager) {
        this.gw = gw;
        this.textureManager = textureManager;
    }

    @Override
    protected void setUpAnimations() {
        BufferedImage[] portal = new BufferedImage[1];
        portal[0] = textureManager.getTexture("portal.png");
        Animation portalImg = new Animation(portal, 100, false);
        animations.put("portal", portalImg);

        setAnimations("portal");
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
