package ui.layout;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.elements.AppLabel;
import util.ResourceLoader;

public class NowPlaying extends VBox {
    private Label trackNameLabel = new AppLabel("Song title");
    private ImageView playButtonView;
    private ImageView nextButtonView;
    private ImageView previousButtonView;
    private Runnable togglePlay;
    private Runnable handleNext;
    private Runnable handlePrevious;
    private Image playButton = new Image(ResourceLoader.loadResourceFile("main-play-icon.png"));

    // NowPlaying - UI playback state manager that runs current track and connects
    // --> AppLayout --> AppController to manage next/previous track
    public NowPlaying(Runnable togglePlay, Runnable handleNext, Runnable handlePrevious) {
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
        nextButtonView.setOnMousePressed(e -> this.handleNext.run());
        previousButtonView.setOnMousePressed(e -> this.handlePrevious.run());

        HBox playbackHBox = new HBox(previousButtonView, playButtonView, nextButtonView);
        playbackHBox.setAlignment(Pos.CENTER);
        playbackHBox.setSpacing(20);
        setAlignment(Pos.CENTER);
        setSpacing(15);
        setPadding(new Insets(10, 0, 10, 0));
        setStyle("-fx-border-color: black; -fx-border-width: 1px 0 0 0;");
        getChildren().addAll(trackNameLabel, playbackHBox);
    }
}
