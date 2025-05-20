package com.uet.oop.object;

import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.Renderable;

import java.awt.Rectangle;
import java.util.Map;

public abstract class GameEntity implements Renderable {
    protected Position mapPosition; // actual position in "world", not in game screen
    protected int movementSpeed;
    protected int hitPoints;
    protected Rectangle hitRect;

    protected Map<String, Animation> animations;
    protected Animation currentAnimation;

    public abstract void movement();
    public abstract void takeDamage();
    public abstract void die();
    public abstract void update();

    protected abstract void setupAnimation();
    protected abstract void setAnimation(String animationName);

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public Position getMapPosition() {
        return mapPosition;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setMapPosition(Position mapPosition) {
        this.mapPosition = mapPosition;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setHitPoints(int hitPoints) { // update healthpoint
        this.hitPoints = hitPoints;
    }
}