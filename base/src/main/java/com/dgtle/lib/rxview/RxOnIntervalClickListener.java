package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * 统计指定时间内点击的多次事件
 *
 * @param <V>
 */
public class RxOnIntervalClickListener<V extends View> implements View.OnClickListener, Runnable {
    private V view;
    private long intervalTime = 1000;//限定时间内多少次点击
    private OnFunction<V, Integer> onFunction;

    private int count;
    private boolean isRuning = false;

    public RxOnIntervalClickListener(V view){
        this.view = view;
        reset();
    }

    public RxOnIntervalClickListener(V view,long intervalTime){
        this.view = view;
        this.intervalTime = intervalTime;
        reset();
    }

    public RxOnIntervalClickListener(V view, long intervalTime, TimeUnit unit){
        this.view = view;
        this.intervalTime = unit.toMillis(intervalTime);
        reset();
    }

    public void setIntervalTime(long intervalTime){
        this.intervalTime = intervalTime;
    }

    public void setOnFunction(OnFunction<V, Integer> onFunction){
        this.onFunction = onFunction;
    }

    private void reset(){
        count = 0;
        isRuning = false;
    }

    @Override
    public void onClick(View v){
        count++;
        if(! isRuning){
            isRuning = true;
            v.postDelayed(this,intervalTime);
        }
        if(onFunction != null){
            onFunction.function(view,count);
        }
    }

    @Override
    public void run(){
        reset();
    }
}
