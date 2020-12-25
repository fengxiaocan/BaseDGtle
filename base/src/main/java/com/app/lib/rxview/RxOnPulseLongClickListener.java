package com.app.lib.rxview;

import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * 长按触摸事件,能够监听是否取消了长按的事件,可以指定长按触摸的时间
 *
 * @param <V>
 */
public class RxOnPulseLongClickListener<V extends View> implements View.OnTouchListener, Runnable {
    private V view;
    private long longClickTime = 800;//长按触摸时间长度
    private long pulseTime = 100;//脉冲时间
    private OnConsumers<V, Integer> onConsumers;
    private OnFunction<V, Integer> onFunction;

    private boolean isLongClicking;
    private boolean isTouchDown;//是否触摸按下
    private int count = 0;

    public RxOnPulseLongClickListener(V view){
        this.view = view;
    }

    public RxOnPulseLongClickListener(V view,long touchTime){
        this.view = view;
        this.longClickTime = touchTime;
    }

    public RxOnPulseLongClickListener(V view, long touchTime, TimeUnit unit){
        this.view = view;
        this.longClickTime = unit.toMillis(touchTime);
    }

    public void setTriggerTime(long longClickTime){
        this.longClickTime = longClickTime;
    }

    public void setPulseTime(long pulseTime){
        this.pulseTime = pulseTime;
    }

    public void setOnConsumers(OnConsumers<V, Integer> onConsumers){
        this.onConsumers = onConsumers;
    }

    public void setOnFunction(OnFunction<V, Integer> onFunction){
        this.onFunction = onFunction;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isLongClicking = false;
                isTouchDown = true;
                count = 0;
                view.postDelayed(this,longClickTime);
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                isTouchDown = false;
                checkCancelAction();
                return false;
        }
        return false;
    }

    private void checkCancelAction(){
        if(isLongClicking){
            isLongClicking = false;
            if(onConsumers != null){
                onConsumers.complete(view);
            }
        }
        view.removeCallbacks(this);
        count = 0;
        isLongClicking = false;
    }

    @Override
    public void run(){
        if(isTouchDown){
            count++;
            isLongClicking = true;
            if(onConsumers != null){
                onConsumers.accept(view,count);
            }
            if(onFunction != null){
                onFunction.function(view,count);
            }
            view.postDelayed(this,pulseTime);
        }
    }
}
