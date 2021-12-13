package dk.rasmusbendix.apmcounter.apm.counter;

import lombok.Getter;

import java.util.ArrayList;

public abstract class Counter {

    @Getter protected ArrayList<Long> actions;

    public Counter() {
        this.actions = new ArrayList<>();
    }

    public abstract void onIncrease();
    public abstract long getValue();

}
