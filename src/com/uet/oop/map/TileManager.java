package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.object.Balloom;
import com.uet.oop.object.Bomb;
import com.uet.oop.object.powerups.*;
import com.uet.oop.rendering.TextureManager;
import com.uet.oop.object.Position;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;

public class TileManager {
    private Map<Character, Tile> tiles; // stores all types of Tile for use
    public char[][] charMap;
    private Position[][] tilePositions;
    public Map<Position, DestructibleTile> destroyingTiles;

    GameWindow gw;
    TextureManager textureManager;

    public TileManager(GameWindow gw, TextureManager textureManager) {
        this.gw = gw;
        this.textureManager = textureManager;
        setDefault();
        getTiles(); // load all types of Tiles
        loadMap();
    }

    public void setDefault() {
        this.tiles = new HashMap<>();
        this.destroyingTiles = new HashMap<>();
        this.charMap = new char[gw.mapRow][gw.mapCol];
        this.tilePositions = new Position[gw.mapRow][gw.mapCol];
    }

    public void getTiles() {
        tiles.put(' ', new GrassTile(gw, textureManager));
        tiles.put('#', new IndestructibleTile(gw, textureManager));
        tiles.put('*', new DestructibleTile(new NoPowerUp(PowerUp.PowerUpType.NULL), gw, textureManager));
        tiles.put('x', new Portal(new NoPowerUp(PowerUp.PowerUpType.NULL), gw, textureManager));
        tiles.put('b', new DestructibleTile(new BombUpPowerUp(PowerUp.PowerUpType.BOMB_UP), gw, textureManager));
        tiles.put('f', new DestructibleTile(new FireUpPowerUp(PowerUp.PowerUpType.FIRE_UP), gw, textureManager));
        tiles.put('s', new DestructibleTile(new SpeedPowerUp(PowerUp.PowerUpType.SPEED_UP), gw, textureManager));
    }

