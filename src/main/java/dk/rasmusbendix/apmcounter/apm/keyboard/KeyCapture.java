package dk.rasmusbendix.apmcounter.apm.keyboard;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import lombok.Getter;

import java.util.ArrayList;

public class KeyCapture extends KeyListenerWrapper {

    @Getter private final ArrayList<Integer> pressedKeys;

    public KeyCapture() {
        this.pressedKeys = new ArrayList<>();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
    }

    @Override
    public void onKeyPress(NativeKeyEvent event) {
        pressedKeys.add(event.getKeyCode());
    }
}
