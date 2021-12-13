package dk.rasmusbendix.apmcounter.apm.mouse;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import dk.rasmusbendix.apmcounter.apm.ActionEvent;

public class AllClicksListener implements NativeMouseInputListener {

    private ActionEvent event;

    public AllClicksListener(ActionEvent event) {
        this.event = event;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        event.onClick();
    }
}
