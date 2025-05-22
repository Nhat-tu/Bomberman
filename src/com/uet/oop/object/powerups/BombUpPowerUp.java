package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

public class BombUpPowerUp extends PermanentPowerUp {
    public BombUpPowerUp (PowerUpType type) {
        super(type);
    }

    public void applyPowerup(Player player) {
        if (player.currentBombs.size() < 3) {
            int currentBomb = player.getMaxBombs();
            player.setMaxBombs(currentBomb + 1);
        }
    }
}
