package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

public class NoPowerUp extends PowerUp {
    public NoPowerUp(PowerUpType type) {
        super(type);
    }

    @Override
    public void applyPowerup(Player player) {
        return;
    }

    @Override
    public void addPowerUp(Player player) {
        return;
    }

}
