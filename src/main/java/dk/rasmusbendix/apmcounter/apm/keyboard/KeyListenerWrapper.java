package dk.rasmusbendix.apmcounter.apm.keyboard;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.HashSet;

public abstract class KeyListenerWrapper implements NativeKeyListener {

    private final HashSet<Integer> keysDown;

    public KeyListenerWrapper() {
        this.keysDown = new HashSet<>();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        if(keysDown.contains(nativeEvent.getKeyCode()))
            return;
        keysDown.add(nativeEvent.getKeyCode());
        onKeyPress(nativeEvent);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        keysDown.remove(nativeEvent.getKeyCode());
    }

    // Doesn't fire multiple times when a key is pressed and held down! Important!
    public abstract void onKeyPress(NativeKeyEvent nativeKeyEvent);

}
