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
        primaryStage.setScene(new Scene(root, 700, 475));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.sizeToScene();

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
