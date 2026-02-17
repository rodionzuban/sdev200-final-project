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

    public AudioPlayer() {
        mediaPlayer = null;
        track = null;
        paused = true;
        addedListen = false;
    }

    public AudioPlayer(WavFile track) {
        load(track);
    }

    public void load(WavFile track) {
        stop();

        try {
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

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            paused = true;
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

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
}
