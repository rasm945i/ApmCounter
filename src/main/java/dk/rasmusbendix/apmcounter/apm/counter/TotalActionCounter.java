package dk.rasmusbendix.apmcounter.apm.counter;

import lombok.Getter;

public class TotalActionCounter extends Counter {

    @Getter private long actionsPerformed;

    public TotalActionCounter() {}

    @Override
    public void onIncrease() {
        actions.add(System.currentTimeMillis());
        actionsPerformed++;
    }

    @Override
    public long getValue() {
        return actionsPerformed;
    }
}
