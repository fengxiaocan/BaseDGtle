package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class DebounceClickObserver<V extends View>{
    private V view;
    private RxOnDebounceClickListener<V> clickListener;

    public DebounceClickObserver(V view){
        this.view = view;
        clickListener = new RxOnDebounceClickListener<V>(view);
    }

    public DebounceClickObserver<V> debounce(long time){
        if(time > 0)
            clickListener.setDelayTime(time);
        return this;
    }

    public DebounceClickObserver<V> debounce(long time, TimeUnit unit){
        if(time > 0)
            clickListener.setDelayTime(unit.toMillis(time));
        return this;
    }

    public void subscribe(OnAction<V> function){
        if(view != null){
            clickListener.setOnAction(function);
            view.setOnClickListener(clickListener);
        }
    }
}
