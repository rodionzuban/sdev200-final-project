package util;

import model.WavFile;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {
    // scan the given directory and create a list of included WavFiles. scan
    // subdirectories, if needed
    public static List<WavFile> scanDirectory(File folder, boolean scanSubDirectories) throws IllegalArgumentException {
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist");
        }

        List<WavFile> result = new ArrayList<WavFile>();

        for (File file : folder.listFiles()) {
            if (scanSubDirectories && file.isDirectory()) {
                result.addAll(scanDirectory(file, true));
            } else if (file.isFile() && file.getAbsolutePath().toLowerCase().endsWith(".wav")) {
                result.add(new WavFile(file));
            }
        }

        return result;
    }

    // load a folder path as a File object
    public static File loadFolder(String path) throws IllegalArgumentException {
        System.out.println(path);
        File folder = new File(URI.create(path));

        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist");
        }

        return folder;

    }
}
