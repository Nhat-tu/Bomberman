package com.uet.oop.object;

public class Bomb {
    private Position mapPosition;
    private int fuseTime;
    private int explosionRadius;
    private GameEntity ownerEntity;

    public Bomb(Position position, int fuseTime, int explosionRadius, GameEntity ownerEntity) {
        this.mapPosition = position;
        this.fuseTime = fuseTime;
        this.explosionRadius = explosionRadius;
        this.ownerEntity = ownerEntity;
    }

    public void countDown() {
        /* TODO */
    }

    public void explode() {
        /* TODO */
    }
}
