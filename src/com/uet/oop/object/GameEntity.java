package com.uet.oop.object;

import com.uet.oop.rendering.Animation;
import java.util.Map;

public abstract class GameEntity {
    protected Position position;
    protected int movementSpeed;
    protected int hitPoints;

    protected Map<String, Animation> animations;
    protected Animation currentAnimation;

    public abstract void movement();
    public abstract void takeDamage();
    public abstract void die();

    public abstract void setupAnimation();
    public abstract void setAnimation(String animationName);

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public Position getPosition() {
        return position;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setHitPoints(int hitPoints) { // update healthpoint
        this.hitPoints = hitPoints;
    }
}