    public void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/levels/Level1.txt");
            if (is == null) {
                throw new Exception("Unable to fetch from res/levels/Level1.txt");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            while (row < gw.mapRow) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    continue;
                }
                charMap[row] = line.toCharArray();
                row++;
            }

            br.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getCause();
        }
    }

    public boolean CheckCollision(Rectangle objectRect) {
        for (Bomb bomb : gw.player.currentBombs) {
            Rectangle bombRect = new Rectangle(
                    bomb.getMapPosition().getX() + bomb.getHitRect().x,
                    bomb.getMapPosition().getY() + bomb.getHitRect().y,
                    bomb.getHitRect().width,
                    bomb.getHitRect().height
            );

            if (!bomb.getEntityCanPass() && bombRect.intersects(objectRect)) {
                return true;
            }
        }

        for (int row = 0; row < gw.mapRow; row++ ) {
            for (int col = 0; col < gw.mapCol; col++) {
                Position mapPos = new Position(col * gw.tileSize, row * gw.tileSize);
                Tile currentTile = getTileAt(mapPos);

                if (currentTile.getTileType() != Tile.TileType.PASSABLE) {

                    Rectangle mapTileRect = new Rectangle(
                            mapPos.getX() + currentTile.hitRect.x,
                            mapPos.getY() + currentTile.hitRect.y,
                            currentTile.hitRect.width,
                            currentTile.hitRect.height
                    );

                    if (mapTileRect.intersects(objectRect)) {
                        if (currentTile.getTileType() == Tile.TileType.HAS_POWERUP) {
                            gw.audioManager.playSound("collect_powerup.wav");
                            // get tile's powerup
                            if (destroyingTiles.containsKey(mapPos)) {
                                DestructibleTile powerupTile = destroyingTiles.get(mapPos);
                                powerupTile.potentialPowerUp.addPowerUp(gw.player);
                                destroyingTiles.remove(mapPos);
                                charMap[row][col] = ' ';
                            }
                            return false; // allow movement to this powerup tile.
                        } else if (currentTile.getTileType() == Tile.TileType.PORTAL &&
                                currentTile.animations.get("displayPortal") == currentTile.currentAnimation) {
                            return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Tile getTileAt(Position position) {
        int tileRow = position.getY() / gw.tileSize;
        int tileCol = position.getX() / gw.tileSize;

        // serve the purpose to remove powerup when collected
        if (destroyingTiles.containsKey(position)) {
            return destroyingTiles.get(position);
        }

        char tileChar = charMap[tileRow][tileCol];

        // If it's an enemy position, treat it as a grass tile for movement purposes
        if (tileChar == '1' || tileChar == 'p') {
            return tiles.get(' '); // Return grass tile for enemy positions
        }

        // instantiate a new instance of Tile instead of using the same GLOBAL instance in Map<char,Tile> tiles.
        // separately handle each tile's own animation by the new instance, not GLOBAL instance in tiles
        // NOTE: instances in tiles only serve map-drawing
        Tile result = switch (tileChar) {
            case '#' -> new IndestructibleTile(gw, textureManager);
            case '*' ->
                    new DestructibleTile(new NoPowerUp(PowerUp.PowerUpType.NULL), gw, textureManager);
            case 'x' -> new Portal(new NoPowerUp(PowerUp.PowerUpType.NULL), gw, textureManager);
            case 'b' ->
                    new DestructibleTile(new BombUpPowerUp(PowerUp.PowerUpType.BOMB_UP), gw, textureManager);
            case 'f' ->
                    new DestructibleTile(new FireUpPowerUp(PowerUp.PowerUpType.FIRE_UP), gw, textureManager);
            case 's' ->
                    new DestructibleTile(new SpeedPowerUp(PowerUp.PowerUpType.SPEED_UP), gw, textureManager);
            default -> tiles.get(tileChar);
        };

        if (result != null) {
            result.setPosition(tilePositions[tileRow][tileCol]);
        }
        return result;
    }

    // used when the player picks up Powerup or bomb has done explosion.
    // D.Tile destroyed() -> isDestroyed = true && type changes to PASSABLE if it has no powerup
    // if (player collides DTile && DTile.isDestroyed) destroyTile(Tile.getPosition)
    public void destroyTile(Position tilePosition) {
        Tile destroyedTile = getTileAt(tilePosition);
        if (destroyedTile.getTileType() == Tile.TileType.DESTRUCTIBLE ||
            destroyedTile.getTileType() == Tile.TileType.HAS_POWERUP) {
            DestructibleTile tempDestructible = (DestructibleTile) destroyedTile;
            tempDestructible.destroyed();
            destroyingTiles.put(tilePosition, tempDestructible);
        } else if (destroyedTile.getTileType() == Tile.TileType.PORTAL) {
            Portal tempPortalTile = (Portal) destroyedTile;
            tempPortalTile.destroyed();
            destroyingTiles.put(tilePosition, tempPortalTile);
        }
    }

    public void readyMap() {
        for (int i = 0; i < gw.mapRow; i++) {
            for (int j = 0; j < gw.mapCol; j++) {

                int mapX = j * gw.tileSize;
                int mapY = i * gw.tileSize;
                int screenX = mapX - ( gw.player.getMapPosition().getX() - gw.player.getScreenPosition().getX() ); // offset the position
                int screenY = mapY - ( gw.player.getMapPosition().getY() - gw.player.getScreenPosition().getY() );

                // stop moving camera at the edge
                // see also draw() in Player
                if (gw.player.getScreenPosition().getX() > gw.player.getMapPosition().getX()) { // past the left edge
                    screenX = mapX;
                }
                if (gw.player.getScreenPosition().getY() > gw.player.getMapPosition().getY()) { // past the upper edge
                    screenY = mapY;
                }
                // past the right edge
                int rightOffset = gw.screenWidth - gw.player.getScreenPosition().getX();
                if (rightOffset > gw.mapWidth - gw.player.getMapPosition().getX()) {
                    screenX = gw.screenWidth - (gw.mapWidth - mapX);
                }
                // past the bottom edge
                int bottomOffset = gw.screenHeight - gw.player.getScreenPosition().getY();
                if (bottomOffset > gw.mapHeight - gw.player.getMapPosition().getY()) {
                    screenY = gw.screenHeight - (gw.mapHeight - mapY);
                }

                tilePositions[i][j] = new Position(screenX, screenY);
            }
        }
    }

    public void draw(Graphics2D g) {
        readyMap();
        for (int i = 0; i < gw.mapRow; i++) {
            for (int j = 0; j < gw.mapCol; j++) {
                Position tilePos = new Position(j * gw.tileSize, i * gw.tileSize);
                if (charMap[i][j] == '1' || charMap[i][j] == 'p') {
                    continue;
                }
                // check if the tile is destroyed
                if (destroyingTiles.containsKey(tilePos)) {
                    DestructibleTile destroyingTile = destroyingTiles.get(tilePos);
                    if (destroyingTile != null) {
                        destroyingTile.currentAnimation.update();
                        destroyingTile.setPosition(tilePositions[i][j]);
                        destroyingTile.draw(g);

                        // If animation is complete, remove it and update charMap
                        if (!destroyingTile.currentAnimation.isRunning()) {
                            if (destroyingTile.getTileType() == Tile.TileType.PASSABLE) {
                                charMap[i][j] = ' ';
                                destroyingTiles.remove(tilePos);
                            } else {
                                if (destroyingTile.getTileType() == Tile.TileType.HAS_POWERUP) {
                                    // powerup tile remains until player picks it up
                                    destroyingTile.setAnimations("displayPowerup");
                                } else if (destroyingTile.getTileType() == Tile.TileType.PORTAL) {
                                    destroyingTile.setAnimations("displayPortal");
                                }
                            }
                        }
                        continue;
                    }

                }

                // Draw normal tiles
                Tile currentTile = getTileAt(tilePos);
                if (currentTile != null) {
                    currentTile.currentAnimation.update();
                    currentTile.setPosition(tilePositions[i][j]);
                    currentTile.draw(g);
                }
            }
        }
    }
}
