package controller;

import audio.AudioPlayer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.TrackCollection;
import model.WavFile;
import ui.layout.AppLayout;
import model.Playlist;
import java.util.Stack;

// AppController - manages state, screen interactions, etc.
public class AppController {

    private AudioPlayer player = new AudioPlayer(this);

    // globalCollection - collection of all tracks loaded into the app
    private TrackCollection globalCollection;
    private ObjectProperty<TrackCollection> currentCollection = new SimpleObjectProperty<TrackCollection>(null);
    private ObservableList<Playlist> playlists;
    private ObjectProperty<WavFile> currentTrack = new SimpleObjectProperty<WavFile>(null);
    private AppLayout layout;

    private Stack<WavFile> backStack = new Stack<>();
    private Stack<WavFile> forwardStack = new Stack<>();

    public AppController() {
        // load sample tracks and playlists (temporary)
        globalCollection = new TrackCollection();
        playlists = FXCollections.observableArrayList();
        player.setOnTrackFinished(this::handleTrackFinished);
        this.layout = null;
    }

    public void setAppLayout(AppLayout appLayout) {
        this.layout = appLayout;
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
        if (currentCollection.get() != null) {
            WavFile nextTrack = currentCollection.get().nextTrack();

            backStack.push(currentTrack.get());
            handlePlayRequest(currentCollection.get(), currentCollection.get().indexOf(nextTrack));
        }
    }

    public void previousTrack() {
        if (player.getCurrentTime() > 3 || backStack.isEmpty()) {
            player.stop();
            player.play();
        } else {
            forwardStack.push(currentTrack.get());
            currentTrack.set(backStack.pop());
            handlePlayRequest(currentCollection.get(), currentCollection.get().indexOf(currentTrack.get()));
        }
    }

    public void handlePlayRequest(TrackCollection collection, int collectionIndex) {
        if (collection != null && collection.size() > 0) {
            setCurrentCollection(collection);

            collection.setCurrentIndex(collectionIndex);

            if (player.load(collection.getTrack())) {
                currentTrack.set(collection.getTrack());
                layout.getNowPlaying().setTrackInfo(collection.getTrack());
                player.play();
            }
        }
    }

    private void handleTrackFinished() {
        nextTrack();
    }

    // getter methods
    public TrackCollection getGlobalCollection() {
        return globalCollection;
    }

    public TrackCollection getCurrentCollection() {
        return currentCollection.get();
    }

    public ObjectProperty<TrackCollection> currentCollectionProperty() {
        return currentCollection;
    }

    public void setCurrentCollection(TrackCollection collection) {
        currentCollection.set(collection);
    }

    public ObservableList<Playlist> getPlaylists() {
        return playlists;
    }

    public BooleanProperty pausedProperty() {
        return player.getPausedProperty();
    }

    public ObjectProperty<WavFile> currentTrackProperty() {
        return currentTrack;
    }

    public void clearForwardStack() {
        forwardStack.clear();
    }

    public AudioPlayer getAudioPlayer() {
        return player;
    }

    public void removeTrackGlobally(int index) {
        WavFile toRemove = globalCollection.getAtIndex(index);
        globalCollection.removeTrack(index);

        for (Playlist playlist : playlists) {
            if (playlist.contains(toRemove)) {
                playlist.removeTrack(playlist.indexOf(toRemove));
            }
        }

    }

    public void removePlaylist(Playlist playlist) {
        if (playlists.contains(playlist)) {
            if (playlist.isCurrentPlaylistProperty().get()) {
                currentCollection.set(new TrackCollection(playlist.getTracks()));
            }
            playlists.remove(playlist);
        }
    }
}