public class DestructibleBlock {
    private Position position;
    private PowerUp potentialPowerUp;

    public DestructibleBlock(Position position, PowerUp potentialPowerUp) {
        this.position = position;
        this.potentialPowerUp = potentialPowerUp;
    }

    public void destroyed() {
        /* TODO */
    }
}
