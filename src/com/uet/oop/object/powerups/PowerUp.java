package com.uet.oop.object.powerups;

import com.uet.oop.object.Player;
import com.uet.oop.object.Position;

public abstract class PowerUp {
    protected Position position;

    public enum PowerUpType {
        // Classic power-ups
        NULL,
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

    public PowerUp(PowerUpType type) {
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void applyPowerup(Player player);
    // see also: TileManager.java >> CheckCollision.
    public void addPowerUp(Player player) {
        player.currentPowerups.add(this);
        applyPowerup(player);
    }

}
