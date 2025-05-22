package com.uet.oop.rendering;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// no more <object>.loadTexture.
public class TextureManager {
    private Map<String, BufferedImage> textureMap;

    public TextureManager() {
        this.textureMap = new HashMap<>();
    }

    // load texture from files, save & associate it with a name.
    public BufferedImage loadTexture(String name, String texturePath) {
        if (textureMap.containsKey(name)) {
            return textureMap.get(name);
        }
        try {
            BufferedImage texture = ImageIO.read(getClass().getResourceAsStream(texturePath));
            if (texture != null) {
                textureMap.put(name, texture);
                System.out.printf("Texture: %s loaded %s from %s%n","\u001B[40m" + name + "\u001B[0m", "\u001B[32m" + "successfully" + "\u001B[0m" , texturePath);
                return texture;
            } else {
                System.out.println("Could not read texture: " + name);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading texture: " + name);
            return null;
        }
    }

    public void bulkLoadTexture() {
        List<String> filePaths;
        filePaths = TextureFileReader.readFromFile("res/texturePaths.txt"); // this already ignores blank lines.

        List<String> fileNames = new ArrayList<>();
        for (String pathString : filePaths) {
            if (pathString != null) {
                try {
                    Path path = Paths.get(pathString);
                    Path fileNamePath = path.getFileName();
                    if (fileNamePath != null) {
                        fileNames.add(fileNamePath.toString());
                    } else {
                        // Handles root directories or paths ending with separator
                        // getFileName() returns null for root (e.g., "/")
                        // For "/path/to/directory/", it returns "directory"
                        fileNames.add("it's root"); // handle
                    }
                } catch (Exception e) {
                    // Handle potential invalid path strings if necessary
                    System.err.println("Error processing path: " + pathString + " - " + e.getMessage());
                    fileNames.add("failed"); // error marker
                }
            } else {
                fileNames.add("null!"); // Handle null paths
            }
        }

        for (int i = 0; i < fileNames.size(); i++) {
            loadTexture(fileNames.get(i), filePaths.get(i));
        }
    }

    public BufferedImage getTexture(String name) {
        BufferedImage texture = textureMap.get(name);
        if (texture == null) {
            System.err.println("Texture requested, but not found: " + name);
        }
        return texture;
    }

    /**
     *
     * @param sheetName
     * @param x is start_X of sub-image
     * @param y is start_Y of sub-image
     * @param width
     * @param height
     *
     * @return a sub-image from the given sheet
     */
    public BufferedImage getSubImage(String sheetName, int x, int y, int width, int height) {
        BufferedImage sheet = getTexture(sheetName);
        if (sheet == null) {
            System.err.println("Sheet: " + sheetName + " not present");
            return null;
        }

        try {
            if (x < 0 || y < 0 || width < 0 || height < 0
                || x + width > sheet.getWidth() || y + height > sheet.getHeight()
            ) {
                System.err.println("Invalid coordinates or dimension for sheet: " + sheetName);
                return null;
            }
            return sheet.getSubimage(x, y, width, height);
        } catch (Exception e) {
            System.out.println("Error getting sub-image from sheet: " + sheetName + ".Cause: " + e.getMessage());
            return null;
        }
    }

    public void unloadTexture(String name) {
        if (textureMap.containsKey(name)) {
            textureMap.remove(name);
            System.out.println("Texture '" + name + "' unloaded.");
        }
    }

    public void unloadAllTextures() {
        textureMap.clear();
        System.out.println("All textures unloaded.");
    }
}
