package com.uet.oop.object;


public abstract class TemporaryPowerUp extends PowerUp {
    private int duration;


    public TemporaryPowerUp(PowerUpType type, Position position) {
        super(type, position);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public abstract void applyPowerUp(Player player);

    public abstract void removePowerUp(Player player);
}
