package com.uet.oop.object;


public abstract class PermanentPowerUp extends PowerUp {
    public PermanentPowerUp(PowerUpType type, Position position) {
        super(type, position);
    }

    @Override
    public abstract void applyPowerUp(Player player);
}
