package ui.screens;

import controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import model.WavFile;
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
                    setGraphic(new TrackRow(track, controller.getGlobalCollection(), null));
                }
            }
        });

        setCenter(trackListView);
        setPadding(new Insets(10));
    }
}
