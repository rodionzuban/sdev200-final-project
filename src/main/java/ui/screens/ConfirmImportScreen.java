package ui.screens;

import java.util.List;
import java.util.function.Consumer;

import controller.AppController;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.TrackCollection;
import model.WavFile;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import ui.elements.TrackRow;
import util.ImageResources;
import util.ResourceLoader;

// ConfirmImportScreen - appears after user successfully loads new tracks to import
// here the user can rename and remove tracks from the import batch before accepting an import
public class ConfirmImportScreen extends BorderPane {
    private ListView<WavFile> trackListView;
    private TrackCollection importCollection;
    private Button importButton = new Button("Import");
    private Button cancelButton = new Button("Cancel");

    public ConfirmImportScreen(List<WavFile> imports, ImportScreen parent, AppController controller) {
        importCollection = new TrackCollection(imports);

        trackListView = new ListView<>();
        trackListView.setItems(importCollection.getTracks());

        int size = imports.size();
        AppLabel titleLabel = new AppLabel(AppLabelType.SUBTITLE,
                "Confirm your imports (" + size + (size != 1 ? " tracks" : " track") + " found):");

        importButton.setOnMousePressed(e -> {
            for (WavFile track : trackListView.getItems()) {
                controller.getGlobalCollection().addTrack(track);
                // how can i use getCustomName to change the name of the track?

            }

            parent.reset();
        });

        Region spacer = new Region();

        HBox topContainer = new HBox(titleLabel, spacer, cancelButton, importButton);
        topContainer.setSpacing(15);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        BorderPane.setMargin(topContainer, new Insets(0, 0, 10, 0));

        trackListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(WavFile track, boolean empty) {
                super.updateItem(track, empty);

                if (empty || track == null) {
                    setGraphic(null);
                } else {
                    TempTrackRow row = new TempTrackRow(track, e -> handleOnPlay(track), e -> handleOnDelete(track),
                            controller);

                    setGraphic(row);
                }
            }

            private void handleOnPlay(WavFile track) {
                if (controller.getCurrentCollection() != null
                        && controller.getCurrentCollection().getTrack() == track) {
                    controller.togglePlay();
                } else {
                    controller.handlePlayRequest(importCollection, importCollection.indexOf(track));
                }
            }

            private void handleOnDelete(WavFile track) {
                importCollection.removeTrack(importCollection.indexOf(track));
            }
        });

        cancelButton.setOnMousePressed(e -> parent.reset());

        setTop(topContainer);
        setCenter(trackListView);
        setPadding(new Insets(10));
    }

    private class TempTrackRow extends HBox {
        private WavFile track;
        private ImageView playButtonView;
        private ImageView removeButtonView;
        private TextField nameField;

        public TempTrackRow(WavFile track, Consumer<WavFile> onPlay, Consumer<WavFile> onDelete,
                AppController controller) {
            nameField = new TextField(track.getTitle());
            this.track = track;

            playButtonView = new ImageView(ImageResources.PLAY_ICON);
            playButtonView.setFitWidth(32);
            playButtonView.setFitHeight(32);
            playButtonView.setOnMousePressed(e -> onPlay.accept(track));

            playButtonView.imageProperty().bind(Bindings
                    .when(controller.currentTrackProperty().isNotNull().and(
                            controller.currentTrackProperty().isEqualTo(this.track)
                                    .and(controller.pausedProperty().not())))
                    .then(ImageResources.PAUSE_ICON).otherwise(ImageResources.PLAY_ICON));

            removeButtonView = new ImageView(new Image(ResourceLoader.loadResourceFile("remove-icon.png")));
            removeButtonView.setFitHeight(32);
            removeButtonView.setFitWidth(32);
            removeButtonView.setOnMousePressed(e -> onDelete.accept(track));

            nameField.textProperty().addListener((obs, old, val) -> track.setTitle(val));

            VBox trackInfo = new VBox(nameField,
                    new AppLabel(TrackRow.formatMilliSeconds(track.getDuration())));

            Region spacer = new Region();

            getChildren().addAll(playButtonView, trackInfo, spacer, removeButtonView);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            setAlignment(Pos.CENTER_LEFT);

        }
    }
}
