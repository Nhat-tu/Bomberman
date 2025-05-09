import java.util.ArrayList;
import java.util.List;

public class Player {
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

    public void move() {
        /* TODO */
    }

    public void placeBombs() {
        /* TODO */
    }

    public void takeDamage(int damage) {
        /* TODO */
    }

    public void gameOver() {
        /* TODO */
    }

    public void update() {
        /* TODO */
    }
}
