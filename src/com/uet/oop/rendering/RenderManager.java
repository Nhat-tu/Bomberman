package com.uet.oop.rendering;

import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

public class RenderManager {
    private Graphics2D graphicContext2d;
    private List<Renderable> renderables;

    public RenderManager() {
        this.renderables = new ArrayList<>();
        this.graphicContext2d = null;
    }

    public void setGraphicContext2d(Graphics2D graphicContext2d) {
        this.graphicContext2d = graphicContext2d;
    }

    public void addRenderable(Renderable renderable) {
        if (renderable != null) {
            renderables.add(renderable);
        }
    }

    public void removeRenderable(Renderable renderable) {
        if (renderable != null) {
            renderables.remove(renderable);
        }
    }

    public void clearRenderables() {
        renderables.clear();
    }

    public void render() {
        if (graphicContext2d == null) {
            System.out.println(" | No graphicContext set | ");
            return;
        }

        for (Renderable renderable : renderables) {
            renderable.draw(graphicContext2d);
        }
    }
}
