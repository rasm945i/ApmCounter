package dk.rasmusbendix.apmcounter.apm;

import lombok.Getter;
import lombok.Setter;
import net.twasi.obsremotejava.OBSRemoteController;

import java.util.HashMap;
import java.util.Map;

public class ObsIntegration {

    private OBSRemoteController controller;
    @Getter @Setter private String websocket;
    @Getter @Setter private String password;
    private final HashMap<String, Map<String, Object>> cachedSourceSettings;

    @FunctionalInterface
    public interface ConnectionCallback {
        void callback();
    }

    public ObsIntegration() {
        this("","");
    }

    public ObsIntegration(String websocket) {
        this(websocket, "");
    }

    public ObsIntegration(String websocket, String password) {
        this.websocket = websocket;
        this.password = password;
        this.cachedSourceSettings = new HashMap<>();
    }

    public void connect(ConnectionCallback onConnect, ConnectionCallback onDisconnect, ConnectionCallback onClose, ConnectionCallback onFail) {
        if(websocket.isEmpty()) {
            System.out.println("No websocket URL specified!");
            return;
        }
        this.controller = password.isEmpty() ?
                new OBSRemoteController(websocket, false, (String)null, false) :
                new OBSRemoteController(websocket, false, password, false);

        this.controller.registerConnectCallback((versionResponse -> onConnect.callback()));
        this.controller.registerDisconnectCallback(onDisconnect::callback);
        this.controller.registerCloseCallback((i, s) -> onClose.callback());
        this.controller.registerConnectionFailedCallback(s -> onFail.callback());

        controller.connect();

        if(controller.isFailed()) {
            System.out.println("Failed to connect to OBS Websocket!");
        }
    }

    public boolean isConnected() {
        return controller != null && !controller.isFailed();
    }

    public void updateSource(String source, String newText) {

        Map<String, Object> cachedSettings = cachedSourceSettings.getOrDefault(source, null);

        if(cachedSettings == null) {
            controller.getSourceSettings(source, cb -> {
                Map<String, Object> settings = cb.getSourceSettings();
                settings.put("text", newText);
                cachedSourceSettings.put(source, settings);
                controller.setSourceSettings(source, settings, nl -> {});
            });
            return;
        }

        cachedSettings.put("text", newText);
        controller.setSourceSettings(source, cachedSettings, nl -> {});

    }

    public void disconnect() {
        if(controller != null)
            controller.disconnect();
    }

}
