package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.object.Position;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.Renderable;
import com.uet.oop.rendering.TextureManager;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile implements Renderable {
    protected Position position;
    protected boolean collided = false;

    protected GameWindow gw;
    protected TextureManager textureManager;

    protected Map<String, Animation> animations;
    protected Animation currentAnimation;

    public enum TileType {
        INDESTRUCTIBLE,
        DESTRUCTIBLE
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
