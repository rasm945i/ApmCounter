package dk.rasmusbendix.apmcounter;

import lombok.Getter;
import lombok.Setter;

import java.util.prefs.Preferences;

public class ApmSettings {

    private final Preferences userprefs;

    // CSV settings
    @Getter @Setter private boolean saveToCsv;
    @Getter @Setter private String csvSavePath;
    @Getter @Setter private boolean csvAutoSave;
    @Getter @Setter private int csvAutoSaveInterval;

    // OBS settings
    @Getter @Setter private boolean enableObsIntegration;
    @Getter @Setter private String websocketUrl;
    @Getter @Setter private String password;

    @Getter @Setter private String eventWrapperJson;

    private ApmSettings(Preferences preferences) {
        this.userprefs = preferences;
    }

    public void savePreferences() {

        userprefs.putBoolean("save-to-csv", isSaveToCsv());
        userprefs.put("csv-save-path", getCsvSavePath());
        userprefs.putBoolean("csv-auto-save", isCsvAutoSave());
        userprefs.putInt("csv-autosave-interval", getCsvAutoSaveInterval());

        userprefs.putBoolean("enable-obs-integration", isEnableObsIntegration());
        userprefs.put("obs-websocket-url", getWebsocketUrl());
        userprefs.put("obs-password", getPassword());

        userprefs.put("wrappers", eventWrapperJson);

    }

    public static ApmSettings getFromPreferences() {

        Preferences userprefs = Preferences.userRoot().node("/apmcounter");
        ApmSettings settings = new ApmSettings(userprefs);

        settings.setSaveToCsv(userprefs.getBoolean("save-to-csv", false));
        settings.setCsvSavePath(userprefs.get("csv-save-path", ""));
        settings.setCsvAutoSave(userprefs.getBoolean("csv-auto-save", false));
        settings.setCsvAutoSaveInterval(userprefs.getInt("csv-autosave-interval", 300));

        settings.setEnableObsIntegration(userprefs.getBoolean("enable-obs-integration", false));
        settings.setWebsocketUrl(userprefs.get("obs-websocket-url", ""));
        settings.setPassword(userprefs.get("obs-password", ""));

        settings.setEventWrapperJson(userprefs.get("wrappers", ""));

        return settings;

    }

}
