package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

public class SpeedPowerUp extends TemporaryPowerUp{
    public SpeedPowerUp(PowerUpType type) {
        super(type);
    }

    @Override
    public void applyPowerup(Player player) {

    }

    @Override
    public void removePowerUp(Player player) {
        player.currentPowerups.remove(this);
    }
}
