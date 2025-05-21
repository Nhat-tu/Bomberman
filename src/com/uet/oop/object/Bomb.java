package com.uet.oop.object;

import com.uet.oop.core.GameWindow;
import com.uet.oop.map.Tile;
import com.uet.oop.rendering.Animation;
import com.uet.oop.rendering.Renderable;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Bomb implements Renderable {
    private Position mapPosition;
    private long plantTime;
    private int fuseTime;
    private int explosionRadius;
    private GameEntity ownerEntity;
    private Map<String, Animation> animations;
    Animation currentAnimation;
    private boolean exploded;
    private boolean explosionComplete;
    private List<Rectangle> explosionAreas;
    private List<Position> destroyedTilePositions;
    private static final int EXPLOSION_DURATION = 1000;
    private long explosionStartTime;

    GameWindow gw;
    TextureManager textureManager;

    public Bomb(Position position, int explosionRadius, GameEntity ownerEntity, GameWindow gw, TextureManager textureManager) {
        this.mapPosition = position;
        this.plantTime = System.currentTimeMillis();
        this.fuseTime = 2100;
        this.explosionRadius = explosionRadius;
        this.ownerEntity = ownerEntity;
        this.exploded = false;
        this.explosionComplete = false;
        this.animations = new HashMap<>();
        this.explosionAreas = new ArrayList<>();
        this.destroyedTilePositions = new ArrayList<>();

        this.gw = gw;
        this.textureManager = textureManager;
        this.currentAnimation = null;
        setupAnimation();
    }

    public void setupAnimation() {
        int pendingExplosionFrames = 4;
        int explosionFrames = 3;
// ------------------------------------
        // pending explosion
        BufferedImage[] exploding = new BufferedImage[pendingExplosionFrames];

        exploding[0] = textureManager.getTexture("bomb.png");
        exploding[1] = textureManager.getTexture("bomb_1.png");
        exploding[2] = textureManager.getTexture("bomb_2.png");
        exploding[3] = textureManager.getTexture("bomb_1.png");

        Animation explodingAnimation = new Animation(exploding, 300, true);
        animations.put("explodingAnimation", explodingAnimation);

        // exploded
        BufferedImage[] exploded = new BufferedImage[explosionFrames];
        exploded[0] = textureManager.getTexture("bomb_exploded_0.png");
        exploded[1] = textureManager.getTexture("bomb_exploded_1.png");
        exploded[2] = textureManager.getTexture("bomb_exploded_2.png");

        Animation explodedAnimation = new Animation(exploded, 200, false);
        animations.put("explodedAnimation", explodedAnimation);
//-------------------------------------
        setAnimation("explodingAnimation"); // default
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
            System.err.println("Couldn't find bomb's animation: " + animationName);
            currentAnimation = null;
        }
    }

    public void update() {
        if (currentAnimation != null) {
            currentAnimation.update();
        }

        if (!exploded && System.currentTimeMillis() - plantTime >= fuseTime) {
            explode();
        }

        if (exploded && !explosionComplete) {
            if (System.currentTimeMillis() - explosionStartTime >= EXPLOSION_DURATION) {
                explosionComplete = true;
                // destroy tiles
                for (Position pos : destroyedTilePositions) {
                    gw.tileManager.destroyTile(pos);
                }
            }
        }
    }

    public void explode() {
        if (exploded) { return; }

        exploded = true;
        explosionStartTime = System.currentTimeMillis();
        createExplosionArea();
        checkDamage();
        setAnimation("explodedAnimation");
        checkChainExplosion();
    }

    public void checkDamage() {
        Rectangle playerHitRect = new Rectangle(
                gw.player.getMapPosition().getX() + gw.player.hitRect.x,
                gw.player.getMapPosition().getY() + gw.player.hitRect.y,
                gw.player.hitRect.width,
                gw.player.hitRect.height
        );

        for (Rectangle explosionArea : explosionAreas) {
            if (checkCollision(playerHitRect, explosionArea)) {
                gw.player.takeDamage();
                break;
            }
        }
    }

    public void checkChainExplosion() {
        if (gw.player.currentBombs != null) {
            for (Bomb bomb : gw.player.currentBombs) {
                if (bomb != this && !bomb.isExploded()) {
                    Rectangle bombRect = new Rectangle(
                            bomb.mapPosition.getX(),
                            bomb.mapPosition.getY(),
                            gw.tileSize,
                            gw.tileSize
                    );

                    for (Rectangle explosionArea : explosionAreas) {
                        if (checkCollision(explosionArea, bombRect)) {
                            bomb.explode();
                            break;
                        }
                    }
                }
            }
        }
    }
    public void createExplosionArea() {
        explosionAreas.clear();

        Rectangle center = new Rectangle(
                mapPosition.getX(),
                mapPosition.getY(),
                gw.tileSize,
                gw.tileSize
        );
        explosionAreas.add(center);

        int[] dx = {1, -1, 0, 0}; // right, left, down. up
        int[] dy = {0, 0, 1, -1};

        for (int dir = 0; dir < 4; dir++) {
            for (int rad = 1; rad <= explosionRadius; rad++) {
                int newX = mapPosition.getX() + (rad * dx[dir] * gw.tileSize);
                int newY = mapPosition.getY() + (rad * dy[dir] * gw.tileSize);

                if (newX < 0 || newX >= gw.mapWidth || newY < 0 || newY >= gw.mapHeight) {
                    break;
                }

                Position tilePos = new Position(newX, newY);
                Tile tile = gw.tileManager.getTileAt(tilePos);
                if (tile != null) {
                    // add explosion area
                    Rectangle explosionArea = new Rectangle(newX, newY, gw.tileSize, gw.tileSize);
                    explosionAreas.add(explosionArea);

                    // handle types of tile
                    if (tile.getTileType() == Tile.TileType.INDESTRUCTIBLE) {
                        break;
                    } else if (tile.getTileType() == Tile.TileType.DESTRUCTIBLE) {
                        destroyedTilePositions.add(tilePos);
                        break;
                    }
                }
            }
        }
    }

    public boolean checkCollision(Rectangle explosionArea, Rectangle objectRect) {
        return explosionArea.intersects(objectRect);
    }


    @Override
    public void draw(Graphics2D g) {
        BufferedImage frameToRender = null;
        if (currentAnimation != null) {
            frameToRender = currentAnimation.getCurrentFrame();
        }
        if (frameToRender != null) {
            // Convert map position to screen position
            int screenX = mapPosition.getX() - (gw.player.getMapPosition().getX() - gw.player.getScreenPosition().getX());
            int screenY = mapPosition.getY() - (gw.player.getMapPosition().getY() - gw.player.getScreenPosition().getY());

            // Apply the same edge-constraints as player
            if (gw.player.getScreenPosition().getX() > gw.player.getMapPosition().getX()) {
                screenX = mapPosition.getX();
            }
            if (gw.player.getScreenPosition().getY() > gw.player.getMapPosition().getY()) {
                screenY = mapPosition.getY();
            }
            int rightOffset = gw.screenWidth - gw.player.getScreenPosition().getX();
            if (rightOffset > gw.mapWidth - gw.player.getMapPosition().getX()) {
                screenX = gw.screenWidth - (gw.mapWidth - mapPosition.getX());
            }
            int bottomOffset = gw.screenHeight - gw.player.getScreenPosition().getY();
            if (bottomOffset > gw.mapHeight - gw.player.getMapPosition().getY()) {
                screenY = gw.screenHeight - (gw.mapHeight - mapPosition.getY());
            }


            g.drawImage(frameToRender, screenX, screenY, gw.tileSize, gw.tileSize,null);

            // draw explosion area
            if (exploded && !explosionComplete) {
                g.setColor(new Color(255, 140, 0, 200));
                for (Rectangle area : explosionAreas) {
                    int areaScreenX = area.x - (gw.player.getMapPosition().getX() - gw.player.getScreenPosition().getX());
                    int areaScreenY = area.y - (gw.player.getMapPosition().getY() - gw.player.getScreenPosition().getY());

                    if (gw.player.getScreenPosition().getX() > gw.player.getMapPosition().getX()) {
                        areaScreenX = area.x;
                    }
                    if (gw.player.getScreenPosition().getY() > gw.player.getMapPosition().getY()) {
                        areaScreenY = area.y;
                    }
                    if (rightOffset > gw.mapWidth - gw.player.getMapPosition().getX()) {
                        areaScreenX = gw.screenWidth - (gw.mapWidth - area.x);
                    }
                    if (bottomOffset > gw.mapHeight - gw.player.getMapPosition().getY()) {
                        areaScreenY = gw.screenHeight - (gw.mapHeight - area.y);
                    }

                    g.fillRect(areaScreenX, areaScreenY, area.width, area.height);
                }
            }
        } else { // if fails then
            g.setColor(Color.WHITE);
            g.fillRect(mapPosition.getX(), mapPosition.getY(), gw.tileSize, gw.tileSize);
        }
    }

    public boolean isExploded() {
        return exploded;
    }

    public boolean isExplosionComplete() {
        return explosionComplete;
    }

    public List<Rectangle> getExplosionAreas() {
        return explosionAreas;
    }

    public Position getMapPosition() {
        return mapPosition;
    }
}
