package com.app.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class IntervalClickObserver<V extends View>{
    private V view;
    private RxOnIntervalClickListener<V> clickListener;

    public IntervalClickObserver(V view){
        this.view = view;
        clickListener = new RxOnIntervalClickListener<V>(view);
    }

    public IntervalClickObserver<V> delay(long time){
        if(time > 0){
            clickListener.setIntervalTime(time);
        }
        return this;
    }

    public IntervalClickObserver<V> delay(long time, TimeUnit unit){
        if(time > 0){
            clickListener.setIntervalTime(unit.toMillis(time));
        }
        return this;
    }

    public void subscribe(OnFunction<V, Integer> function){
        if(view != null){
            clickListener.setOnFunction(function);
            view.setOnClickListener(clickListener);
        }
    }
}
