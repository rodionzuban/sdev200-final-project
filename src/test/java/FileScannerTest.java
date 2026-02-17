import java.io.File;
import java.util.List;

import util.FileScanner;
import model.WavFile;

public class FileScannerTest {
    public static void main(String[] args) {
        System.out.println("FileScanner Test");
        File folder;

        try {

            // manually load file using sample path string to test loadFolder
            folder = FileScanner
                    .loadFolder(FileScannerTest.class.getClassLoader().getResource("testfolder").toString());
            System.out.println("PASS: loadFolder loads the folder");

            // test loading without subdirectories
            List<WavFile> noSubDirectories = FileScanner.scanDirectory(folder, false);

            if (noSubDirectories.size() == 1) {
                System.out.println("PASS: scanDirectory without subdirectories returns correct number of WavFiles");
            } else {
                System.out.println(
                        "FAIL: scanDirectory without subdirectories returns incorrect number of WavFiles\nExpected: 1\nGot: "
                                + noSubDirectories.size());
            }

            // test loading with subdirectories
            List<WavFile> subDirectories = FileScanner.scanDirectory(folder, true);

            if (subDirectories.size() == 3) {
                System.out.println("PASS: scanDirectory with subdirectories returns correct number of WavFiles");
            } else {
                System.out.println(
                        "FAIL: scanDirectory with subdirectories returns incorrect number of WavFiles\nExpected: 3\nGot: "
                                + subDirectories.size());
            }
        } catch (Exception e) {
            System.out.println("FAIL: loadFolder doesn't load folder.\nException: " + e.getMessage());
        }
    }
}
