package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class OnPulseLongClickObserver<V extends View>{
    private V view;
    private RxOnPulseLongClickListener<V> pulseLongClickListener;


    public OnPulseLongClickObserver(V view){
        this.view = view;
        pulseLongClickListener = new RxOnPulseLongClickListener<V>(view);
    }

    public OnPulseLongClickObserver<V> triggerTime(long time){
        if(time > 0){
            pulseLongClickListener.setTriggerTime(time);
        }
        return this;
    }

    public OnPulseLongClickObserver<V> triggerTime(long time, TimeUnit unit){
        if(time > 0){
            pulseLongClickListener.setTriggerTime(unit.toMillis(time));
        }
        return this;
    }

    public OnPulseLongClickObserver<V> pulseTime(long time, TimeUnit unit){
        if(time > 0){
            pulseLongClickListener.setPulseTime(unit.toMillis(time));
        }
        return this;
    }

    public OnPulseLongClickObserver<V> pulseTime(long time){
        if(time > 0){
            pulseLongClickListener.setPulseTime(time);
        }
        return this;
    }

    public void subscribe(OnConsumers<V, Integer> consumers){
        pulseLongClickListener.setOnConsumers(consumers);
        view.setOnTouchListener(pulseLongClickListener);
    }

    public void subscribe(OnFunction<V, Integer> function){
        pulseLongClickListener.setOnFunction(function);
        view.setOnTouchListener(pulseLongClickListener);
    }
}
