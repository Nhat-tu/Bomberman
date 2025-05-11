package com.uet.oop.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class GameWindow extends JPanel implements Runnable {
    // Some components
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;

    final int mapCol = 16; // Grid-base mapping
    final int mapRow = 12;
    final int screenWidth = mapCol * tileSize;
    final int screenHeight = mapRow * tileSize;
    // FPS
    final int FPS = 30;

    // multiple threads: for later or never
    Thread gameThread;
    // key handler
    KeyboardHandler keyHandler = new KeyboardHandler();
    // test purpose
    int rectX = screenWidth / 2 - tileSize / 2;
    int rectY = screenHeight / 2 - tileSize / 2;
    int rectSpd = 10;

    /**
     * constructor.
     */
    public GameWindow() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GREEN);
        this.setDoubleBuffered(true); // idk how but this improves rendering performance.
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

    }

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
        if (keyHandler.isKeyPressed(KeyEvent.VK_W)) {
            rectY -= rectSpd;
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_S)) {
            rectY += rectSpd;
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_A)) {
            rectX -= rectSpd;
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_D)) {
            rectX += rectSpd;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gain more 2D-centered control over graphic
        Graphics2D g2d = (Graphics2D) g; //Tab Tab
        g2d.setColor(Color.MAGENTA);
        g2d.fillRect(rectX, rectY, tileSize, tileSize);
        g2d.dispose();
    }
}
