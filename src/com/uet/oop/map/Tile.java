package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.object.Position;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.Renderable;
import com.uet.oop.rendering.TextureManager;

import java.awt.Rectangle;
import java.util.Map;

public abstract class Tile implements Renderable {
    protected GameWindow gw;
    protected TextureManager textureManager;

    protected Position position;
    protected Rectangle hitRect;
//    protected boolean collideWithEntity = false;

    protected Map<String, Animation> animations;
    protected Animation currentAnimation;

    public enum TileType {
        PASSABLE,
        INDESTRUCTIBLE,
        DESTRUCTIBLE,
        BOMB,
        EXPLOSION_AREA
    }

    TileType tileType;

    public TileType getTileType() {
        return tileType;
    }

    protected abstract void setUpAnimations();
    protected abstract void setAnimations(String animationName);

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
