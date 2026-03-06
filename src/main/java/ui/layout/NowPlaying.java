package ui.layout;

import javafx.geometry.Pos;
import controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.WavFile;
import ui.elements.AppLabel;
import util.ResourceLoader;

public class NowPlaying extends VBox {
    private Label trackNameLabel = new AppLabel("");
    private ImageView playButtonView;
    private ImageView nextButtonView;
    private ImageView previousButtonView;
    private Runnable togglePlay;
    private Runnable handleNext;
    private Runnable handlePrevious;
    private Image playButton = new Image(ResourceLoader.loadResourceFile("main-play-icon.png"));
    private Image pauseButton = new Image(ResourceLoader.loadResourceFile("main-pause-icon.png"));
    private Slider durationSlider = new Slider();
    private AppLabel elapsedLabel = new AppLabel("");
    private AppLabel totalLabel = new AppLabel("");

    // NowPlaying - UI playback state manager that runs current track and connects
    // --> AppLayout --> AppController to manage next/previous track
    public NowPlaying(Runnable togglePlay, Runnable handleNext, Runnable handlePrevious, AppController controller) {
        playButtonView = new ImageView(playButton);
        nextButtonView = new ImageView(ResourceLoader.loadResourceFile("next-icon.png"));
        previousButtonView = new ImageView(ResourceLoader.loadResourceFile("previous-icon.png"));

        playButtonView.setFitWidth(64);
        playButtonView.setFitHeight(64);
        nextButtonView.setFitWidth(48);
        nextButtonView.setFitHeight(48);
        previousButtonView.setFitWidth(48);
        previousButtonView.setFitHeight(48);

        this.togglePlay = togglePlay;
        this.handleNext = handleNext;
        this.handlePrevious = handlePrevious;

        playButtonView.setOnMousePressed(e -> this.togglePlay.run());
        nextButtonView.setOnMousePressed(e -> {
            this.handleNext.run();
        });
        previousButtonView.setOnMousePressed(e -> this.handlePrevious.run());

        controller.pausedProperty().addListener((obs, oldVal, isPaused) -> {
            if (isPaused) {
                playButtonView.setImage(playButton);
            } else {
                playButtonView.setImage(pauseButton);
            }
        });

        durationSlider.setMin(0);
        HBox.setHgrow(durationSlider, Priority.ALWAYS);
        durationSlider.setMaxWidth(Double.MAX_VALUE);

        controller.getAudioPlayer().mediaPlayerProperty().addListener((o, oldPlayer, mediaPlayer) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setOnReady(() -> {
                    Duration total = mediaPlayer.getMedia().getDuration();
                    durationSlider.setMax(total.toSeconds());
                    totalLabel.setText(formatTime(total));
                    elapsedLabel.setText("0:00");
                });

                mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                    if (!durationSlider.isValueChanging()) {
                        durationSlider.setValue(newTime.toSeconds());
                        elapsedLabel.setText(formatTime(newTime));
                    }
                });
            }
        });

        HBox sliderHBox = new HBox(elapsedLabel, durationSlider, totalLabel);
        sliderHBox.setPadding(new Insets(0, 10, 0, 10));
        sliderHBox.setSpacing(10);
        HBox playbackHBox = new HBox(previousButtonView, playButtonView, nextButtonView);
        playbackHBox.setAlignment(Pos.CENTER);
        playbackHBox.setSpacing(20);
        setAlignment(Pos.CENTER);
        setSpacing(15);
        setPadding(new Insets(10, 0, 10, 0));
        setStyle("-fx-border-color: black; -fx-border-width: 1px 0 0 0;");
        getChildren().addAll(trackNameLabel, sliderHBox, playbackHBox);
    }

    public void setTrackInfo(WavFile track) {
        trackNameLabel.setText(track.getTitle());
    }

    private String formatTime(Duration duration) {
        int seconds = (int) duration.toSeconds();
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
