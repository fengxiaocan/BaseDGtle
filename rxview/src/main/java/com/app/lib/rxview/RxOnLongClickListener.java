package com.app.lib.rxview;

import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * 长按触摸事件,能够监听是否取消了长按的事件,可以指定长按触摸的时间
 *
 * @param <V>
 */
public class RxOnLongClickListener<V extends View> implements View.OnTouchListener, Runnable {
    private V view;
    private long longClickTime = 800;//长按触摸时间长度
    private OnConsumer<V> onConsumer;
    private OnAction<V> onAction;

    private boolean isLongClicking;

    public RxOnLongClickListener(V view){
        this.view = view;
    }

    public RxOnLongClickListener(V view,long touchTime){
        this.view = view;
        this.longClickTime = touchTime;
    }

    public RxOnLongClickListener(V view, long touchTime, TimeUnit unit){
        this.view = view;
        this.longClickTime = unit.toMillis(touchTime);
    }

    public void setTriggerTime(long longClickTime){
        this.longClickTime = longClickTime;
    }

    public void setOnConsumer(OnConsumer<V> onConsumer){
        this.onConsumer = onConsumer;
    }

    public void setOnAction(OnAction<V> onAction){
        this.onAction = onAction;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isLongClicking = false;
                view.postDelayed(this,longClickTime);
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                checkCancelAction();
                return false;
        }
        return false;
    }

    private void checkCancelAction(){
        if(isLongClicking){
            isLongClicking = false;
            if(onConsumer != null){
                onConsumer.complete(view);
            }
        }
        view.removeCallbacks(this);
        isLongClicking = false;
    }

    @Override
    public void run(){
        isLongClicking = true;
        if(onConsumer != null){
            onConsumer.accept(view);
        }
        if(onAction != null){
            onAction.action(view);
        }
    }
}
