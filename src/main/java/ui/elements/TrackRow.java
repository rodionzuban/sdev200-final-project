package ui.elements;

import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.TrackCollection;
import model.WavFile;
import util.ResourceLoader;

// TrackRow - UI element for displaying a track in playlists, home screen
public class TrackRow extends HBox {
    private WavFile track;
    private TrackCollection parentCollection;
    private Image playButton = new Image(ResourceLoader.loadResourceFile("play-icon.png"));
    private ImageView playButtonView;
    private ImageView removeButtonView;

    public TrackRow(WavFile track, TrackCollection parentCollection, Consumer<Integer> onPlay) {
        playButtonView = new ImageView(playButton);
        playButtonView.setFitWidth(32);
        playButtonView.setFitHeight(32);

        removeButtonView = new ImageView(new Image(ResourceLoader.loadResourceFile("remove-icon.png")));
        removeButtonView.setFitHeight(32);
        removeButtonView.setFitWidth(32);

        VBox trackInfo = new VBox(new AppLabel(track.getTitle()),
                new AppLabel(formatMilliSeconds(track.getDuration())));

        this.track = track;
        this.parentCollection = parentCollection;

        Region spacer = new Region();

        getChildren().addAll(playButtonView, trackInfo, spacer, removeButtonView);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        setAlignment(Pos.CENTER_LEFT);
    }

    // turns millisecond amount into a format like mm:ss
    public static String formatMilliSeconds(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds -= minutes * 60;

        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
