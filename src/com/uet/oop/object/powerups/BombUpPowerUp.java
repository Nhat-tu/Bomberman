package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

public class BombUpPowerUp extends PermanentPowerUp {
    public BombUpPowerUp (PowerUpType type) {
        super(type);
    }

    public void applyPowerup(Player player) {
        if (player.currentBombs.size() < player.getMaxBombs()) {
            int currentBomb = player.getMaxBombs();
            if (currentBomb < 3) {
                player.setMaxBombs(currentBomb + 1);
                System.out.println("Player's max bombs increased. Current max bombs: " + player.getMaxBombs());
            }
        }
    }
}
