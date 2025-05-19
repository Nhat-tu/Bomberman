package com.uet.oop.object;


import java.awt.Graphics2D;

public class PermanentPowerUp extends PowerUp {
    public PermanentPowerUp(PowerUpType type) {
        super(type);
    }

    @Override
    public void applyPowerUp(Player player) {
        player.currentPowerups.add(this);
    }

    public void draw(Graphics2D g) {

    }
}
