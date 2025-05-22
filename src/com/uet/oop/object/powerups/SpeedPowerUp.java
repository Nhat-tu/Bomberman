package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;

import java.util.Iterator;

public class SpeedPowerUp extends TemporaryPowerUp{
    private final int speedIncrease;
    public SpeedPowerUp(PowerUpType type) {
        super(type);
        this.speedIncrease = 2;
    }

    @Override
    public void applyPowerup(Player player) {
        setStartTime();
        int currentSpeed = player.getMovementSpeed();
        if (currentSpeed < 7) {
            player.setMovementSpeed(currentSpeed + speedIncrease);
        }
    }

    @Override
    public void removePowerUp(Player player, Iterator<PowerUp> iterator) {
        player.setMovementSpeed(player.getMovementSpeed() - speedIncrease);
        iterator.remove();
    }
}
