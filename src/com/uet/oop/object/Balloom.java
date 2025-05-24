package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.map.Tile;
import com.uet.oop.map.TileManager;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

public class Balloom extends Enemy {
    private long lastChooseDirectionTime;
    private final int minTimeBetweenDirectionChange = 1500; // ms

    public Balloom(GameWindow gw, TextureManager textureManager, TileManager tileManager) {
        super(gw, textureManager, tileManager);
        this.direction = "right";
        setDefaultValues();
        setupAnimation();
    }

    @Override
    public void setupAnimation() {
        int framesPerMoveAnimation = 4;
        int framesPerDeathAnimation = 7;
//      --------------------
        // baloom left side
        BufferedImage[] moveLeftFrames = new BufferedImage[framesPerMoveAnimation];
        moveLeftFrames[0] = textureManager.getTexture("balloom_left_1.png");
        moveLeftFrames[1] = textureManager.getTexture("balloom_left_2.png");
        moveLeftFrames[2] = textureManager.getTexture("balloom_left_3.png");
        moveLeftFrames[3] = textureManager.getTexture("balloom_left_2.png");
        Animation moveLeftAnimation = new Animation(moveLeftFrames, 150, true);
        animations.put("moveLeftAnimation", moveLeftAnimation);

        // balloom right side
        BufferedImage[] moveRightFrames = new BufferedImage[framesPerMoveAnimation];
        moveRightFrames[0] = textureManager.getTexture("balloom_right_1.png");
        moveRightFrames[1] = textureManager.getTexture("balloom_right_2.png");
        moveRightFrames[2] = textureManager.getTexture("balloom_right_3.png");
        moveRightFrames[3] = textureManager.getTexture("balloom_right_2.png");
        Animation moveRightAnimation = new Animation(moveRightFrames, 150, true);
        animations.put("moveRightAnimation", moveRightAnimation);

        // balloom death
        BufferedImage[] deathFrames = new BufferedImage[framesPerDeathAnimation];
        deathFrames[0] = textureManager.getTexture("balloom_dead.png");
        deathFrames[1] = textureManager.getTexture("balloom_dead.png");
        deathFrames[2] = textureManager.getTexture("balloom_dead.png");
        deathFrames[3] = textureManager.getTexture("mob_dead_1.png");
        deathFrames[4] = textureManager.getTexture("mob_dead_2.png");
        deathFrames[5] = textureManager.getTexture("mob_dead_3.png");
        deathFrames[6] = textureManager.getTexture("mob_dead_4.png");
        Animation deathAnimation = new Animation(deathFrames, 300, false);
        animations.put("deathAnimation", deathAnimation);
//      ----------------------
        setAnimation("moveRightAnimation");
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
            System.err.println("Couldn't find balloom's animation: " + animationName);
            currentAnimation = null;
        }
    }

    public void update() {
        if (isAlive) {
            movement();
        } else {
            currentAnimation.update();
        }
    }

    public void chooseDirection() {
        this.lastChooseDirectionTime = System.currentTimeMillis();

        final int[] dx = {1, -1, 0, 0}; // right, left, down, up
        final int[] dy = {0, 0, 1, -1};

        Vector<Integer> validDirection = new Vector<>();
        for (int i = 0; i < 4; i++) {
            int nextX = mapPosition.getX() + dx[i] * gw.tileSize;
            int nextY = mapPosition.getY() + dy[i] * gw.tileSize;

            if (nextX < 0 || nextX >= gw.mapWidth || nextY < 0 || nextY >= gw.mapHeight) {
                break;
            }

            Position surroundingTilePos = new Position(nextX, nextY);
            Tile tile = gw.tileManager.getTileAt(surroundingTilePos);
            if (tile.getTileType() == Tile.TileType.PASSABLE) {
                validDirection.add(i);
            }
        }

        if (validDirection.isEmpty()) {
            setMapPosition(getMapPosition());
            currentAnimation.update();
            return;
        }

        Random rand = new Random();
        int numDirection = validDirection.get( rand.nextInt(validDirection.size()) ); // take value from 0 to size.

        this.direction = switch (numDirection) {
            case 0 -> "right";
            case 1 -> "left";
            case 2 -> "down";
            case 3 -> "up";
            default -> "";
        };
    }

    @Override
    public void movement() {
        int newX = mapPosition.getX();
        int newY = mapPosition.getY();

        if (direction.equals("up")) {
            setAnimation("moveLeftAnimation");
            newY -= movementSpeed;
            currentAnimation.update();
        } else if (direction.equals("down")) {
            setAnimation("moveRightAnimation");
            newY += movementSpeed;
            currentAnimation.update();
        } else if (direction.equals("left")) {
            setAnimation("moveLeftAnimation");
            newX -= movementSpeed;
            currentAnimation.update();
        } else {
            setAnimation("moveRightAnimation");
            newX += movementSpeed;
            currentAnimation.update();
        }

        Rectangle balloomMapRect = new Rectangle(
                newX + hitRect.x,
                newY + hitRect.y,
                hitRect.width,
                hitRect.height
        );

        if (!gw.tileManager.CheckCollision(balloomMapRect)) {
            setMapPosition(new Position(newX, newY));
        }

        checkCollisionWithPlayer(balloomMapRect, gw.player);

        if (System.currentTimeMillis() - lastChooseDirectionTime >= minTimeBetweenDirectionChange ||
            gw.tileManager.CheckCollision(balloomMapRect)) {
            chooseDirection();
        }
    }

    public void checkCollisionWithPlayer(Rectangle balloomRect, Player player) {
        int newX = Math.max(0, Math.min(player.getMapPosition().getX(), gw.mapWidth - gw.tileSize));
        int newY = Math.max(0, Math.min(player.getMapPosition().getY(), gw.mapHeight - gw.tileSize));

        Rectangle playerMapRect = new Rectangle(
                newX + player.hitRect.x,
                newY + player.hitRect.y,
                hitRect.width,
                hitRect.height
        );

        if (player.isAlive() && balloomRect.intersects(playerMapRect)) {
            player.die();
        }
    }

    public void draw(Graphics2D g) {
        BufferedImage frameToRender = null;
        if (currentAnimation != null) {
            frameToRender = currentAnimation.getCurrentFrame();
        }
        if (frameToRender != null) {
            int balloomScreenX = mapPosition.getX() - ( gw.player.getMapPosition().getX() - gw.player.getScreenPosition().getX() ); // offset the position
            int balloomScreenY = mapPosition.getY() - ( gw.player.getMapPosition().getY() - gw.player.getScreenPosition().getY() );

            // stop moving camera at the edge
            // see also draw() in Player
            if (gw.player.getScreenPosition().getX() > gw.player.getMapPosition().getX()) { // past the left edge
                balloomScreenX = mapPosition.getX();
            }
            if (gw.player.getScreenPosition().getY() > gw.player.getMapPosition().getY()) { // past the upper edge
                balloomScreenY = mapPosition.getY();
            }
            // past the right edge
            int rightOffset = gw.screenWidth - gw.player.getScreenPosition().getX();
            if (rightOffset > gw.mapWidth - gw.player.getMapPosition().getX()) {
                balloomScreenX = gw.screenWidth - (gw.mapWidth - mapPosition.getX());
            }
            // past the bottom edge
            int bottomOffset = gw.screenHeight - gw.player.getScreenPosition().getY();
            if (bottomOffset > gw.mapHeight - gw.player.getMapPosition().getY()) {
                balloomScreenY = gw.screenHeight - (gw.mapHeight - mapPosition.getY());
            }

            g.drawImage(frameToRender, balloomScreenX, balloomScreenY, gw.tileSize, gw.tileSize,null);
            // DEBUG
//            g.setColor(Color.WHITE);
//            g.drawRect(balloomScreenX + hitRect.x, balloomScreenY + hitRect.y, hitRect.width, hitRect.height);

        } else { // if fails then
            g.setColor(new Color(125, 120, 200, 255));
            g.fillRect(mapPosition.getX(), mapPosition.getY(), gw.tileSize, gw.tileSize);
        }
    }
}
