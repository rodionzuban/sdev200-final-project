import audio.AudioPlayer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import model.WavFile;
import util.TestResourceLoader;

public class AudioPlayerTest {
    public static void main(String[] args) {
        System.out.println("AudioPlayer test");
        JFXPanel fxPanel = new JFXPanel();

        WavFile testFile = new WavFile(TestResourceLoader.loadResourceFile("test_audio1.wav"));
        AudioPlayer player = new AudioPlayer(testFile);
        // Test if .play() plays the track and time increases

        player.play();
        sleep(2000);
        if (player.isPlaying()) {
            System.out.println("PASS: play starts playback");
        } else {
            System.out.println("FAIL: play doesn't start playback");
        }

        double timeAfterPlay = player.getCurrentTime();

        if (timeAfterPlay > 0) {
            System.out.println("PASS: play increases playback time");
        } else {
            System.out.println("FAIL: play does not increase playback time\nExpected: > 0\nGot: " + timeAfterPlay);
        }

        // Test if pausing freezes time and stops playing state

        player.pause();
        double pausedTime = player.getCurrentTime();

        sleep(2000);

        if (Math.abs(player.getCurrentTime() - pausedTime) < 0.05) {
            System.out.println("PASS: pause freezes time");
        } else {
            System.out.println("FAIL: pause did not freeze time");
        }

        if (!player.isPlaying()) {
            System.out.println("PASS: pause stops playing state");
        } else {
            System.out.println("FAIL: pause still playing");
        }

        // Test if playback time restarts after resuming

        player.play();
        sleep(2000);

        if (player.getCurrentTime() > pausedTime) {
            System.out.println("PASS: play() resumes after pause");
        } else {
            System.out.println("FAIL: play() restarted instead of resuming");
        }

        // Test if stopping resets playback time

        player.stop();

        if (player.getCurrentTime() < 0.05) {
            System.out.println("PASS: stop() resets time");
        } else {
            System.out.println("FAIL: stop() did not reset time");
        }

        if (!player.isPlaying()) {
            System.out.println("PASS: stop() stops playback");
        } else {
            System.out.println("FAIL: stop() still playing");
        }

        Platform.exit();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
