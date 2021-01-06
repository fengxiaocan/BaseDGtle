package com.dgtle.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 防抖动点击事件,在指定的时间内,只接收点击了n次之后最后一次的事件
 *
 * @param <V>
 */
public class RxOnBounceClickListener<V extends View> implements View.OnClickListener{
    private V view;
    private long intervalTime = 500;//限定时间内多少次点击
    private OnAction<V> onAction;
    private ObservableEmitter<V> observableEmitter;

    public RxOnBounceClickListener(V view){
        this.view = view;
    }

    public void setOnAction(OnAction<V> onAction){
        this.onAction = onAction;
        init();
    }

    public void setIntervalTime(long intervalTime){
        this.intervalTime = intervalTime;
    }

    private void init(){
        Observable.create(new ObservableOnSubscribe<V>(){
            @Override
            public void subscribe(ObservableEmitter<V> emitter) throws Exception {
                observableEmitter = emitter;
            }
        }).debounce(intervalTime, TimeUnit.MILLISECONDS).subscribe(new Observer<V>(){
            @Override
            public void onSubscribe(Disposable d){
            }

            @Override
            public void onNext(V integer){
                if(onAction != null){
                    onAction.action(view);
                }
            }

            @Override
            public void onError(Throwable e){
            }

            @Override
            public void onComplete(){
            }
        });
    }

    @Override
    public void onClick(View v){
        observableEmitter.onNext(view);
    }
}
