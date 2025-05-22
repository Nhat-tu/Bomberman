package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.core.KeyboardHandler;
import com.uet.oop.object.powerups.PowerUp;
import com.uet.oop.object.powerups.TemporaryPowerUp;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Player extends GameEntity {
    private Position screenPosition;
    private int bombs;
    private int explosionRadius;
    private int invulnerabilityTimer;
    public List<Bomb> currentBombs;
    public List<PowerUp> currentPowerups;

    GameWindow gw;
    KeyboardHandler keyH;
    private TextureManager textureManager;

    private boolean messageShown = false;
    private boolean isAlive;

    public Player(GameWindow gw, KeyboardHandler keyH, TextureManager textureManager) /*, TileManager tileManager)*/ {
        setDefaultValues();
        this.currentAnimation = null;
        this.gw = gw;
        this.keyH = keyH;
        this.textureManager = textureManager;
        this.currentBombs = new ArrayList<>();
        this.currentPowerups = new ArrayList<>();
        this.animations = new HashMap<>();
        this.screenPosition = new Position(gw.screenWidth / 2 - gw.tileSize / 2, gw.screenHeight / 2 - gw.tileSize / 2); // centered
        setupAnimation();
    }

    public void setDefaultValues() {
        this.mapPosition = new Position(48,48);
        this.movementSpeed = 4;
        this.hitPoints = 1;
        this.bombs = 1;
        this.explosionRadius = 1;
        this.invulnerabilityTimer = 3000; // ms
        this.hitRect = new Rectangle (
                0,
                0,
                38,
                40
        );
        this.isAlive = true;
    }

    public void setupAnimation() {
        int framesPerMoveAnimation = 4;
        int framesPerDeathAnimation = 7;

// ------------------------------------
        // move left anim
        BufferedImage[] moveLeftFrames = new BufferedImage[framesPerMoveAnimation];

        moveLeftFrames[0] = textureManager.getTexture("player_left_1.png");
        moveLeftFrames[1] = textureManager.getTexture("player_left.png");
        moveLeftFrames[2] = textureManager.getTexture("player_left_2.png");
        moveLeftFrames[3] = textureManager.getTexture("player_left.png");

        Animation moveLeftAnimation = new Animation(moveLeftFrames, 150, true);
        animations.put("moveLeftAnimation", moveLeftAnimation);

        // move right anim
        BufferedImage[] moveRightFrames = new BufferedImage[framesPerMoveAnimation];

        moveRightFrames[0] = textureManager.getTexture("player_right_1.png");
        moveRightFrames[1] = textureManager.getTexture("player_right.png");
        moveRightFrames[2] = textureManager.getTexture("player_right_2.png");
        moveRightFrames[3] = textureManager.getTexture("player_right.png");

        Animation moveRightAnimation = new Animation(moveRightFrames, 150, true);
        animations.put("moveRightAnimation", moveRightAnimation);

        // move up anim
        BufferedImage[] moveUpFrames = new BufferedImage[framesPerMoveAnimation];

        moveUpFrames[0] = textureManager.getTexture("player_up_1.png");
        moveUpFrames[1] = textureManager.getTexture("player_up.png");
        moveUpFrames[2] = textureManager.getTexture("player_up_2.png");
        moveUpFrames[3] = textureManager.getTexture("player_up.png");

        Animation moveUpAnimation = new Animation(moveUpFrames, 150, true);
        animations.put("moveUpAnimation", moveUpAnimation);

        // move down anim
        BufferedImage[] moveDownFrames = new BufferedImage[framesPerMoveAnimation];

        moveDownFrames[0] = textureManager.getTexture("player_down_1.png");
        moveDownFrames[1] = textureManager.getTexture("player_down.png");
        moveDownFrames[2] = textureManager.getTexture("player_down_2.png");
        moveDownFrames[3] = textureManager.getTexture("player_down.png");

        Animation moveDownAnimation = new Animation(moveDownFrames, 150, true);
        animations.put("moveDownAnimation", moveDownAnimation);

        // death
        BufferedImage[] deathFrames = new BufferedImage[framesPerDeathAnimation];
        // test prefix method
        String prefix = "player_dead_";
        for (int i = 0; i < framesPerDeathAnimation - 1; i++) {
            deathFrames[i] = textureManager.getTexture(prefix + i + ".png");
        }
        Animation deathAnimation = new Animation(deathFrames, 300, false);
        animations.put("deathAnimation", deathAnimation);
//-------------------------------------
        setAnimation("moveRightAnimation"); // default
    }

    public void setAnimation(String animationName) {
        Animation nextAnimation = animations.get(animationName);
        if (nextAnimation != null && !nextAnimation.equals(currentAnimation)) {
            if (currentAnimation != null) {
                currentAnimation.stop();
            }
            currentAnimation = nextAnimation;
            currentAnimation.resetAndStart();

        } else if (nextAnimation != null && nextAnimation.equals(currentAnimation)) {
            if (!currentAnimation.isRunning()) {
                currentAnimation.start();
            }
        } else {
            System.err.println("Couldn't find player's animation: " + animationName);
            currentAnimation = null;
        }
    }

    public void update() {
        // update and remove done exploded bomb
        currentBombs.removeIf(bomb -> {
            bomb.update();
            if (bomb.isExplosionComplete()) {
                gw.renderManager.removeRenderable(bomb);
                return true;
            }
            return false;
        });

        if (isAlive) {
            movement();
            updatePowerup();
        } else {
            currentAnimation.update();
            if (!currentAnimation.isRunning()) {
                setDefaultValues();
                setAnimation("moveRightAnimation");
            }
        }
    }

    @Override
    public void movement() {
        int newX = mapPosition.getX();
        int newY = mapPosition.getY();
// Add at the beginning of movement()
        final double DIAGONAL_FACTOR = 0.707; // approximately 1/√2

// Then modify the movement speed when moving diagonally
        if (keyH.isKeyPressed(KeyEvent.VK_W) && (keyH.isKeyPressed(KeyEvent.VK_A) || keyH.isKeyPressed(KeyEvent.VK_D)) ||
                keyH.isKeyPressed(KeyEvent.VK_S) && (keyH.isKeyPressed(KeyEvent.VK_A) || keyH.isKeyPressed(KeyEvent.VK_D))) {
            int diagonalSpeed = (int)(movementSpeed * DIAGONAL_FACTOR);
            // Use diagonalSpeed instead of movementSpeed for both X and Y
            if (keyH.isKeyPressed(KeyEvent.VK_W)) newY -= diagonalSpeed;
            if (keyH.isKeyPressed(KeyEvent.VK_S)) newY += diagonalSpeed;
            if (keyH.isKeyPressed(KeyEvent.VK_A)) newX -= diagonalSpeed;
            if (keyH.isKeyPressed(KeyEvent.VK_D)) newX += diagonalSpeed;
        } else {

            if (keyH.isKeyPressed(KeyEvent.VK_W)) {
                setAnimation("moveUpAnimation");
                newY -= movementSpeed;
                currentAnimation.update();
            } if (keyH.isKeyPressed(KeyEvent.VK_S)) {
                setAnimation("moveDownAnimation");
                newY += movementSpeed;
                currentAnimation.update();
            } if (keyH.isKeyPressed(KeyEvent.VK_A)) {
                setAnimation("moveLeftAnimation");
                newX -= movementSpeed;
                currentAnimation.update();
            } if (keyH.isKeyPressed(KeyEvent.VK_D)) {
                setAnimation("moveRightAnimation");
                newX += movementSpeed;
                currentAnimation.update();
            } if (keyH.isKeyPressed(KeyEvent.VK_SPACE)) {
                placeBombs();
            }
        }


        // Add boundary checks
        newX = Math.max(0, Math.min(newX, gw.mapWidth - gw.tileSize));
        newY = Math.max(0, Math.min(newY, gw.mapHeight - gw.tileSize));


        Rectangle playerMapRect = new Rectangle(
                newX + hitRect.x,
                newY + hitRect.y,
                hitRect.width,
                hitRect.height
        );

        if (!gw.tileManager.CheckCollision(playerMapRect)) {
            setMapPosition(new Position(newX, newY));
        }

    }

    public void placeBombs() {
        if (currentBombs.size() >= bombs) {
            return;
        }
        int bombX = (int) (Math.round( (double)getMapPosition().getX() / gw.tileSize ) * gw.tileSize);
        int bombY = (int) (Math.round( (double)getMapPosition().getY() / gw.tileSize ) * gw.tileSize);
        Position bombPosition = new Position(bombX, bombY);

        for (Bomb bomb : currentBombs) {
            if (bomb.getMapPosition().equals(bombPosition)) {
                return;
            }
        }

        Bomb newBomb = new Bomb(bombPosition, explosionRadius, this, gw, textureManager);
        currentBombs.add(newBomb);
        gw.renderManager.addRenderable(newBomb);
    }

    @Override
    public void takeDamage() {
        hitPoints -= 1;
        System.out.println("Player hit!, hitPoints left: " + hitPoints);
        die();
    }

    @Override
    public void die() { //
        isAlive = false;
        setAnimation("deathAnimation");
        /* reset player state, position */

        if (hitPoints == 0) {
            if (!messageShown) {
                System.out.println("GAME OVER!");
                messageShown = true;
            }
        }
    }

    public void updatePowerup() {
        Iterator<PowerUp> iterator = currentPowerups.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (powerUp instanceof TemporaryPowerUp tempPowerUp) {
                if (System.currentTimeMillis() - tempPowerUp.getStartTime() > tempPowerUp.getDuration()) {
                    tempPowerUp.removePowerUp(this, iterator);
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        BufferedImage frameToRender = null;
        if (currentAnimation != null) {
            frameToRender = currentAnimation.getCurrentFrame();
        }
        if (frameToRender != null) {
            // if the player goes past designated screenX and screenY (the screen center)
            // fix it in the screen center
            // otherwise, move freely
            // see also draw() in TileManager
            int x = getScreenPosition().getX();
            int y = getScreenPosition().getY();

            if (x > getMapPosition().getX()) { // to the left edge
                x = getMapPosition().getX();
            }
            if (y > getMapPosition().getY()) { // to the upper edge
                y = getMapPosition().getY();
            }
            // to the right edge
            int rightOffset = gw.screenWidth - getScreenPosition().getX();
            if (rightOffset > gw.mapWidth - getMapPosition().getX()) {
                x = gw.screenWidth - (gw.mapWidth - getMapPosition().getX());
            }
            // to the bottom edge
            int bottomOffset = gw.screenHeight - getScreenPosition().getY();
            if (bottomOffset > gw.mapHeight - getMapPosition().getY()) {
                y = gw.screenHeight - (gw.mapHeight - getMapPosition().getY());
            }

            g.drawImage(frameToRender, x, y, gw.tileSize - 6, gw.tileSize - 6,null);
        } else { // if fails then
            g.setColor(new Color(80, 160, 0));
            g.fillRect(screenPosition.getX(), screenPosition.getY(), gw.tileSize, gw.tileSize);
        }
    }

    public Position getScreenPosition() {
        return screenPosition;
    }

    public int getMaxBombs() {
        return bombs;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void setMaxBombs(int maxBomb) {
        this.bombs = maxBomb;
    }

    public void setExplosionRadius(int explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

}
