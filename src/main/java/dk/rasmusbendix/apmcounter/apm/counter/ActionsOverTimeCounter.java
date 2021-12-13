package dk.rasmusbendix.apmcounter.apm.counter;

import lombok.Getter;
import lombok.Setter;

public class ActionsOverTimeCounter extends Counter {

    @Getter @Setter
    private long duration;

    public ActionsOverTimeCounter(long duration) {
        this.duration = duration;
    }

    @Override
    public void onIncrease() {
        actions.add(System.currentTimeMillis());
    }

    @Override
    public long getValue() {
//        cleanActionsOverTimeMap();
//        return actions.size();
        if(actions.isEmpty()) {
            return 0;
        }

        int val = 0;
        long timestamp = System.currentTimeMillis();
        for (int i = actions.size()-1; i >= 0; i--) {
            if(actions.get(i) + duration < timestamp)
                break;
            val++;
        }

        return val;

    }

//    public void cleanActionsOverTimeMap() {
//        long timestamp = System.currentTimeMillis();
//        while(actions.getFirst() + duration < timestamp) {
//            actions.removeFirst();
//        }
//    }

}
