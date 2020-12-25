package com.app.lib.rxview;

import android.view.View;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 20/9/18
 * @desc View的事件分发
 */
public class RxView<T extends View>{
    /**
     * 防抖动点击事件,在指定的时间内,只接收点击最后一次的事件
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T extends View> BounceClickObserver<T> bounceClick(T view){
        return new BounceClickObserver<T>(view);
    }

    /**
     * 防止多次点击,只接收第一次的事件
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T extends View> DebounceClickObserver<T> debounceClick(T view){
        return new DebounceClickObserver<T>(view);
    }


    /**
     * 指定时间内的最后一次点击事件
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T extends View> DestineClickObserver<T> destineClick(T view){
        return new DestineClickObserver<T>(view);
    }

    /**
     * 统计指定时间内点击的多次事件
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T extends View> IntervalClickObserver<T> intervalClick(T view){
        return new IntervalClickObserver<T>(view);
    }

    /**
     * 长按事件
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T extends View> OnLongClickObserver<T> longClick(T view){
        return new OnLongClickObserver<T>(view);
    }

    /**
     * 监听双击事件或单击事件
     */
    public static DoubleClickObserver doubleClick(View view){
        return new DoubleClickObserver(view);
    }

    /**
     * 脉冲式长按点击事件监听,每隔n秒发送一次脉冲波
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T extends View> OnPulseLongClickObserver<T> pulseLongClick(T view){
        return new OnPulseLongClickObserver<T>(view);
    }

}
