package ui.elements;

import java.util.List;
import java.util.function.Consumer;

import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Playlist;
import model.TrackCollection;
import model.WavFile;
import ui.dialogs.AddTrackDialog;
import util.ImageResources;
import util.ResourceLoader;

// TrackRow - UI element for displaying a track in playlists, home screen
public class TrackRow extends HBox {
    private WavFile track;
    private TrackCollection parentCollection;
    private Image playButton = new Image(ResourceLoader.loadResourceFile("play-icon.png"));
    private Image pauseButton = new Image(ResourceLoader.loadResourceFile("pause-icon.png"));
    private ImageView playButtonView;
    private ImageView removeButtonView;
    private ImageView addButtonView;

    public TrackRow(WavFile track, TrackCollection parentCollection, Consumer<Integer> onPlay,
            Consumer<Integer> onRemove,
            AppController controller) {
        this.track = track;
        this.parentCollection = parentCollection;

        playButtonView = new ImageView(playButton);
        playButtonView.setFitWidth(32);
        playButtonView.setFitHeight(32);
        playButtonView.setOnMousePressed(e -> {
            onPlay.accept(this.parentCollection.indexOf(this.track));
            controller.clearForwardStack();
        });

        removeButtonView = new ImageView(ImageResources.REMOVE_ICON);
        removeButtonView.setFitHeight(32);
        removeButtonView.setFitWidth(32);
        removeButtonView.setOnMousePressed(e -> onRemove.accept(this.parentCollection.indexOf(this.track)));

        addButtonView = new ImageView(ImageResources.ADD_ICON);
        addButtonView.setFitHeight(32);
        addButtonView.setFitWidth(32);
        addButtonView.setOnMousePressed(e -> handleTrackAdd(controller));

        playButtonView.imageProperty().bind(Bindings
                .when(controller.currentTrackProperty().isNotNull().and(
                        controller.currentTrackProperty().isEqualTo(this.track).and(controller.pausedProperty().not())))
                .then(pauseButton).otherwise(playButton));

        VBox trackInfo = new VBox(new AppLabel(track.getTitle()),
                new AppLabel(formatMilliSeconds(track.getDuration())));

        this.track = track;
        this.parentCollection = parentCollection;

        Region spacer = new Region();

        getChildren().addAll(playButtonView, trackInfo, spacer, addButtonView, removeButtonView);
        setSpacing(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        setAlignment(Pos.CENTER_LEFT);
    }

    private void handleTrackAdd(AppController controller) {
        AddTrackDialog dialog = new AddTrackDialog(track, controller);
        List<Playlist> result = dialog.run();

        for (Playlist playlist : controller.getPlaylists()) {
            if (result.contains(playlist)) {
                playlist.addTrack(track);
            } else {
                playlist.removeTrack(playlist.indexOf(track));
            }
        }
    }

    // turns millisecond amount into a format like mm:ss
    public static String formatMilliSeconds(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds -= minutes * 60;

        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
}
