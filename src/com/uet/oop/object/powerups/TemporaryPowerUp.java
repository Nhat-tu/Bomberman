package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

import java.util.Iterator;

public abstract class TemporaryPowerUp extends PowerUp {
    private long startTime;
    private int duration;


    public TemporaryPowerUp(PowerUpType type) {
        super(type);
        this.duration = 15000; // ms
    }

    public int getDuration() {
        return duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public abstract void removePowerUp(Player player, Iterator<PowerUp> iterator);
}
