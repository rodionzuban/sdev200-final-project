import controller.AppController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.layout.AppLayout;

public class App extends Application {
    public void start(Stage stage) {
        AppController controller = new AppController();
        AppLayout layout = new AppLayout(controller, stage);
        controller.setAppLayout(layout);

        Scene scene = new Scene(layout);
        stage.setTitle("WAV Library");
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}
