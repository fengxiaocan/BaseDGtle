package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class OnLongClickObserver<V extends View>{
    private V view;
    private RxOnLongClickListener<V> rxOnLongClickListener;


    public OnLongClickObserver(V view){
        this.view = view;
        rxOnLongClickListener = new RxOnLongClickListener<V>(view);
    }

    public OnLongClickObserver<V> triggerTime(long time){
        if(time > 0){
            rxOnLongClickListener.setTriggerTime(time);
        }
        return this;
    }

    public OnLongClickObserver<V> triggerTime(long time, TimeUnit unit){
        if(time > 0){
            rxOnLongClickListener.setTriggerTime(unit.toMillis(time));
        }
        return this;
    }

    public void subscribe(OnConsumer<V> function){
        rxOnLongClickListener.setOnConsumer(function);
        view.setOnTouchListener(rxOnLongClickListener);
    }

    public void subscribe(OnAction<V> function){
        if(view != null){
            rxOnLongClickListener.setOnAction(function);
            view.setOnTouchListener(rxOnLongClickListener);
        }
    }
}
