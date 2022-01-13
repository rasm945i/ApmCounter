package dk.rasmusbendix.apmcounter.apm;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import dk.rasmusbendix.apmcounter.Main;
import dk.rasmusbendix.apmcounter.apm.counter.ActionsOverTimeCounter;
import dk.rasmusbendix.apmcounter.apm.counter.Counter;
import dk.rasmusbendix.apmcounter.apm.counter.TotalActionCounter;
import dk.rasmusbendix.apmcounter.apm.keyboard.AllKeysListener;
import dk.rasmusbendix.apmcounter.apm.keyboard.SpecificKeysListener;
import dk.rasmusbendix.apmcounter.apm.mouse.AllClicksListener;
import dk.rasmusbendix.apmcounter.apm.mouse.SpecificClicksListener;
import dk.rasmusbendix.apmcounter.csv.CsvSaver;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EventWrapper {

    @Getter @Setter private transient ActionEvent actionEvent;

    @Getter @Setter private String id;

    @Getter @Setter private Set<Integer> keys;
    @Getter @Setter private Set<Integer> buttons;

    @Getter @Setter private boolean useKeyboard = false;
    @Getter @Setter private boolean useMouse = false;

    @Getter @Setter private boolean allKeys = true;
    @Getter @Setter private boolean allClicks = true;

    @Getter @Setter private boolean updateOnChange = true;
    @Getter @Setter private long updateIntervalMs = 250;


    @Getter @Setter private boolean showAllActions = true;
    @Getter @Setter private long showActionsLastMs = 1000;

    @Getter @Setter private boolean saveToCsv;
    @Getter @Setter private long csvSaveInterval;
    @Getter @Setter private boolean usingObsIntegration;
    @Getter @Setter private boolean sourceExists;

    private transient NativeMouseListener mouseListener;
    private transient NativeKeyListener keyListener;

    @Getter @Setter transient Counter counter;

    private transient ScheduledExecutorService updaterService;
    private transient ScheduledFuture<?> updaterFuture;

    private transient String csvFileName;
    private transient ScheduledExecutorService csvService;
    private transient ScheduledFuture<?> csvFuture;

    public EventWrapper(String id) {
        this.id = id;
        this.keys = Collections.emptySet();
        this.buttons = Collections.emptySet();
        csvFileName = "";
    }


    public void construct() {

        csvFileName = CsvSaver.getFileName(this);

        counter = showAllActions ?
                new TotalActionCounter() :
                new ActionsOverTimeCounter(showActionsLastMs);

        this.actionEvent = () -> {
            counter.onIncrease();

            if(updateOnChange && usingObsIntegration)
                Main.getObsIntegration().updateSource(id, counter.getValue() + "");
        };

        if(!updateOnChange) {

            System.out.println("Updates every " + updateIntervalMs + "ms");
            updaterService = Executors.newScheduledThreadPool(1);
            updaterFuture = updaterService.scheduleAtFixedRate(() -> {
                Main.getObsIntegration().updateSource(id, counter.getValue() + "");
            }, updateIntervalMs, updateIntervalMs, TimeUnit.MILLISECONDS);

        }

        if(useKeyboard) {
            keyListener = allKeys ? new AllKeysListener(actionEvent) : new SpecificKeysListener(actionEvent, keys);
            GlobalScreen.addNativeKeyListener(keyListener);
        }

        if(useMouse) {
            mouseListener = allClicks ? new AllClicksListener(actionEvent) : new SpecificClicksListener(actionEvent, buttons);
            GlobalScreen.addNativeMouseListener(mouseListener);
        }

        if(saveToCsv && csvSaveInterval > 0) {

            csvService = Executors.newScheduledThreadPool(1);
            csvFuture = csvService.scheduleAtFixedRate(() -> CsvSaver.saveToCsv(this), csvSaveInterval, csvSaveInterval, TimeUnit.SECONDS);

        }


    }

    public String getCsvFileName() {
        if(csvFileName.isEmpty())
            csvFileName = CsvSaver.getFileName(this);
        return csvFileName;
    }

    public void deconstruct() {
        if(mouseListener != null) {
            GlobalScreen.removeNativeMouseListener(mouseListener);
        }
        if(keyListener != null) {
            GlobalScreen.removeNativeKeyListener(keyListener);
        }

        if(updaterService != null) {
            updaterService.shutdown();
        }
        if(updaterFuture != null) {
            updaterFuture.cancel(false);
        }

        if(csvService != null) {
            csvService.shutdown();
        }
        if(csvFuture != null) {
            csvFuture.cancel(true);
        }

        // If save to csv is enabled, this is where we save to CSV!
        if(saveToCsv) {
            CsvSaver.saveToCsv(this);
        }

    }

    @Override
    public String toString() {
        return id;
    }

}
