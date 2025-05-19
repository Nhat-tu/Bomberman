package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

public class SpeedPowerUp extends TemporaryPowerUp{
    public SpeedPowerUp(PowerUpType type) {
        super(type);
    }


    @Override
    public void applyPowerUp(Player player) {
        player.currentPowerups.add(this);
    }

    @Override
    public void removePowerUp(Player player) {
        player.currentPowerups.remove(this);
    }
}
