package com.uet.oop.core;

import com.uet.oop.map.TileManager;
import com.uet.oop.object.Enemy;
import com.uet.oop.object.Balloom;
import com.uet.oop.object.Position;
import com.uet.oop.rendering.TextureManager;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class EnemyHandler {
    public List<Enemy> enemies;
    private GameWindow gw;
    private TextureManager textureManager;
    private TileManager map;

    public EnemyHandler(GameWindow gw, TextureManager textureManager, TileManager tileManager) {
        this.gw = gw;
        this.textureManager = textureManager;
        this.map = tileManager;
        this.enemies = new ArrayList<>();
        loadEnemies();
    }

    public void loadEnemies() {
        for (int i = 0; i < gw.mapRow; i++) {
            for (int j = 0; j < gw.mapCol; j++) {
                char enemyChar = gw.tileManager.charMap[i][j];
                switch (enemyChar) {
                    case '1': // Balloom
                        Balloom balloom = new Balloom(gw, textureManager, map);
                        balloom.setMapPosition(new Position(j * gw.tileSize, i * gw.tileSize));
                        enemies.add(balloom);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void update() {
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive() && !enemy.getCurrentAnimation().isRunning()) {
                // cause the game to crash if kill balloom too early
                // because its death animation is not fully loaded but forced to play
                // safer: create a List of enemies to be removed.
                enemiesToRemove.add(enemy);
            } else {
                enemy.update();
            }
        }

        for (Enemy enemy : enemiesToRemove) {
            enemies.remove(enemy);
            gw.renderManager.removeRenderable(enemy);
        }
    }
}
