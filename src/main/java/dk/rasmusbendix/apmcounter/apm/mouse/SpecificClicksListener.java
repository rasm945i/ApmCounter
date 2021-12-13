package dk.rasmusbendix.apmcounter.apm.mouse;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import dk.rasmusbendix.apmcounter.apm.ActionEvent;

import java.util.ArrayList;

public class SpecificClicksListener implements NativeMouseInputListener {

    private ActionEvent event;
    private ArrayList<Integer> buttons;

    public SpecificClicksListener(ActionEvent event, ArrayList<Integer> buttons) {
        this.buttons = buttons;
        this.event = event;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        if(buttons.contains(nativeEvent.getButton()))
            event.onClick();
    }
}
