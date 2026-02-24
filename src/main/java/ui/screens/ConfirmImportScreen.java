package ui.screens;

import java.util.List;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
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
import model.Playlist;
import model.TrackCollection;
import model.WavFile;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import ui.elements.TrackRow;
import util.ResourceLoader;

// ConfirmImportScreen - appears after user successfully loads new tracks to import
// here the user can rename and remove tracks from the import batch before accepting an import
public class ConfirmImportScreen extends BorderPane {
    private ListView<WavFile> trackListView;
    private Button importButton = new Button("Import");
    private Button cancelButton = new Button("Cancel");

    public ConfirmImportScreen(List<WavFile> imports, ImportScreen parent) {

        trackListView = new ListView<>();
        trackListView.setItems(FXCollections.observableArrayList(imports));

        int size = imports.size();
        AppLabel titleLabel = new AppLabel(AppLabelType.SUBTITLE,
                "Confirm your imports (" + size + (size != 1 ? " tracks" : " track") + " found):");

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
                    setGraphic(new TempTrackRow(track, null));
                }
            }
        });

        cancelButton.setOnMousePressed(e -> parent.reset());

        setTop(topContainer);
        setCenter(trackListView);
        setPadding(new Insets(10));
    }

    private class TempTrackRow extends HBox {
        private WavFile track;
        private Image playButton = new Image(ResourceLoader.loadResourceFile("play-icon.png"));
        private ImageView playButtonView;
        private ImageView removeButtonView;
        private TextField nameField;

        public TempTrackRow(WavFile track, Consumer<Integer> onPlay) {
            nameField = new TextField(track.getTitle());

            playButtonView = new ImageView(playButton);
            playButtonView.setFitWidth(32);
            playButtonView.setFitHeight(32);

            removeButtonView = new ImageView(new Image(ResourceLoader.loadResourceFile("remove-icon.png")));
            removeButtonView.setFitHeight(32);
            removeButtonView.setFitWidth(32);

            VBox trackInfo = new VBox(nameField,
                    new AppLabel(TrackRow.formatMilliSeconds(track.getDuration())));

            this.track = track;

            Region spacer = new Region();

            getChildren().addAll(playButtonView, trackInfo, spacer, removeButtonView);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            setAlignment(Pos.CENTER_LEFT);

        }

        public String getCustomName() {
            return nameField.getText();
        }
    }
}
