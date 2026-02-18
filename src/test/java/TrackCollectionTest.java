import model.TrackCollection;
import model.WavFile;
import util.TestResourceLoader;

public class TrackCollectionTest {

    public static void main(String[] args) {

        System.out.println("TrackCollection Test");

        // test constructor

        TrackCollection tc = new TrackCollection();

        if (tc.size() == 0)
            System.out.println("PASS: empty constructor");
        else
            System.out.println("FAIL: empty constructor");

        WavFile a = new WavFile(TestResourceLoader.loadResourceFile("test_audio1.wav"));
        WavFile b = new WavFile(TestResourceLoader.loadResourceFile("testfolder/innertestfolder/test_audio3.wav"));

        tc.addTrack(a);
        tc.addTrack(b);

        // addTrack should update the size of the collection and prevent duplicates

        if (tc.size() == 2) {
            System.out.println("PASS: addTrack correctly adds tracks");
        } else {
            System.out.println("FAIL: addTrack fails to add tracks");
        }

        if (!tc.addTrack(a)) {
            System.out.println("PASS: addTrack prevents duplicates");
        } else {
            System.out.println("FAIL: addTrack allows duplicates");
        }

        // set the correct new index and return the WavFile there
        WavFile newIndex = tc.setCurrentIndex(1);

        if (tc.getCurrentIndex() == 1 && newIndex.equals(b)) {
            System.out.println("PASS: setCurrentIndex correctly sets index");
        } else {
            System.out.println("FAIL: setCurrentIndex incorrectly sets index");
        }

        // remove the correct track correctly adjust index
        tc.removeTrack(1);

        if (tc.size() == 1 && tc.getCurrentIndex() == 0) {
            System.out.println("PASS: removeTrack correctly removes track and sets new index");
        } else {
            System.out.println("FAIL: removeTrack fails to remove track and set new index");
        }

        try {
            tc.setCurrentIndex(99);
            System.out.println("FAIL: invalid index not caught");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: invalid index caught");
        }
    }
}
