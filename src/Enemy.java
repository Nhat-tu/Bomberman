import java.util.Random;

public class Enemy extends GameEntity {

    Enemy(Position position, double speed, int HP) {
        super(position, speed, HP);
    }

    /**
     *
     */
    @Override
    public void movement() {
        final int[] dx = {-1, 1, 0, 0};
        final int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = this.position.x + dx[i] /* * Map.tileSize */; // Map class with static attributes: tileSize, width, height
            int newY = this.position.y + dy[i] /* * Map.tileSize */;
            Position newPos = new Position(newX, newY);

            if (newX >= 0 && newY >= 0 /* && newX < Map.width && newY < Map. */) {
                position = newPos;
            }
        }
    }

    /**
     * abcde.
     *
     */
    @Override
    public void takeDamage() {
        HP -= 1;
    }

    /**
     *
     */
    @Override
    public void die() {
        // use hashcode & equals to see which one dies.
        /* List */ /* entities.erase() or something */
    }
}
