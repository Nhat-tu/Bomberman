package com.uet.oop.object;

import java.awt.Graphics2D;

public class Explosion {
    private Position position;
    private int explosionRadius;
    private int duration;


    public Explosion (Position position, int duration) {
        this.position = position;
        this.duration = duration;
    }

    public void applyDamage(GameEntity gameEntity) {

    }
}
