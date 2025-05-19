package com.uet.oop.object;

import java.awt.Graphics2D;

public class TemporaryPowerUp extends PowerUp {
    private int duration;


    public TemporaryPowerUp(PowerUpType type, Position position) {
        super(type);
        this.duration = 15000; // ms
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void applyPowerUp(Player player) {
        player.currentPowerups.add(this);
        /* player collides with it -> start time count */
        // if (time counter == duration) {
        //     removePowerUp(player);
        // }

    }

    public void removePowerUp(Player player) {
        player.currentPowerups.remove(this);
    }

    public void draw(Graphics2D g) {

    }
}
