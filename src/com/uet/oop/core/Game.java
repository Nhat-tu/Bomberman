package com.uet.oop.core;


import javax.swing.JFrame;


public enum Game {
    INSTANCE;
    JFrame window;
    Game() {
        window = new JFrame();
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
        System.out.println("Game initialized");
    }

    public void update() {

    }

    public void render() {

    }
}
