package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

public class FireUpPowerUp extends PermanentPowerUp {
    public FireUpPowerUp(PowerUpType type) {
        super(type);
    }

    @Override
    public void applyPowerup(Player player) {
        int currentExplosionRadius = player.getExplosionRadius();

        if (currentExplosionRadius < 4) {
            player.setExplosionRadius(currentExplosionRadius + 1);
        }
    }

}
