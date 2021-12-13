package dk.rasmusbendix.apmcounter.apm.keyboard;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import dk.rasmusbendix.apmcounter.apm.ActionEvent;

public class AllKeysListener extends KeyListenerWrapper {

    private ActionEvent event;

    public AllKeysListener(ActionEvent event) {
        this.event = event;
    }

    @Override
    public void onKeyPress(NativeKeyEvent nativeKeyEvent) {
        event.onClick();
    }

}
