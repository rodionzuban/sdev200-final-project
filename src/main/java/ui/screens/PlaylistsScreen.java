package ui.screens;

import java.util.function.Consumer;

import controller.AppController;
import javafx.collections.ObservableList;
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
import util.ResourceLoader;

// PlaylistsScreen - shows all user playlists according to AppController
public class PlaylistsScreen extends BorderPane {
    private ListView<Playlist> playlistListView;
    private AppController controller;

    public PlaylistsScreen(AppController controller) {
        playlistListView = new ListView<>();
        playlistListView.setItems(controller.getPlaylists());

        this.controller = controller;

        playlistListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Playlist playlist, boolean empty) {
                super.updateItem(playlist, empty);

                final PlaylistRow row = new PlaylistRow(controller.getPlaylists(),
                        PlaylistsScreen.this::handlePlaylistPlay, PlaylistsScreen.this::handlePlaylistSelect);

                if (empty || playlist == null) {
                    setGraphic(null);
                } else {
                    row.setPlaylist(playlist);
                    setGraphic(row);
                }
            }
        });

        setPadding(new Insets(10));
        setCenter(playlistListView);
    }

    // PlaylistRow - UI element for displaying a playlist's data
    // Clicking playlist info opens related PlaylistScreen. Clicking play button
    // plays playlist
    private class PlaylistRow extends HBox {
        private Playlist playlist;
        private ObservableList<Playlist> allPlaylists;
        private Image playButton = new Image(ResourceLoader.loadResourceFile("play-icon.png"));
        private VBox trackInfo = new VBox();
        private ImageView playButtonView;
        private ImageView removeButtonView;

        public PlaylistRow(ObservableList<Playlist> allPlaylists, Consumer<Playlist> onPlay,
                Consumer<Playlist> onSelect) {
            playButtonView = new ImageView(playButton);
            playButtonView.setFitWidth(32);
            playButtonView.setFitHeight(32);

            removeButtonView = new ImageView(new Image(ResourceLoader.loadResourceFile("remove-icon.png")));
            removeButtonView.setFitHeight(32);
            removeButtonView.setFitWidth(32);

            this.allPlaylists = allPlaylists;
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
            Region spacer = new Region();

            getChildren().addAll(playButtonView, trackInfo, spacer, removeButtonView);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            setAlignment(Pos.CENTER_LEFT);
        }

        public void setPlaylist(Playlist playlist) {
            this.playlist = playlist;
            trackInfo.getChildren().addAll(new AppLabel(playlist.getPlaylistName()),
                    new AppLabel(playlist.size() + (playlist.size() != 1 ? " tracks" : " track")));
        }
    }

    private void handlePlaylistPlay(Playlist playlist) {
        handlePlaylistSelect(playlist);
    }

    private void handlePlaylistSelect(Playlist playlist) {
        setCenter(new PlaylistScreen(playlist, controller));
    }

    public void reset() {
        setCenter(playlistListView);
    }
}
