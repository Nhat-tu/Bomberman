import java.util.ArrayList;
import java.util.List;

public class Player extends GameEntity{
    Position position;
    private int movementSpeed;
    private int bombs;
    private int explosionRadius;
    private int numberOfLives;
    private int invulnerabilityTimer;
    private List<PowerUp> currentPowerups;

    public Player() {
        position = new Position();
        movementSpeed = 4;
        bombs = 1;
        explosionRadius = 1;
        numberOfLives = 3;
        invulnerabilityTimer = 3;
        currentPowerups = new ArrayList<PowerUp>();
    }

    @Override
    public void movement() {
        /* TODO */
    }

    @Override
    public void takeDamage() {
        /* TODO */
    }

    @Override
    public void die() {
        /* TODO */
    }

    public void placeBombs() {
        /* TODO */
    }

    public void update() {
        /* TODO */
    }
}
