package com.uet.oop.object;


import java.util.ArrayList;
import java.util.List;

public class Player extends GameEntity{
    private int bombs;
    private int explosionRadius;
    private int invulnerabilityTimer;
    private List<PowerUp> currentPowerups;

    public Player(Position position, int movementSpeed, int hitPoints) {
        super(position, movementSpeed, hitPoints);
        bombs = 1;
        explosionRadius = 1;
        invulnerabilityTimer = 3;
        currentPowerups = new ArrayList<PowerUp>();
    }

    @Override
    public void movement() {
        /* TODO */
    }

    @Override
    public void takeDamage() {
        /* TODO */
    }

    @Override
    public void die() {
        /* TODO */
    }

    public void placeBombs() {
        /* TODO */
    }

    public void update() {
        /* TODO */
    }
}
