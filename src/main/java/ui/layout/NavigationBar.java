package ui.layout;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ui.elements.AppLabel;
import ui.elements.AppLabelType;
import ui.screens.ScreenType;

// NavigationBar - UI element that acts as a messenger for user requests to navigate to a page, which are handled in AppLayout
public class NavigationBar extends HBox {
    private Label homeLabel = new AppLabel(AppLabelType.SUBTITLE, "Home");
    private Label playlistsLabel = new AppLabel(AppLabelType.SUBTITLE, "Playlists");
    private Label addFilesLabel = new AppLabel(AppLabelType.SUBTITLE, "Add Files");
    private Consumer<ScreenType> handleNavigation;

    public NavigationBar(Consumer<ScreenType> handleNavigation) {
        homeLabel.setUnderline(true);
        this.handleNavigation = handleNavigation;
        homeLabel.setOnMousePressed((e) -> {
            this.handleNavigation.accept(ScreenType.HOME);
            homeLabel.setUnderline(true);
            playlistsLabel.setUnderline(false);
            addFilesLabel.setUnderline(false);
        });
        playlistsLabel.setOnMousePressed((e) -> {
            this.handleNavigation.accept(ScreenType.PLAYLISTS);
            homeLabel.setUnderline(false);
            playlistsLabel.setUnderline(true);
            addFilesLabel.setUnderline(false);

        });
        addFilesLabel.setOnMousePressed((e) -> {
            this.handleNavigation.accept(ScreenType.IMPORT);
            homeLabel.setUnderline(false);
            playlistsLabel.setUnderline(false);
            addFilesLabel.setUnderline(true);
        });

        getChildren().addAll(new AppLabel(AppLabelType.TITLE, "WAV Library"), homeLabel, playlistsLabel,
                addFilesLabel);
        setSpacing(10);
        setPadding(new Insets(10, 0, 10, 0));
        setAlignment(Pos.BOTTOM_LEFT);
        setStyle("-fx-border-color: black; -fx-border-width: 0 0 1px 0;");
    }
}
