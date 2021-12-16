package dk.rasmusbendix.apmcounter;

import dk.rasmusbendix.apmcounter.apm.EventWrapper;
import dk.rasmusbendix.apmcounter.apm.ObsIntegration;
import dk.rasmusbendix.apmcounter.csv.CsvSaver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class MainController {

    private static final double FADED_VALUE = 0.65;
    private static final String CSV_INVALID_LOCATION = "Invalid Location";

    private static ArrayList<EventWrapper> staticKeyCollection = new ArrayList<>();
    private EventWrapper currentlySelected;
    private ApmSettings settings;

    @FXML
    private Button addKeyCollection;

    @FXML
    private RadioButton allClicksRadio;

    @FXML
    private RadioButton allKeysRadio;

    @FXML
    private Button applySettingsButton;

    @FXML
    private ToggleGroup clicks;

    @FXML
    private CheckBox csvAutosaveEnabled;

    @FXML
    private CheckBox csvEnabled;

    @FXML
    private Button csvLocationButton;

    @FXML
    private Text csvLocationString;

    @FXML
    private TextField csvSaveInterval;

    @FXML
    private CheckBox enableObsIntegration;

    @FXML
    private TextField groupName;

    @FXML
    private Button importSettingsButton;

    @FXML
    private ListView<EventWrapper> keyCollection;

    @FXML
    private CheckBox keyboardCheckbox;

    @FXML
    private ToggleGroup keys;

    @FXML
    private CheckBox mouseCheckbox;

    @FXML
    private TextField passwordField;

    @FXML
    private Button removeKeyCollection;

    @FXML
    private Button saveButton;

    @FXML
    private Button selectClicksButton;

    @FXML
    private Button selectKeysButton;

    @FXML
    private ListView<Integer> selectedKeysList;

    @FXML
    private ListView<Integer> selectedClicksList;

    @FXML
    private RadioButton specificClicksRadio;

    @FXML
    private RadioButton specificKeysRadio;

    @FXML
    private TextField websocketField;

    @FXML
    private RadioButton changeRadio;

    @FXML
    private RadioButton intervalRadio;

    @FXML
    private RadioButton allActionsRadio;

    @FXML
    private RadioButton actionLastXRadio;

    @FXML
    private TextField actionsPerMs;

    @FXML
    private ToggleGroup updateGroup;

    @FXML
    private TextField updateIntervalMs;

    @FXML
    private Text obsStatusTextField;

    @FXML
    private Button obsDisconnectButton;

    @FXML
    private Button obsConnectButton;

    private final ObsIntegration.ConnectionCallback connect = () -> {
        Platform.runLater(() -> {
            obsStatusTextField.setText("Status: Connected");

            obsConnectButton.setDisable(true);
            obsConnectButton.setOpacity(FADED_VALUE);

            obsDisconnectButton.setDisable(false);
            obsDisconnectButton.setOpacity(1);
        });
    };

    private final ObsIntegration.ConnectionCallback disconnect = () -> {
        Platform.runLater(() -> {
            obsStatusTextField.setText("Status: Disconnected");

            obsConnectButton.setDisable(false);
            obsConnectButton.setOpacity(1);

            obsDisconnectButton.setDisable(true);
            obsDisconnectButton.setOpacity(FADED_VALUE);
        });
    };

    private final ObsIntegration.ConnectionCallback close = () -> {
        Platform.runLater(() -> {
            obsStatusTextField.setText("Status: Disconnected");

            obsConnectButton.setDisable(false);
            obsConnectButton.setOpacity(1);

            obsDisconnectButton.setDisable(true);
            obsDisconnectButton.setOpacity(FADED_VALUE);
        });
    };

    private final ObsIntegration.ConnectionCallback failed = () -> {
        Platform.runLater(() -> {
            obsStatusTextField.setText("Status: Failed");

            obsConnectButton.setDisable(false);
            obsConnectButton.setOpacity(1);

            obsDisconnectButton.setDisable(true);
            obsDisconnectButton.setOpacity(FADED_VALUE);
        });
    };

    public void initialize() {

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            // Max 7 digits
            if(csvSaveInterval.getText().length() > 6 && change.isAdded())
                return null;

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        // Ugly code, don't look. Apparently 1 formatter can only be used once, sadly
        TextFormatter<String> csvFormatter, actionFormatter, updateFormatter;
        csvFormatter = new TextFormatter<>(filter);
        actionFormatter = new TextFormatter<>(filter);
        updateFormatter = new TextFormatter<>(filter);

        csvSaveInterval.setTextFormatter(csvFormatter);
        actionsPerMs.setTextFormatter(actionFormatter);
        updateIntervalMs.setTextFormatter(updateFormatter);

        settings = ApmSettings.getFromPreferences();


        // Keep track of selected group
        keyCollection.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentlySelected = newValue;
            if(currentlySelected != null)
                updateSettingsUI();
        });

        // Default to selecting first item to avoid confusion with the UI
        if(!keyCollection.getItems().isEmpty())
            keyCollection.getSelectionModel().select(0);

        updateStaticKeyCollection();
        updateGlobalSettingsUI();

        if(settings.isEnableObsIntegration()) {
            Platform.runLater(() -> Main.getObsIntegration().connect(connect, disconnect, close, failed));
        }

    }

    private void updateStaticKeyCollection() {
        staticKeyCollection = new ArrayList<>(keyCollection.getItems());
    }

    public static void shutdown() {
        staticKeyCollection.forEach(EventWrapper::deconstruct);
    }


    private void updateGlobalSettingsUI() {

        csvEnabled.setSelected(settings.isSaveToCsv());
        csvAutosaveEnabled.setSelected(settings.isCsvAutoSave());
        csvSaveInterval.setText(settings.getCsvAutoSaveInterval() + "");

        if(settings.getCsvSavePath().isEmpty()) {
            CsvSaver.setCsvSaveLocation(null);
            csvLocationString.setText("N/A");
        } else {
            CsvSaver.setCsvSaveLocation(new File(settings.getCsvSavePath()));
            csvLocationString.setText(settings.getCsvSavePath());
        }


        enableObsIntegration.setSelected(settings.isEnableObsIntegration());
        websocketField.setText(settings.getWebsocketUrl());
        passwordField.setText(settings.getPassword());

        Main.getObsIntegration().setWebsocket(settings.getWebsocketUrl());
        Main.getObsIntegration().setPassword(settings.getPassword());

    }


    private void updateSettingsUI() {
        groupName.setText(currentlySelected.getId());
        mouseCheckbox.setSelected(currentlySelected.isUseMouse());
        keyboardCheckbox.setSelected(currentlySelected.isUseKeyboard());

        if(currentlySelected.isAllClicks()) {
            allClicksRadio.setSelected(true);
        } else {
            specificClicksRadio.setSelected(true);
        }

        if(currentlySelected.isAllKeys()) {
            allKeysRadio.setSelected(true);
        } else {
            specificClicksRadio.setSelected(true);
        }

        if(currentlySelected.isUpdateOnChange()) {
            changeRadio.setSelected(true);
        } else {
            intervalRadio.setSelected(true);
        }

        if(currentlySelected.isShowAllActions()) {
            allActionsRadio.setSelected(true);
        } else {
            actionLastXRadio.setSelected(true);
        }

        updateIntervalMs.setText(String.valueOf(currentlySelected.getUpdateIntervalMs()));
        actionsPerMs.setText(String.valueOf(currentlySelected.getShowActionsLastMs()));

        selectedKeysList.getItems().clear();
        selectedKeysList.getItems().addAll(currentlySelected.getKeys());

        selectedClicksList.getItems().clear();
        selectedClicksList.getItems().addAll(currentlySelected.getButtons());

    }

    @FXML
    void applyEventWrapperSettings(ActionEvent event) {

        // Nothing was selected, nothing to do
        if(currentlySelected == null)
            return;

        currentlySelected.deconstruct();

        currentlySelected.setId(groupName.getText());
        currentlySelected.setUseMouse(mouseCheckbox.isSelected());
        currentlySelected.setUseKeyboard(keyboardCheckbox.isSelected());
        currentlySelected.setAllClicks(allClicksRadio.isSelected());
        currentlySelected.setAllKeys(allKeysRadio.isSelected());

        currentlySelected.setKeys(new ArrayList<>(selectedKeysList.getItems()));
        currentlySelected.setButtons(new ArrayList<>(selectedClicksList.getItems()));

        currentlySelected.setUsingObsIntegration(enableObsIntegration.isSelected());

        if(!updateIntervalMs.getText().isEmpty())
            currentlySelected.setUpdateIntervalMs(Integer.parseInt(updateIntervalMs.getText()));
        currentlySelected.setUpdateOnChange(changeRadio.isSelected());

        if(!actionsPerMs.getText().isEmpty())
            currentlySelected.setShowActionsLastMs(Integer.parseInt(actionsPerMs.getText()));
        currentlySelected.setShowAllActions(allActionsRadio.isSelected());

        currentlySelected.setSaveToCsv(settings.isSaveToCsv());
        currentlySelected.setCsvSaveInterval(settings.isCsvAutoSave() ? settings.getCsvAutoSaveInterval() : 0);

        // Update the name of the EventWrapper in the list of wrappers
        if(keyCollection.getItems().contains(currentlySelected)) {
            int index = keyCollection.getItems().indexOf(currentlySelected);
            EventWrapper temp = currentlySelected;
            keyCollection.getItems().remove(index);
            keyCollection.getItems().add(index, temp);
            keyCollection.getSelectionModel().select(index);
        }

        currentlySelected.construct();
        System.out.println("Constructed events!");

    }

    @FXML
    void onAddCollection(ActionEvent event) {

        EventWrapper wrapper = new EventWrapper("New Group");
        keyCollection.getItems().add(wrapper);
        keyCollection.getSelectionModel().select(keyCollection.getItems().size()-1);
        updateStaticKeyCollection();

    }

    @FXML
    void onChooseCsvLocation(ActionEvent event) {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("APM - CSV Save Location");

        if(CsvSaver.getCsvSaveLocation() != null &&
                CsvSaver.getCsvSaveLocation().exists() &&
                CsvSaver.getCsvSaveLocation().isDirectory()) {
            chooser.setInitialDirectory(CsvSaver.getCsvSaveLocation());
        }

        File directory = chooser.showDialog(applySettingsButton.getScene().getWindow());

        if(directory == null) {
            return;
        }

        csvLocationString.setText(directory.getPath());
        settings.setCsvSavePath(directory.getPath());
        CsvSaver.setCsvSaveLocation(directory);

        settings.savePreferences();

    }

    @FXML
    void onImportSettings(ActionEvent event) {

//        FileChooser chooser = new FileChooser();
//        File selection = chooser.showOpenDialog(applySettingsButton.getScene().getWindow());
//        if(selection == null) {
//            settings.setCsvSavePath("");
//            csvLocationString.setText(CSV_INVALID_LOCATION);
//        }
//
//        settings = ApmSettings.fromSaveFile(selection);

    }

    @FXML
    void onRemoveCollection(ActionEvent event) {

    }

    @FXML
    void onSave(ActionEvent event) {

        Main.getObsIntegration().disconnect();


        if(csvLocationString.getText().equals(CSV_INVALID_LOCATION))
            settings.setCsvSavePath("");
        else
            settings.setCsvSavePath(csvLocationString.getText());

        settings.setSaveToCsv(csvEnabled.isSelected());
        settings.setCsvAutoSave(csvAutosaveEnabled.isSelected());

        if(!csvSaveInterval.getText().isEmpty())
            settings.setCsvAutoSaveInterval(Integer.parseInt(csvSaveInterval.getText()));
        settings.setEnableObsIntegration(enableObsIntegration.isSelected());
        settings.setPassword(passwordField.getText());
        settings.setWebsocketUrl(websocketField.getText());

        Main.getObsIntegration().setWebsocket(settings.getWebsocketUrl());
        Main.getObsIntegration().setPassword(settings.getPassword());

        if(settings.isEnableObsIntegration())
            Main.getObsIntegration().connect(connect, disconnect, close, failed);

        settings.savePreferences();

    }

    @FXML
    void onSelectClicks(ActionEvent event) {

    }

    @FXML
    void onSelectKeys(ActionEvent event) {

    }

    @FXML
    void onObsDisconnectAction(ActionEvent e) {
        Main.getObsIntegration().disconnect();
    }

    @FXML
    void onObsConnectAction(ActionEvent e) {
        Main.getObsIntegration().connect(connect, disconnect, close, failed);
    }

}
