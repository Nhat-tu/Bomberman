package com.uet.oop.object;

import java.awt.Graphics2D;

public class TemporaryPowerUp extends PowerUp {
    private int duration;


    public TemporaryPowerUp(PowerUpType type, Position position) {
        super(type);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void applyPowerUp(Player player) {

    }

    public void removePowerUp(Player player) {

    }

    public void draw(Graphics2D g) {

    }
}
