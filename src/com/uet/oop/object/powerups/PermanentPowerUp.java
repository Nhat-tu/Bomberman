package com.uet.oop.object.powerups;


import com.uet.oop.object.Player;

public abstract class PermanentPowerUp extends PowerUp {
    public PermanentPowerUp(PowerUpType type) {
        super(type);
    }

    @Override
    public abstract void applyPowerUp(Player player);

}
