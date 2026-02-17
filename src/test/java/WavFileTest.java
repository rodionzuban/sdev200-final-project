import java.io.File;
import model.WavFile;
import util.TestResourceLoader;

public class WavFileTest {
    public static void main(String[] args) {
        System.out.println("WavFile Test");
        WavFile song = new WavFile(TestResourceLoader.loadResourceFile("test_audio1.wav"));
        if (song.getTitle().equals("test_audio1")) {
            System.out.println("PASS: extractTitle works");
        } else {
            System.out.println(
                    "FAIL: extractTitle works incorrectly\nExpected: test_audio1\nGot: " + song.getTitle());
        }

        if (song.getDuration() != -1) {
            System.out.println("PASS: extractDuration works");
        } else {
            System.out
                    .println("FAIL: extractDuration works incorrectly\nExpected: test_audio1\nGot: "
                            + song.getTitle());
        }

        song.addListen();
        song.addListen();

        if (song.getNumListens() == 2) {
            System.out.println("PASS: addListen works");
        } else {
            System.out.println("FAIL: addListen works incorrectly\nExpected: 2\nGot: " + song.getNumListens());
        }

        try {
            new WavFile(TestResourceLoader.loadResourceFile("test_audio2.mp3"));
            System.out.println("FAIL: does not catch non-WAV files");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: catches non-WAV files");
        } catch (Exception e) {
            System.out.println("OTHER EXCEPTION: " + e.getMessage());
        }
        try {
            new WavFile(new File("fake_song.wav"));
            System.out.println("FAIL: does not non-files");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: catches non-files");
        }

    }
}
