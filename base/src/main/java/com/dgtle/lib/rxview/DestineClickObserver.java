package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class DestineClickObserver<V extends View>{
    private V view;
    private RxOnDestineClickListener<V> clickListener;

    public DestineClickObserver(V view){
        this.view = view;
        clickListener = new RxOnDestineClickListener<V>(view);
    }

    public DestineClickObserver<V> delay(long time){
        if(time > 0)
            clickListener.setDestineTime(time);
        return this;
    }

    public DestineClickObserver<V> delay(long time, TimeUnit unit){
        if(time > 0)
            clickListener.setDestineTime(unit.toMillis(time));
        return this;
    }

    public void subscribe(OnFunction<V, Integer> function){
        if(view != null){
            clickListener.setOnFunction(function);
            view.setOnClickListener(clickListener);
        }
    }
}
