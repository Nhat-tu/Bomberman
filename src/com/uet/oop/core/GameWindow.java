package com.uet.oop.core;

import com.uet.oop.map.TileManager;
import com.uet.oop.object.Player;
import com.uet.oop.rendering.RenderManager;
import com.uet.oop.rendering.TextureManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;

public class GameWindow extends JPanel implements Runnable {
    // Some components
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    // SCREEN SETTINGS
    public final int screenCol = 15;
    public final int screenRow = 13;
    public final int screenWidth = screenCol * tileSize;
    public final int screenHeight = screenRow * tileSize;
    // FPS
    final int FPS = 30;

    // MAP SETTINGS
    public final int mapCol = 31; // Grid-base mapping
    public final int mapRow = 13;
    public final int mapWidth = mapCol * tileSize;
    public final int mapHeight = mapRow * tileSize;

    KeyboardHandler keyHandler;
    public RenderManager renderManager;
    TextureManager textureManager;
    public TileManager tileManager;
    public Player player;

/*    --- Load game resources --- */
    public void initGame() {
        keyHandler = new KeyboardHandler();
        renderManager = new RenderManager();
        textureManager = new TextureManager();

        this.addKeyListener(keyHandler);

        // can add more
        textureManager.bulkLoadTexture();
        tileManager = new TileManager(this, this.textureManager);
        player = new Player(this, this.keyHandler, this.textureManager); // migrate setUpAnimation to constructor
        tileManager.readyMap();

        // can add more
        renderManager.addRenderable(player);
    }

    /**
     * constructor.
     */
    public GameWindow() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(80, 160, 0));
        this.setDoubleBuffered(true); // idk how but this improves rendering performance.
        this.setFocusable(true);
        initGame();
    }

    Thread gameThread;

    public void startGameThread() {
        gameThread = new Thread(this); // instantiate gameThread
        gameThread.start(); // auto call run()
    }

    @Override
    public void run() {
        double loopInterval = (double) 1000 / FPS;
        double delta = 0;
        long lastTime = System.currentTimeMillis();
        long currentTime;

        // Game Loop
        while (gameThread != null) {
            currentTime = System.currentTimeMillis();
            delta += (currentTime - lastTime) / loopInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint(); // automatically calls paintComponent();
                delta--;
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // convert to 2D graphic
        Graphics2D g2d = (Graphics2D) g;
        // render map first
        tileManager.draw(g2d);
        //add more here
        renderManager.setGraphicContext2d(g2d);
        renderManager.render();
    }
}
