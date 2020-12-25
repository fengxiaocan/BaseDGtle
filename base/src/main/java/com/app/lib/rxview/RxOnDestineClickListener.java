package com.app.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * 指定时间内的多次点击事件
 *
 * @param <V>
 */
public class RxOnDestineClickListener<V extends View> implements View.OnClickListener, Runnable {
    private V view;
    private long destineTime = 1000;//限定时间内多少次点击
    private OnFunction<V, Integer> onFunction;

    private int count;

    public RxOnDestineClickListener(V view){
        this.view = view;
    }

    public RxOnDestineClickListener(V view,long destineTime){
        this.view = view;
        this.destineTime = destineTime;
    }

    public RxOnDestineClickListener(V view, long destineTime, TimeUnit unit){
        this.view = view;
        this.destineTime = unit.toMillis(destineTime);
    }

    public void setDestineTime(long destineTime){
        this.destineTime = destineTime;
    }

    public void setOnFunction(OnFunction<V, Integer> onFunction){
        this.onFunction = onFunction;
    }

    private void reset(){
        count = 0;
    }

    @Override
    public void onClick(View v){
        count++;
        v.removeCallbacks(this);
        v.postDelayed(this,destineTime);
        if(onFunction != null){
            onFunction.function(view,count);
        }
    }

    @Override
    public void run(){
        reset();
    }
}
