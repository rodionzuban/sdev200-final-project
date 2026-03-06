package ui.screens;

import controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import model.WavFile;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import ui.elements.TrackRow;

// HomeScreen - displays all tracks in AppController's globalCollection
// updates when tracks are added / removed
public class HomeScreen extends BorderPane {
    private ListView<WavFile> trackListView;

    public HomeScreen(AppController controller) {
        trackListView = new ListView<>();
        trackListView.setItems(controller.getGlobalCollection().getTracks());

        trackListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(WavFile track, boolean empty) {
                super.updateItem(track, empty);

                if (empty || track == null) {
                    setGraphic(null);
                } else {
                    setGraphic(
                            new TrackRow(track, controller.getGlobalCollection(), this::handleTrackPlay,
                                    this::handleTrackRemove, controller));
                }
            }

            private void handleTrackPlay(Integer index) {
                controller.handlePlayRequest(controller.getGlobalCollection(), index);
            }

            private void handleTrackRemove(Integer index) {
                controller.removeTrackGlobally(index);
            }
        });

        AppLabel noTracksLabel = new AppLabel(AppLabelType.SUBTITLE, "No tracks in library. Add files to get started!");
        StackPane centerPane = new StackPane(trackListView, noTracksLabel);

        trackListView.visibleProperty().bind(controller.getGlobalCollection().sizeProperty().greaterThan(0));
        noTracksLabel.visibleProperty().bind(controller.getGlobalCollection().sizeProperty().isEqualTo(0));

        setCenter(centerPane);
        setPadding(new Insets(10));
    }
}
