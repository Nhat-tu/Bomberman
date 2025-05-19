package com.uet.oop.map;

import com.uet.oop.core.GameWindow;
import com.uet.oop.object.powerups.*;
import com.uet.oop.rendering.TextureManager;
import com.uet.oop.object.Position;

import java.awt.Graphics2D;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TileManager {
    private Map<Character, Tile> tiles; // stores all types of Tile for use
    private char[][] charMap;

    GameWindow gw;
    TextureManager textureManager;

    public TileManager(GameWindow gw, TextureManager textureManager) {
        this.gw = gw;
        this.textureManager = textureManager;
        setDefault();
        getTiles(); // load all types of Tiles
        loadMap();
    }

    private void setDefault() {
        this.tiles = new HashMap<>();
        this.charMap = new char[gw.mapRow][gw.mapCol];
    }

    public void getTiles() {
        tiles.put(' ', new GrassTile(gw, textureManager));
        tiles.put('#', new IndestructibleTile(gw, textureManager));
        tiles.put('*', new DestructibleTile(new NoPowerUp(PowerUp.PowerUpType.NULL), gw, textureManager));
        tiles.put('x', new IndestructibleTile(gw, textureManager));
        tiles.put('b', new DestructibleTile(new BombUpPowerUp(PowerUp.PowerUpType.BOMB_UP), gw, textureManager));
        tiles.put('f', new DestructibleTile(new FireUpPowerUp(PowerUp.PowerUpType.FIRE_UP), gw, textureManager));
        tiles.put('s', new DestructibleTile(new SpeedPowerUp(PowerUp.PowerUpType.SPEED_UP), gw, textureManager));
    }

    public void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/levels/Level0.txt");
            if (is == null) {
                throw new Exception("Unable to fetch from res/levels/Level0.txt");
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

    public void draw(Graphics2D g) {
        for (int i = 0; i < gw.mapRow; i++) {
            for (int j = 0; j < gw.mapCol; j++) {
                Tile currentTile = tiles.get(charMap[i][j]);

                int mapX = j * gw.tileSize;
                int mapY = i * gw.tileSize;
                int screenX = mapX - ( gw.player.getMapPosition().getX() - gw.player.getScreenPosition().getX() ); // offset the position
                int screenY = mapY - ( gw.player.getMapPosition().getY() - gw.player.getScreenPosition().getY() );

                // stop moving camera at the edge
                // see also draw() in Player
                if (gw.player.getScreenPosition().getX() > gw.player.getMapPosition().getX()) { // to the left edge
                    screenX = mapX;
                }
                if (gw.player.getScreenPosition().getY() > gw.player.getMapPosition().getY()) { // to the upper edge
                    screenY = mapY;
                }
                // to the right edge
                int rightOffset = gw.screenWidth - gw.player.getScreenPosition().getX();
                if (rightOffset > gw.mapWidth - gw.player.getMapPosition().getX()) {
                    screenX = gw.screenWidth - (gw.mapWidth - mapX);
                }
                // to the bottom edge
                int bottomOffset = gw.screenHeight - gw.player.getScreenPosition().getY();
                if (bottomOffset > gw.mapHeight - gw.player.getMapPosition().getY()) {
                    screenY = gw.screenHeight - (gw.mapHeight - mapY);
                }

                currentTile.setPosition(new Position(screenX, screenY));
                currentTile.draw(g);
            }
        }
    }
}
