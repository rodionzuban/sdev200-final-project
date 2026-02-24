package ui.layout;

import controller.AppController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.screens.HomeScreen;
import ui.screens.ImportScreen;
import ui.screens.PlaylistsScreen;
import ui.screens.ScreenType;

// AppLayout - manages UI interactions and navigation
public final class AppLayout extends BorderPane {
    private NavigationBar navigation;
    private NowPlaying nowPlaying;
    private HomeScreen homeScreen;
    private PlaylistsScreen playlistsScreen;
    private ImportScreen importScreen;
    private Node currentScreen;

    public AppLayout(AppController controller, Stage stage) {
        navigation = new NavigationBar(this::switchScreen);
        nowPlaying = new NowPlaying(controller::togglePlay, controller::nextTrack, controller::previousTrack);
        homeScreen = new HomeScreen(controller);
        playlistsScreen = new PlaylistsScreen(controller);
        importScreen = new ImportScreen(stage);
        currentScreen = homeScreen;

        setTop(navigation);
        setBottom(nowPlaying);
        setCenter(currentScreen);

        setPadding(new Insets(10));
    }

    private void switchScreen(ScreenType screen) {

        switch (screen) {
            case HOME:
                setCenter(homeScreen);
                break;

            case PLAYLISTS:
                playlistsScreen.reset();
                setCenter(playlistsScreen);
                break;

            case IMPORT:
                importScreen.reset();
                setCenter(importScreen);
                break;
        }
    }
}
