public class Player {
    Position position;
    private int movementSpeed;
    private int bombs;
    private int explosionRadius;
    private int numberOfLives;
    private int invulnerabilityTimer;

    public Player() {
        position = new Position();
        movementSpeed = 4;
        bombs = 1;
        explosionRadius = 1;
        numberOfLives = 3;
        invulnerabilityTimer = 3;
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

    public void update() {
        /* TODO */
    }
}
