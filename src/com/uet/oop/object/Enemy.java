package com.uet.oop.object;


public class Enemy extends GameEntity {
    Enemy(Position position, int speed, int hitPoints) {
        super(position, speed, hitPoints);
    }

    @Override
    public void setupAnimation() {
        /* TODO */
    }

    @Override
    public void setAnimation(String animationName) {
        /* TODO */
    }

    public void loadTexture() {
        /* TODO */
    }

    /**
     *
     */
    @Override
    public void movement() { // ???? should be rebuilt
        final int[] dx = {-1, 1, 0, 0};
        final int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = this.position.getX() + dx[i] /* * Map.tileSize */; // Map class with static attributes: tileSize, width, height
            int newY = this.position.getY() + dy[i] /* * Map.tileSize */;
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
        hitPoints -= 1;
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
