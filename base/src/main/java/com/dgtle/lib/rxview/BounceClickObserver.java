package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class BounceClickObserver<V extends View>{
    private V view;
    private RxOnBounceClickListener<V> clickListener;

    public BounceClickObserver(V view){
        this.view = view;
        clickListener = new RxOnBounceClickListener<V>(view);
    }

    public BounceClickObserver<V> bounce(long time){
        if(time > 0){
            clickListener.setIntervalTime(time);
        }
        return this;
    }

    public BounceClickObserver<V> bounce(long time, TimeUnit unit){
        if(time > 0){
            clickListener.setIntervalTime(unit.toMillis(time));
        }
        return this;
    }

    public void subscribe(OnAction<V> function){
        if(view != null){
            clickListener.setOnAction(function);
            view.setOnClickListener(clickListener);
        }
    }
}
