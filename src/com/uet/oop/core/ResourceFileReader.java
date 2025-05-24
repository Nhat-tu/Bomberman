package com.uet.oop.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ResourceFileReader {
    public static List<String> readFromFile(String resourceFileName) {
        List<String> paths = new ArrayList<>();
        try (InputStream is = ResourceFileReader.class.getClassLoader().getResourceAsStream(resourceFileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.err.println("CRITICAL ERROR: Resource file not found in JAR: " + resourceFileName + ". Please ensure it's in src/main/resources and packaged correctly.");
                return paths;
            }

            System.out.printf("Successfully opened resource stream for: %s%n", resourceFileName);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    paths.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading resource file: " + resourceFileName + " - " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException: Resource stream for " + resourceFileName + " was null. File likely not found in JAR.");
            e.printStackTrace();
        }
        return paths;
    }
}