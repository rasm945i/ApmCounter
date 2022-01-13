package dk.rasmusbendix.apmcounter.apm.mouse;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import dk.rasmusbendix.apmcounter.apm.ActionEvent;

import java.util.Set;

public class SpecificClicksListener implements NativeMouseInputListener {

    private final ActionEvent event;
    private final Set<Integer> buttons;

    public SpecificClicksListener(ActionEvent event, Set<Integer> buttons) {
        this.buttons = buttons;
        this.event = event;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        if(buttons.contains(nativeEvent.getButton()))
            event.onClick();
    }
}
