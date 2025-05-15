package com.uet.oop.core;


import javax.swing.JFrame;


public enum Game {
    INSTANCE;
    JFrame window;
    Game() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GameWindow gameWindow = new GameWindow();
        window.add(gameWindow); // add GameWindow to window's contentPane, display purpose.

        window.pack(); // resize this fking window to fit the size and layout of its contents (in this case, JPanel GameWindow)
        window.setLocationRelativeTo(null);
        window.setTitle("Bomberman v0.1");
        window.setVisible(true);

        gameWindow.requestFocusInWindow(); // fcking crucial to receive keyboard input.

        gameWindow.startGameThread();
    }
}
