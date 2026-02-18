import java.util.ArrayList;
import java.util.List;

import model.Playlist;
import model.WavFile;
import util.TestResourceLoader;

public class PlaylistTest {

    public static void main(String[] args) {

        System.out.println("Playlist Test");

        List<WavFile> tracks = new ArrayList<>();

        WavFile a = new WavFile(TestResourceLoader.loadResourceFile("test_audio1.wav"));
        WavFile b = new WavFile(TestResourceLoader.loadResourceFile("testfolder/innertestfolder/test_audio3.wav"));
        WavFile c = new WavFile(TestResourceLoader.loadResourceFile("testfolder/innertestfolder/test_audio1.wav"));

        tracks.add(a);
        tracks.add(b);
        tracks.add(c);

        // test constructors
        Playlist p = new Playlist(tracks, "Test", "Demo");

        if (p.getPlaylistName().equals("Test") && p.size() == 3) {
            System.out.println("PASS: constructor correctly initializes object");
        } else {
            System.out.println("FAIL: constructor fails to correctly initialize object");
        }

        // sequential play
        p.setCurrentIndex(0);
        WavFile testTrack = p.nextTrack();

        if (p.getCurrentIndex() == 1 && testTrack.equals(b)) {
            System.out.println("PASS: sequential nextTrack returns correct track and updates index");
        } else {
            System.out.println("FAIL: sequential nextTrack doesn't return correct track and update index");
        }

        // wrap around
        p.setCurrentIndex(2);
        p.nextTrack();

        if (p.getCurrentIndex() == 0) {
            System.out.println("PASS: nextTrack wraps around after last index");
        } else {
            System.out.println("FAIL: wrap around");
        }

        // shuffle toggle
        p.toggleShuffle();

        if (p.getOnShuffle()) {
            System.out.println("PASS: shuffle enabled correctly");
        } else {
            System.out.println("FAIL: shuffle enabled incorrectly");
        }

        WavFile first = p.nextTrack();
        WavFile second = p.nextTrack();

        if (!first.equals(second)) {
            System.out.println("PASS: shuffle different tracks");
        } else {
            System.out.println("FAIL: shuffling repeats tracs");
        }

        p.toggleShuffle();

        if (!p.getOnShuffle()) {
            System.out.println("PASS: shuffle disabled correctly");
        } else {
            System.out.println("FAIL: shuffle disabled incorrectly");
        }
    }
}