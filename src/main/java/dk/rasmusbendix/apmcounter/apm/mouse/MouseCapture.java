package dk.rasmusbendix.apmcounter.apm.mouse;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import lombok.Getter;

import java.util.ArrayList;

public class MouseCapture implements NativeMouseInputListener {

    @Getter private final ArrayList<Integer> buttons;

    public MouseCapture() {
        this.buttons = new ArrayList<>();
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        buttons.add(nativeEvent.getButton());
    }
}
