package dk.rasmusbendix.apmcounter.apm.keyboard;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import dk.rasmusbendix.apmcounter.apm.ActionEvent;

import java.util.ArrayList;

public class SpecificKeysListener extends KeyListenerWrapper {

    private ArrayList<Integer> keycodes;
    private ActionEvent event;

    public SpecificKeysListener(ActionEvent event, ArrayList<Integer> codes) {
        this.keycodes = codes;
        this.event = event;
    }

    @Override
    public void onKeyPress(NativeKeyEvent nativeKeyEvent) {
        if(keycodes.contains(nativeKeyEvent.getKeyCode()))
            event.onClick();
    }

}
