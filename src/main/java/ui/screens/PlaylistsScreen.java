package ui.screens;

import java.util.function.Consumer;

import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Playlist;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import util.ImageResources;
import util.ResourceLoader;

// PlaylistsScreen - shows all user playlists according to AppController
public class PlaylistsScreen extends BorderPane {
    private ListView<Playlist> playlistListView;
    private AppController controller;
    private ImageView addButtonView;
    private HBox headerBox;

    public PlaylistsScreen(AppController controller) {
        this.controller = controller;

        playlistListView = new ListView<>();
        playlistListView.setItems(controller.getPlaylists());

        addButtonView = new ImageView(ImageResources.ADD_ICON);
        addButtonView.setFitHeight(32);
        addButtonView.setFitWidth(32);
        addButtonView.setOnMousePressed(e -> {
            Playlist p = new Playlist(controller, "My Playlist " + (controller.getPlaylists().size() + 1), "");
            controller.getPlaylists().add(p);
        });

        playlistListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Playlist playlist, boolean empty) {
                super.updateItem(playlist, empty);

                final PlaylistRow row = new PlaylistRow(PlaylistsScreen.this::handlePlaylistPlay,
                        PlaylistsScreen.this::handlePlaylistSelect, PlaylistsScreen.this::handlePlaylistRemove,
                        controller);

                if (empty || playlist == null) {
                    setGraphic(null);
                } else {
                    row.setPlaylist(playlist);
                    setGraphic(row);
                }
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox = new HBox(new AppLabel(AppLabelType.SUBTITLE, "Your playlists"), spacer, addButtonView);

        BorderPane.setMargin(headerBox, new Insets(0, 0, 10, 0));

        setPadding(new Insets(10));
        setCenter(playlistListView);
        setTop(headerBox);
    }

    // PlaylistRow - UI element for displaying a playlist's data
    // Clicking playlist info opens related PlaylistScreen. Clicking play button
    // plays playlist
    private class PlaylistRow extends HBox {
        private Playlist playlist;

        private Image playButton = new Image(ResourceLoader.loadResourceFile("play-icon.png"));
        private VBox trackInfo = new VBox();
        private ImageView playButtonView;
        private ImageView removeButtonView;

        public PlaylistRow(Consumer<Playlist> onPlay,
                Consumer<Playlist> onSelect, Consumer<Playlist> onRemove, AppController controller) {
            playButtonView = new ImageView(playButton);
            playButtonView.setFitWidth(32);
            playButtonView.setFitHeight(32);

            removeButtonView = new ImageView(ImageResources.REMOVE_ICON);
            removeButtonView.setFitHeight(32);
            removeButtonView.setFitWidth(32);

            playButtonView.setOnMousePressed(e -> {
                if (playlist != null) {
                    onPlay.accept(playlist);
                }
            });
            trackInfo.setOnMousePressed(e -> {
                if (playlist != null) {
                    onSelect.accept(playlist);
                }
            });
            removeButtonView.setOnMousePressed(e -> {
                if (playlist != null) {
                    onRemove.accept(playlist);
                }
            });

            Region spacer = new Region();

            getChildren().addAll(playButtonView, trackInfo, spacer, removeButtonView);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            setAlignment(Pos.CENTER_LEFT);
        }

        public void setPlaylist(Playlist playlist) {
            this.playlist = playlist;
            AppLabel nameLabel = new AppLabel("");
            AppLabel lengthLabel = new AppLabel(playlist.size() + (playlist.size() != 1 ? " tracks" : " track"));

            nameLabel.textProperty().bind(playlist.playlistNameProperty());
            playlist.sizeProperty().addListener((obs, oldSize, newSize) -> {
                lengthLabel.setText(newSize + (newSize.intValue() != 1 ? " tracks" : " track"));
            });

            trackInfo.getChildren().addAll(nameLabel,
                    lengthLabel);

            playButtonView.imageProperty().bind(
                    Bindings.when(
                            playlist.isCurrentPlaylistProperty()
                                    .and(controller.pausedProperty().not()))
                            .then(ImageResources.PAUSE_ICON)
                            .otherwise(ImageResources.PLAY_ICON));

        }
    }

    private void handlePlaylistPlay(Playlist playlist) {
        if (playlist.isCurrentPlaylistProperty().get()) {
            controller.togglePlay();
        } else {
            controller.handlePlayRequest(playlist, playlist.getCurrentIndex());
        }
    }

    private void handlePlaylistSelect(Playlist playlist) {
        setCenter(new PlaylistScreen(playlist, controller));
        setTop(null);
    }

    private void handlePlaylistRemove(Playlist playlist) {
        controller.removePlaylist(playlist);
    }

    public void reset() {
        setCenter(playlistListView);
        setTop(headerBox);
    }
}
