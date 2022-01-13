package dk.rasmusbendix.apmcounter.apm.keyboard;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import dk.rasmusbendix.apmcounter.apm.ActionEvent;

import java.util.Set;

public class SpecificKeysListener extends KeyListenerWrapper {

    private final Set<Integer> keycodes;
    private final ActionEvent event;

    public SpecificKeysListener(ActionEvent event, Set<Integer> codes) {
        this.keycodes = codes;
        this.event = event;
    }

    @Override
    public void onKeyPress(NativeKeyEvent nativeKeyEvent) {
        if(keycodes.contains(nativeKeyEvent.getKeyCode()))
            event.onClick();
    }

}
