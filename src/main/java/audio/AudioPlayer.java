package audio;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.WavFile;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private WavFile track;
    private boolean paused;
    private boolean addedListen;
    private WavFile lastPlayedTrack;

    // initialize AudioPlayr
    public AudioPlayer() {
        mediaPlayer = null;
        track = null;
        paused = true;
        addedListen = false;
        lastPlayedTrack = null;
    }

    public AudioPlayer(WavFile track) {
        load(track);
    }

    // load WavFile as a MediaPlayer Media in JavaFX
    public void load(WavFile track) {
        stop();

        try {
            if (lastPlayedTrack != null) {
                lastPlayedTrack = this.track;
            }
            File file = new File(track.getFilePath());
            Media media = new Media(file.toURI().toString());

            this.mediaPlayer = new MediaPlayer(media);
            this.track = track;
            this.paused = true;
            this.addedListen = false;
        }

        catch (Exception e) {
            System.out.println("Error loading track: " + e.getMessage());
        }
    }

    // play track, and add a listen to it if not done yet
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            paused = false;
            if (!addedListen) {
                track.addListen();
                addedListen = true;
            }
        }
    }

    // pause track
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            paused = true;
        }
    }

    // stop track, resetting to the start of the track
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // getter methods
    public boolean getPaused() {
        return paused;
    }

    public WavFile getTrack() {
        return track;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public double getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return -1;
    }

    // loads the last track played for previous button on playback
    public void loadLastTrack() {
        if (lastPlayedTrack != null) {
            load(lastPlayedTrack);
        }
    }
}
