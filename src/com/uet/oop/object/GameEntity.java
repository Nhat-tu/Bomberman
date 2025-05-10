package com.uet.oop.object;


public abstract class GameEntity {
    protected Position position;
    protected double movementSpeed;
    protected int hitPoints;

    public abstract void movement();
    public abstract void takeDamage();
    public abstract void die();


    public GameEntity(Position position, double movementSpeed, int hitPoints) {
        this.position = position;
        this.movementSpeed = movementSpeed;
        this.hitPoints = hitPoints;
    }
}