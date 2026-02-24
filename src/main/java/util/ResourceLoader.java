package util;

// loads test resources (similar to TestResourceLoader in test)
public class ResourceLoader {
    public static String loadResourceFile(String path) {
        try {
            return ResourceLoader.class.getClassLoader().getResource(path).toString();
        } catch (Exception e) {
            System.out.println("Error loading resource file: " + e.getMessage());
            return null;
        }
    }
}
