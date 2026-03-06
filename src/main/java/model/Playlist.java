package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import controller.AppController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Playlist extends TrackCollection {
    private StringProperty playlistName = new SimpleStringProperty();
    private StringProperty playlistDescription = new SimpleStringProperty();
    private List<Integer> notPlayedIndices;
    private BooleanProperty onShuffle = new SimpleBooleanProperty(false);
    private BooleanProperty isCurrentPlaylist = new SimpleBooleanProperty(false);

    // private initializer for constructors
    private void init(AppController controller) {
        notPlayedIndices = new ArrayList<>();
        playlistName.set("");
        playlistDescription.set("");

        if (controller != null) {
            isCurrentPlaylist.bind(controller.currentCollectionProperty().isEqualTo(this));
        }
    }

    public Playlist(AppController controller) {
        super();
        init(controller);
    }

    public Playlist(AppController controller, List<WavFile> tracks) {
        super(tracks);
        init(controller);
        notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
    }

    public Playlist(AppController controller, String playlistName, String playlistDescription) {
        this(controller);
        this.playlistName.set(playlistName);
        this.playlistDescription.set(playlistDescription);
    }

    public Playlist(AppController controller, List<WavFile> tracks, String playlistName, String playlistDescription) {
        this(controller, tracks);
        this.playlistName.set(playlistName);
        this.playlistDescription.set(playlistDescription);
    }

    // CONSTRUCTOR TO BE USED FOR TESTING ONLY
    public Playlist(List<WavFile> tracks, String playlistName, String playlistDescription) {
        super(tracks);
        init(null);
        notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
        this.playlistName.set(playlistName);
        this.playlistDescription.set(playlistDescription);
    }

    // getter methods
    public String getPlaylistName() {
        return playlistName.get();
    }

    public StringProperty playlistNameProperty() {
        return playlistName;
    }

    public String getPlaylistDescription() {
        return playlistDescription.get();
    }

    public StringProperty playlistDescriptionProperty() {
        return playlistDescription;
    }

    public boolean getOnShuffle() {
        return onShuffle.get();
    }

    public BooleanProperty onShuffleProperty() {
        return onShuffle;
    }

    public void setPlaylistName(String name) {
        playlistName.set(name);
    }

    public void setPlaylistDescription(String description) {
        playlistDescription.set(description);
    }

    // get the next track, either sequentially or by randomly selecting from
    // notPlayedIndices in this shuffle iteration
    @Override
    public WavFile nextTrack() {
        if (size() <= 0) {
            return null;
        }

        if (onShuffle.get()) {
            if (notPlayedIndices.size() == 0) {
                shuffle();
            }
            if (size() == 1) {
                return setCurrentIndex(0);
            }
            int index = (int) (Math.random() * (notPlayedIndices.size()));

            int trackIndex = notPlayedIndices.remove(index);
            return setCurrentIndex(trackIndex);
        } else {
            return super.nextTrack();
        }
    }

    // toggle shuffled mode, resetting notPlayedIndices
    public void toggleShuffle() {
        if (!onShuffle.get()) {
            shuffle();
        }

        onShuffle.set(!onShuffle.get());
    }

    // update notPlayedIndices when a track is added for shuffling
    @Override
    public boolean addTrack(WavFile track) {
        boolean added = super.addTrack(track);

        if (added) {
            notPlayedIndices.add(size() - 1);
        }

        return added;
    }

    // update notPlayedIndices by removing deleted index and subtracting 1 from all
    // indices higher than the one that was removed
    @Override
    public boolean removeTrack(int index) {
        boolean removed = super.removeTrack(index);

        if (removed) {
            notPlayedIndices.remove((Integer) index);

            for (int i = 0; i < notPlayedIndices.size(); i++) {
                if (notPlayedIndices.get(i) > index) {
                    notPlayedIndices.set(i, notPlayedIndices.get(i) - 1);
                }
            }
        }

        return removed;
    }

    private void shuffle() {
        notPlayedIndices = IntStream.range(0, tracks.size()).boxed().collect(Collectors.toList());
        notPlayedIndices.remove((Integer) currentIndex);
    }

    @Override
    public WavFile setCurrentIndex(int index) throws IllegalArgumentException {
        WavFile track = super.setCurrentIndex(index);
        if (onShuffle.get()) {
            shuffle();
        }

        return track;
    }

    public BooleanProperty isCurrentPlaylistProperty() {
        return isCurrentPlaylist;
    }

    public String toString() {
        return playlistName.get();
    }
}