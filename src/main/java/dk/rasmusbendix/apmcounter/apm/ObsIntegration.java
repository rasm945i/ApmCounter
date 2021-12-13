package dk.rasmusbendix.apmcounter.apm;

import lombok.Getter;
import lombok.Setter;
import net.twasi.obsremotejava.OBSRemoteController;

import java.util.Map;

public class ObsIntegration {

    private OBSRemoteController controller;
    @Getter @Setter private String websocket;
    @Getter @Setter private String password;

    public ObsIntegration() {
        websocket = "";
        password = "";
    }

    public ObsIntegration(String websocket) {
        this(websocket, "");
    }

    public ObsIntegration(String websocket, String password) {
        this.websocket = websocket;
        this.password = password;
        connect();
    }

    public void connect() {
        if(websocket.isEmpty()) {
            System.out.println("No websocket URL specified!");
            return;
        }
        this.controller = password.isEmpty() ?
                new OBSRemoteController(websocket, false) :
                new OBSRemoteController(websocket, false, password);

        if(controller.isFailed()) {
            System.out.println("Failed to connect to OBS Websocket!");
        }
    }

    public boolean isConnected() {
        return controller != null && !controller.isFailed();
    }

    public void updateSource(String source, String newText) {

        controller.getSourceSettings(source, cb -> {
            Map<String, Object> settings = cb.getSourceSettings();
            settings.put("text", newText);
            controller.setSourceSettings(source, settings, nl -> {});
        });

    }

    public void disconnect() {
        if(controller != null)
            controller.disconnect();
    }

}
