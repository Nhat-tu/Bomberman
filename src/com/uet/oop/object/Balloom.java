package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.map.TileManager;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Balloom extends Enemy {
    public Balloom(GameWindow gw, TextureManager textureManager, TileManager tileManager) {
        super(gw, textureManager, tileManager);
        setupAnimation();
    }

    @Override
    public void setupAnimation() {
        int framesPerAnimation = 4;

// ------------------------------------
        // move left anim
        BufferedImage[] moveLeftFrames = new BufferedImage[framesPerAnimation];

        moveLeftFrames[0] = textureManager.getTexture("balloom_left_1.png");
        moveLeftFrames[1] = textureManager.getTexture("balloom_left_2.png");
        moveLeftFrames[2] = textureManager.getTexture("balloom_left_3.png");
        moveLeftFrames[3] = textureManager.getTexture("balloom_left_2.png");

        Animation moveLeftAnimation = new Animation(moveLeftFrames, 200, true);
        animations.put("moveLeftAnimation", moveLeftAnimation);

        // move right anim
        BufferedImage[] moveRightFrames = new BufferedImage[framesPerAnimation];

        moveRightFrames[0] = textureManager.getTexture("balloom_right_1.png");
        moveRightFrames[1] = textureManager.getTexture("balloom_right_2.png");
        moveRightFrames[2] = textureManager.getTexture("balloom_right_3.png");
        moveRightFrames[3] = textureManager.getTexture("balloom_right_2.png");

        Animation moveRightAnimation = new Animation(moveRightFrames, 200, true);
        animations.put("moveRightAnimation", moveRightAnimation);

        // move up anim
        BufferedImage[] moveUpFrames = new BufferedImage[framesPerAnimation];

        moveUpFrames[0] = textureManager.getTexture("balloom_up_1.png");
        moveUpFrames[1] = textureManager.getTexture("balloom_up_2.png");
        moveUpFrames[2] = textureManager.getTexture("balloom_up_3.png");
        moveUpFrames[3] = textureManager.getTexture("balloom_up_2.png");

        Animation moveUpAnimation = new Animation(moveUpFrames, 200, true);
        animations.put("moveUpAnimation", moveUpAnimation);

        // move down anim
        BufferedImage[] moveDownFrames = new BufferedImage[framesPerAnimation];

        moveDownFrames[0] = textureManager.getTexture("balloom_down_1.png");
        moveDownFrames[1] = textureManager.getTexture("balloom_down_2.png");
        moveDownFrames[2] = textureManager.getTexture("balloom_down_3.png");
        moveDownFrames[3] = textureManager.getTexture("balloom_down_2.png");

        Animation moveDownAnimation = new Animation(moveDownFrames, 200, true);
        animations.put("moveDownAnimation", moveDownAnimation);
//-------------------------------------
        setAnimation("moveRightAnimation"); // default
    }

    @Override
    public void setAnimation(String animationName) {
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
            System.err.println("Couldn't find Balloom's animation: " + animationName);
            currentAnimation = null;
        }
    }

    public void update() {
        movement();
        takeDamage();
        die();
    }

    public void draw(Graphics2D g) {

    }
}
