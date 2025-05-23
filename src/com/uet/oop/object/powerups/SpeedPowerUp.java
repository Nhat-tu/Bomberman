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
            System.out.println("Player's movement speed increased. Current speed: " + player.getMovementSpeed());
        }
    }

    @Override
    public void removePowerUp(Player player, Iterator<PowerUp> iterator) {
        player.setMovementSpeed(4); // Set to default speed to avoid speed lower than default when player's dead before the power up's duration/
        iterator.remove();
        System.out.println("Player's movement speed is restored to default. Current speed: " + player.getMovementSpeed());
    }
}
