package com.uet.oop.rendering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class TextureFileReader {
    public static List<String> readFromFile(String filePath) {
        List<String> paths = new ArrayList<>();

        File file = new File(filePath);
        String fileName = file.getName();
        System.out.printf("Reading from file: %s. Dir: %s%n", fileName, file.getAbsolutePath());
        System.out.println();

        try (FileInputStream fileInput = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                paths.add(line);
            }
        } catch (IOException e) {
            e.getCause();
        }
        return paths;
    }
}
