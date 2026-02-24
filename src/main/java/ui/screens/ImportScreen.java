package ui.screens;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import util.FileScanner;
import model.WavFile;

// ImportScreen - loads .wav files from file or directory selection, then sends to ConfirmImportScreen
public class ImportScreen extends BorderPane {
    private Button fileImportButton = new Button("File");
    private Button folderImportButton = new Button("Folder");
    private VBox centerSelectionContainer = new VBox();

    public ImportScreen(Stage stage) {
        HBox buttonContainer = new HBox(fileImportButton, folderImportButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(25);

        centerSelectionContainer.getChildren().addAll(new AppLabel(AppLabelType.SUBTITLE, "Choose your import type:"),
                buttonContainer);
        centerSelectionContainer.setSpacing(15);
        centerSelectionContainer.setAlignment(Pos.CENTER);
        centerSelectionContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // load file import dialog
        fileImportButton.setOnMousePressed(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open WAV File");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            try {
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("WAV files", "*.wav"));
                WavFile selectedFile = new WavFile(fileChooser.showOpenDialog(stage));
                List<WavFile> fileList = new ArrayList<WavFile>();
                fileList.add(selectedFile);
                setCenter(new ConfirmImportScreen(fileList, this));
            } catch (Exception ex) {

            }
        });

        // load directory import dialog
        folderImportButton.setOnMousePressed(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Directory");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            try {
                File selectedDirectory = directoryChooser.showDialog(stage);
                if (selectedDirectory != null && selectedDirectory.isDirectory()) {
                    List<WavFile> fileList = FileScanner.scanDirectory(selectedDirectory, true);
                    setCenter(new ConfirmImportScreen(fileList, this));
                }
            } catch (Exception ex) {

            }
        });

        setCenter(centerSelectionContainer);
    }

    // close import confirmation (for re-navigation)
    public void reset() {
        setCenter(centerSelectionContainer);
    }
}
