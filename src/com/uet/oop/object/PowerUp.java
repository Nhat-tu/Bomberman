package com.uet.oop.object;


public abstract class PowerUp {
    private Position position;
    public enum PowerUpType {
        // Classic power-ups
        BOMB_UP, // Increases the number of bombs that player can place simultaneously.
        FIRE_UP, // Increases explosion radius of the bombs.
        SPEED_UP, // Increases base speed of player.
        FULL_FIRE, // Instantly maximizes the player's bomb explosion radius.
        BOMB_PASS, // Allows player to walk through the bomb.
        FLAME_PASS, // Makes player immune to bomb's explosion.
        WALL_PASS, // Allows player to walk through the destructible wall.

        // Offensive power-ups
        REMOTE_CONTROL, // Allows player to manually detonate their placed bombs.
        PIERCING, // Allows bomb's explosion to travel through multiple destructible blocks.
        SPREAD_BOMB, // Releases several bombs in different directions
        NAPALM_BOMB, // The explosion leaves behind flames that damage enemies passing by.
    }
    private PowerUpType type;

    public PowerUp(PowerUpType type, Position position) {
        this.position = position;
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    public abstract void applyPowerUp(Player player);
}
