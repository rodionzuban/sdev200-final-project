package ui.screens;

import controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Playlist;
import model.WavFile;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import ui.elements.TrackRow;
import util.ResourceLoader;

// PlaylistScreen - shows a specific playlist's info and tracks
// Reached via PlaylistsScreen 

public class PlaylistScreen extends BorderPane {
    private ListView<WavFile> trackListView;
    private ImageView mainPlayButtonView = new ImageView(ResourceLoader.loadResourceFile("main-play-icon.png"));
    private long playlistLengthMilliseconds = 0;
    private AppLabel editLabel = new AppLabel("Edit");
    private AppLabel shuffleLabel = new AppLabel("Shuffle: On");

    public PlaylistScreen(Playlist playlist, AppController controller) {
        trackListView = new ListView<>();
        trackListView.setItems(playlist.getTracks());

        mainPlayButtonView.setFitHeight(64);
        mainPlayButtonView.setFitWidth(64);

        AppLabel trackName = new AppLabel(AppLabelType.SUBTITLE, playlist.getPlaylistName());
        AppLabel trackDescription = new AppLabel(playlist.getPlaylistDescription());

        for (WavFile track : playlist.getTracks()) {
            playlistLengthMilliseconds += track.getDuration();
        }

        AppLabel trackInfoLabel = new AppLabel(TrackRow.formatMilliSeconds(playlistLengthMilliseconds) + " | "
                + playlist.size() + (playlist.size() != 1 ? " tracks" : " track"));

        Region spacer = new Region();

        // Top most row in screen, shows listed information
        HBox topInfoRow = new HBox(trackName, trackInfoLabel, spacer, editLabel, shuffleLabel);

        HBox.setHgrow(spacer, Priority.ALWAYS);
        topInfoRow.setSpacing(15);
        topInfoRow.setMaxWidth(Double.MAX_VALUE);

        // infoContainer - all track info (description, tracks #, length, etc.) but not
        // play button
        VBox infoContainer = new VBox(topInfoRow, trackDescription);

        // topInfoBox - all info rendered in BorderPane.top
        HBox topInfoBox = new HBox(mainPlayButtonView, infoContainer);
        HBox.setHgrow(infoContainer, Priority.ALWAYS);
        infoContainer.setMaxWidth(Double.MAX_VALUE);

        BorderPane.setMargin(topInfoBox, new Insets(0, 0, 10, 0));

        topInfoBox.setSpacing(15);

        // load tracks
        trackListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(WavFile track, boolean empty) {
                super.updateItem(track, empty);

                if (empty || track == null) {
                    setGraphic(null);
                } else {
                    setGraphic(new TrackRow(track, playlist, null));
                }
            }
        });

        setCenter(trackListView);
        setTop(topInfoBox);
        setPadding(new Insets(10));
    }
}
