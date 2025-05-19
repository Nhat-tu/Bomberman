package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.rendering.TextureManager;

import java.util.HashMap;

public abstract class Enemy extends GameEntity {
    GameWindow gw;
    TextureManager textureManager;

    public Enemy(GameWindow gw, TextureManager textureManager) {
        setDefaultValues();
        this.gw = gw;
        this.textureManager = textureManager;
        this.animations = new HashMap<>();
    }

    public void setDefaultValues() {
        /* read Position from map.txt */
        this.hitPoints = 1;
        this.movementSpeed = 6;
        this.currentAnimation = null;
    }

    /**
     *
     */
    @Override
    public void movement() { // ???? should be rebuilt
        final int[] dx = {-1, 1, 0, 0};
        final int[] dy = {0, 0, -1, 1};

        // its movement: random 1/4 directions -> move to that dir until "collide" with wall/bomb -> repeat.
    }

    /**
     * abcde.
     *
     */
    @Override
    public void takeDamage() {
        hitPoints -= 1;
        if (hitPoints == 0) {
            die();
        }
    }

    /**
     *
     */
    @Override
    public void die() {
        // use hashcode & equals to see which one dies.
        /* List */ /* entities.erase() or something */
    }
}
