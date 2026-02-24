package controller;

import audio.AudioPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.TrackCollection;
import model.Playlist;
import util.FileScanner;
import util.TestResourceLoader;
import java.util.ArrayList;

// AppController - manages state, screen interactions, etc.
public class AppController {

    private AudioPlayer player = new AudioPlayer();

    // globalCollection - collection of all tracks loaded into the app
    private TrackCollection globalCollection;
    private ObservableList<Playlist> playlists;

    public AppController() {
        // load sample tracks and playlists (temporary)
        globalCollection = new TrackCollection(
                FileScanner.scanDirectory(TestResourceLoader.loadResourceFile("testfolder"), true));
        ArrayList<Playlist> testPlaylists = new ArrayList<Playlist>();
        testPlaylists.add(new Playlist(globalCollection.getTracks(), "My Playlist 1", "Best Tunes"));
        testPlaylists.add(new Playlist(globalCollection.getTracks(), "My Playlist 2", "Best Tunes (again)"));
        testPlaylists.add(new Playlist(globalCollection.getTracks(), "My Playlist 3", "No Description"));
        playlists = FXCollections.observableArrayList(testPlaylists);
    }

    // handle playback pause/play requests
    public void togglePlay() {
        if (player.isPlaying())
            player.pause();
        else
            player.play();
    }

    // load previous and next track in playback
    public void nextTrack() {
        if (globalCollection != null) {
            player.load(globalCollection.nextTrack());
        }
    }

    public void previousTrack() {
        if (player.getCurrentTime() > 3) {
            player.stop();
            player.play();
        } else {
            player.loadLastTrack();
        }
    }

    // getter methods
    public TrackCollection getGlobalCollection() {
        return globalCollection;
    }

    public ObservableList<Playlist> getPlaylists() {
        return playlists;
    }
}