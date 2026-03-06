package audio;

import java.io.File;

import controller.AppController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.WavFile;

public class AudioPlayer {
    private ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();
    private WavFile track;
    private BooleanProperty paused = new SimpleBooleanProperty(true);
    private boolean addedListen;
    private WavFile lastPlayedTrack;
    private Runnable onTrackFinished;
    private AppController controller;

    // initialize AudioPlayer
    public AudioPlayer(AppController controller) {
        mediaPlayer.set(null);
        track = null;
        paused.set(true);
        addedListen = false;
        lastPlayedTrack = null;
        onTrackFinished = null;
        this.controller = controller;
    }

    // CONSTRUCTOR USED FOR TESTING ONLY
    public AudioPlayer(WavFile track) {
        load(track);
    }

    public void setOnTrackFinished(Runnable onTrackFinished) {
        this.onTrackFinished = onTrackFinished;
    }

    // load WavFile as a MediaPlayer Media in JavaFX. Return value indicates if a
    // new track was loaded
    public boolean load(WavFile track) {
        // if the user tries to load a track that's already loaded
        if (this.track != null && this.track.equals(track) && controller.getCurrentCollection().size() != 1) {
            if (paused.get())
                play();
            else
                pause();
            return false;
        }

        stop();

        try {
            if (track != null) {
                lastPlayedTrack = this.track;
            }

            File file = new File(track.getFilePath());
            Media media = new Media(file.toURI().toString());

            this.mediaPlayer.set(new MediaPlayer(media));
            this.track = track;
            this.paused.set(false);
            this.addedListen = false;

            this.mediaPlayer.get().setOnEndOfMedia(onTrackFinished);

            return true;
        }

        catch (Exception e) {
            System.out.println("Error loading track: " + e.getMessage());
            return false;
        }
    }

    // play track, and add a listen to it if not done yet
    public void play() {
        if (mediaPlayer.get() != null) {
            mediaPlayer.get().play();
            paused.set(false);
            if (!addedListen) {
                track.addListen();
                addedListen = true;
            }
        }
    }

    // pause track
    public void pause() {
        if (mediaPlayer.get() != null) {
            mediaPlayer.get().pause();
            paused.set(true);
        }
    }

    // stop track, resetting to the start of the track
    public void stop() {
        if (mediaPlayer.get() != null) {
            mediaPlayer.get().stop();
            paused.set(true);
        }
    }

    // getter methods
    public boolean getPaused() {
        return paused.get();
    }

    public BooleanProperty getPausedProperty() {
        return paused;
    }

    public WavFile getTrack() {
        return track;
    }

    public WavFile getLastTrack() {
        return lastPlayedTrack;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer.get();
    }

    public ObjectProperty<MediaPlayer> mediaPlayerProperty() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer.get() != null && mediaPlayer.get().getStatus() == MediaPlayer.Status.PLAYING;
    }

    public double getCurrentTime() {
        if (mediaPlayer.get() != null) {
            return mediaPlayer.get().getCurrentTime().toSeconds();
        }
        return -1;
    }

    // loads the last track played for previous button on playback
    public WavFile loadLastTrack() {
        if (lastPlayedTrack != null) {
            WavFile toReturn = lastPlayedTrack;
            load(lastPlayedTrack);
            return toReturn;
        }

        return null;
    }
}
