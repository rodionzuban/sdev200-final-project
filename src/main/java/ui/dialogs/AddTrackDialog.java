package ui.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import controller.AppController;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.Playlist;
import model.WavFile;

// custom multi-select dialog for choosing playlists to add a track to
public class AddTrackDialog extends Dialog<List<Playlist>> {
    Dialog<List<Playlist>> dialog;

    public AddTrackDialog(WavFile track, AppController controller) {
        dialog = new Dialog<>();
        dialog.setTitle("Select playlists to add track to");
        dialog.setHeaderText("Hold Ctrl to select multiple playlists");

        ListView<Playlist> allPlaylistsView = new ListView<Playlist>();
        allPlaylistsView.getItems().addAll(controller.getPlaylists());

        allPlaylistsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (Playlist playlist : allPlaylistsView.getItems()) {
            if (playlist.contains(track)) {
                allPlaylistsView.getSelectionModel().select(playlist);
            }
        }

        ButtonType okButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.getDialogPane().setContent(allPlaylistsView);

        dialog.setResultConverter(button -> {
            if (button == okButton) {
                return new ArrayList<>(allPlaylistsView.getSelectionModel().getSelectedItems());
            }
            return null;
        });
    }

    // method to show dialog and fetch results
    public List<Playlist> run() {
        Optional<List<Playlist>> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }
}