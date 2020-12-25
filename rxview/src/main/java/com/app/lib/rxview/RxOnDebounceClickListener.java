package com.app.lib.rxview;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.*;
import io.reactivex.android.schedulers.*;
import io.reactivex.disposables.*;

/**
 * 防止多次点击,只接受第一次的事件
 *
 * @param <V>
 */
public class RxOnDebounceClickListener<V extends View> implements View.OnClickListener{
    private ObservableEmitter<V> mEmitter;
    private V view;
    private long delayTime = 1500;
    private OnAction<V> onAction;

    public RxOnDebounceClickListener(V view){
        this.view = view;
    }

    private void init(){
        Observable.create(new ObservableOnSubscribe<V>(){
            @Override
            public void subscribe(ObservableEmitter<V> emitter) throws Exception {
                mEmitter = emitter;
            }
        }).throttleFirst(delayTime, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<V>(){
                    @Override
                    public void onSubscribe(Disposable d){

                    }

                    @Override
                    public void onNext(V v){
                        if(onAction != null){
                            onAction.action(v);
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

    public void setDelayTime(long delayTime){
        this.delayTime = delayTime;
    }

    public void setOnAction(OnAction<V> onAction){
        this.onAction = onAction;
        init();
    }

    @Override
    public synchronized void onClick(View v){
        mEmitter.onNext(view);
    }

}
