package com.uet.oop.rendering;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Animation {
    private BufferedImage[] frames;
    private long frameDuration; // ms
    private long lastFrameTime; // the last time frame updates
    private int currentFrameIndex;
    private boolean loop;
    private boolean running; // animation is running?

    public Animation(BufferedImage[] frames, long frameDuration, boolean loop) {
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("Animation must not be null or empty");
        }
        if (frameDuration <= 0) {
            throw new IllegalArgumentException("Frame duration must be greater than zero");
        }

        this.frames = frames;
        this.frameDuration = frameDuration;
        this.lastFrameTime = System.currentTimeMillis();
        this.currentFrameIndex = 0;
        this.loop = loop;
        this.running = false;
    }

    public void update() {
        if (!running || frames.length <= 1) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDuration) {
            currentFrameIndex++;
            lastFrameTime = currentTime;

            if (currentFrameIndex >= frames.length) {
                if (loop) {
                    currentFrameIndex = 0;
                } else {
                    currentFrameIndex = frames.length - 1;
                    running = false;
                }
            }
        }
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrameIndex];
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public void resetAndStart() {
        this.currentFrameIndex = 0;
        this.running = true;
        this.lastFrameTime = System.currentTimeMillis();

    }

    public void reset() {
        this.currentFrameIndex = 0;
        this.running = false;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public boolean isRunning() {
        return running;
    }

    public int getFrameCount() {
        return frames.length;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Animation)) {
            return false;
        }
        if (this == object) {
            return true;
        }

        Animation other = (Animation) object;
        return (Arrays.equals(this.frames, other.frames));
    }
}
