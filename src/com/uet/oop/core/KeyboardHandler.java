package com.uet.oop.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.HashSet;
import java.util.Set;

public class KeyboardHandler implements KeyListener {
    // use hashset for fast add, remove, check exists, NO DUPLICATED KEYS.
    private Set<Integer> pressedKeys = new HashSet<>();

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    } // refer to GameWindow
    /* public boolean wPressed, sPressed , aPressed, dPressed; */

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        /* switch (keyCode) {
            case KeyEvent.VK_W:
                wPressed = true;
                break;
            case KeyEvent.VK_S:
                sPressed = true;
                break;
            case KeyEvent.VK_A:
                aPressed = true;
                break;
            case KeyEvent.VK_D:
                dPressed = true;
                break;
            default:
                break;
        } */
        pressedKeys.add(keyCode);
        System.out.println("Key pressed: " + keyCode);
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        /* switch (keyCode) {
            case KeyEvent.VK_W:
                wPressed = false;
                break;
            case KeyEvent.VK_S:
                sPressed = false;
                break;
            case KeyEvent.VK_A:
                aPressed = false;
                break;
            case KeyEvent.VK_D:
                dPressed = false;
                break;
            default:
                break;
        } */
        pressedKeys.remove(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        /* (NO USAGE. IGNORE) */
    }
}
