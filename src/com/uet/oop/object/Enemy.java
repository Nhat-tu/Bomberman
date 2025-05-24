package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.map.TileManager;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.Rectangle;
import java.util.HashMap;

public abstract class Enemy extends GameEntity {
    GameWindow gw;
    TextureManager textureManager;
    TileManager map;
    protected String direction;
    protected boolean isAlive;

    public Enemy(GameWindow gw, TextureManager textureManager, TileManager tileManager) {
        this.gw = gw;
        this.textureManager = textureManager;
        this.map = tileManager;
        this.animations = new HashMap<>();
    }

    public void setDefaultValues() {
        this.hitPoints = 1;
        this.movementSpeed = 2;
        this.currentAnimation = null;
        this.isAlive = true;
        this.hitRect = new Rectangle(
                1,
                1,
                46,
                46
        );
    }

    // should be called in bomb.java
    @Override
    public void takeDamage() {
        hitPoints -= 1;
        die();
    }

    @Override
    public void die() {
        isAlive = false;
        setAnimation("deathAnimation");
    }

    public String getDirection() {
        return direction;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
