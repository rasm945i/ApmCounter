package dk.rasmusbendix.apmcounter;

import com.github.kwhat.jnativehook.GlobalScreen;
import dk.rasmusbendix.apmcounter.apm.ObsIntegration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public class Main extends Application {

    @Getter private static final ObsIntegration obsIntegration = new ObsIntegration();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/main_controller.fxml"));
        primaryStage.setTitle("Bendix' APM Counter");
        primaryStage.setScene(new Scene(root, 700, 500));

        primaryStage.show();
        primaryStage.sizeToScene();

        // Allow some resizing for people who uses fonts and font-sizes that are not what I used for testing
        // Restricted resizing to minimize how stupid the application looks in fullscreen and "tinyscreen?"
        primaryStage.setMaxHeight(primaryStage.getHeight() * 1.25);
        primaryStage.setMaxWidth(primaryStage.getWidth() * 1.25);

        primaryStage.setMinHeight(primaryStage.getHeight() * 0.75);
        primaryStage.setMinWidth(primaryStage.getWidth() * 0.75);



        // Mouse and keyboard events
        GlobalScreen.registerNativeHook();

    }


    @Override
    public void stop() throws Exception {
        MainController.shutdown();
        GlobalScreen.unregisterNativeHook();
        obsIntegration.disconnect();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
