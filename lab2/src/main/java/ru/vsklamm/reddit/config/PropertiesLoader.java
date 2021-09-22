package ru.vsklamm.reddit.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadProperties(final String filename) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            System.err.printf("File '%s' not found%n", filename);
        } catch (IOException e) {
            System.err.printf("Failed to read file '%s'%n", filename);
        }
        return properties;
    }
}

