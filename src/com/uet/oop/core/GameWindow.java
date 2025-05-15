package com.uet.oop.core;

import com.uet.oop.object.Player;
import com.uet.oop.object.Position;
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

    final int mapCol = 16; // Grid-base mapping
    final int mapRow = 12;
    final int screenWidth = mapCol * tileSize;
    final int screenHeight = mapRow * tileSize;
    // FPS
    final int FPS = 30;

    KeyboardHandler keyHandler;
    RenderManager renderManager;
    TextureManager textureManager;
    Player player;

/*    --- Load game resources --- */
    public void initGame() {
        keyHandler = new KeyboardHandler();
        renderManager = new RenderManager();
        textureManager = new TextureManager();
        this.addKeyListener(keyHandler);

        player = new Player(new Position(), 7, 3, this, keyHandler, this.textureManager);

        textureManager.loadTexture("player_spritesheet", "/player/bomberman_sprite.png");
        player.setupAnimation();

        renderManager.addRenderable(player);

        System.out.println("Game initialized");
    }

    /**
     * constructor.
     */
    public GameWindow() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY);
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

        //add more here
        renderManager.setGraphicContext2d(g2d);
        renderManager.render();

        // renderManager.clearRenderables();
        // g2d.dispose();
    }
}
