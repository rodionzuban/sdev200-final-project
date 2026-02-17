package util;

import java.io.File;

public class TestResourceLoader {
    public static File loadResourceFile(String path) {
        try {
            return new File(TestResourceLoader.class.getClassLoader().getResource(path).toURI());
        } catch (Exception e) {
            System.out.println("Error loading resource file: " + e.getMessage());
            return null;
        }
    }
}
