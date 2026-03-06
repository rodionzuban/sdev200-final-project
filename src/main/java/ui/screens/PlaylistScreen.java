package ui.screens;

import java.util.Optional;

import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.Playlist;
import model.WavFile;
import ui.elements.AppLabel;
import ui.elements.TrackRow;
import util.ImageResources;

// PlaylistScreen - shows a specific playlist's info and tracks
// Reached via PlaylistsScreen 

public class PlaylistScreen extends BorderPane {
    private ListView<WavFile> trackListView;
    private ImageView mainPlayButtonView = new ImageView(ImageResources.MAIN_PLAY_ICON);
    private long playlistLengthMilliseconds = 0;
    private AppLabel playlistNameLabel = new AppLabel("");
    private AppLabel playlistDescriptionLabel = new AppLabel("");
    private AppLabel editLabel = new AppLabel("Edit");
    private AppLabel shuffleLabel = new AppLabel("Shuffle: Off");
    private EditPlaylistDialog editDialog = new EditPlaylistDialog();

    public PlaylistScreen(Playlist playlist, AppController controller) {
        trackListView = new ListView<>();
        trackListView.setItems(playlist.getTracks());

        mainPlayButtonView.setFitHeight(64);
        mainPlayButtonView.setFitWidth(64);

        mainPlayButtonView.setOnMouseClicked(e -> {
            if (playlist.isCurrentPlaylistProperty().get()) {
                controller.togglePlay();
            } else {
                controller.handlePlayRequest(playlist, playlist.getCurrentIndex());
            }
        });

        mainPlayButtonView.imageProperty().bind(
                Bindings.when(
                        playlist.isCurrentPlaylistProperty()
                                .and(controller.pausedProperty().not()))
                        .then(ImageResources.PAUSE_ICON)
                        .otherwise(ImageResources.PLAY_ICON));

        playlistNameLabel.textProperty().bind(playlist.playlistNameProperty());
        playlistDescriptionLabel.textProperty().bind(playlist.playlistDescriptionProperty());

        shuffleLabel.textProperty()
                .bind(Bindings.when(playlist.onShuffleProperty()).then("Shuffle: On").otherwise("Shuffle: Off"));

        editLabel.setOnMousePressed(e -> {
            Pair<String, String> res = editDialog.run(playlist.getPlaylistName(), playlist.getPlaylistDescription());

            playlist.setPlaylistName(res.getKey());
            playlist.setPlaylistDescription(res.getValue());
        });

        shuffleLabel.setOnMousePressed(e -> playlist.toggleShuffle());

        for (WavFile track : playlist.getTracks()) {
            playlistLengthMilliseconds += track.getDuration();
        }

        AppLabel trackInfoLabel = new AppLabel(TrackRow.formatMilliSeconds(playlistLengthMilliseconds) + " | "
                + playlist.size() + (playlist.size() != 1 ? " tracks" : " track"));

        Region spacer = new Region();

        // Top most row in screen, shows listed information
        HBox topInfoRow = new HBox(playlistNameLabel, trackInfoLabel, spacer, editLabel, shuffleLabel);

        HBox.setHgrow(spacer, Priority.ALWAYS);
        topInfoRow.setSpacing(15);
        topInfoRow.setMaxWidth(Double.MAX_VALUE);

        // infoContainer - all track info (description, tracks #, length, etc.) but not
        // play button
        VBox infoContainer = new VBox(topInfoRow, playlistDescriptionLabel);

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
                    setGraphic(
                            new TrackRow(track, playlist, this::handleTrackPlay, this::handleTrackRemove,
                                    controller));
                }
            }

            private void handleTrackPlay(Integer index) {
                controller.handlePlayRequest(playlist, index);
            }

            private void handleTrackRemove(Integer index) {
                playlist.removeTrack(index);
            }
        });

        setCenter(trackListView);
        setTop(topInfoBox);
        setPadding(new Insets(10));
    }

    // Dialog window for editing playlist name and description
    private class EditPlaylistDialog extends Dialog<Pair<String, String>> {
        private TextField nameField;
        private TextField descriptionField;

        public EditPlaylistDialog() {
            setTitle("Edit playlist");
            setHeaderText("Enter name and escription:");

            nameField = new TextField();
            descriptionField = new TextField();

            GridPane formPane = new GridPane();
            formPane.setVgap(10);
            formPane.setHgap(10);

            formPane.addRow(0, new AppLabel("Name:"), nameField);
            formPane.addRow(1, new AppLabel("Description:"), descriptionField);

            getDialogPane().setContent(formPane);

            ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

            setResultConverter(dialogButton -> {
                if (dialogButton == acceptButtonType) {
                    return new Pair<>(nameField.getText(), descriptionField.getText());
                }
                return null;
            });
        }

        public Pair<String, String> run(String defaultName, String defaultDescription) {
            nameField.setPromptText(defaultName);
            descriptionField.setPromptText(defaultDescription);

            Optional<Pair<String, String>> result = showAndWait();

            if (result.isPresent()) {
                return result.get();
            } else {
                return new Pair<String, String>(defaultName, defaultDescription);
            }
        }
    }
}
