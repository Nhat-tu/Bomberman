package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;
import com.uet.oop.object.Position;

public abstract class TemporaryPowerUp extends PowerUp {
    private int duration;


    public TemporaryPowerUp(PowerUpType type) {
        super(type);
        this.duration = 15000; // ms
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
