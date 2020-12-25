package com.app.lib.rxandroid;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 20/9/18
 * @desc ...
 */
public abstract class DataObserver<T,D> implements Observer<T>{
    private D data;

    public DataObserver(D data){
        this.data = data;
    }

    public D getData(){
        return data;
    }

    @Override
    public void onSubscribe(Disposable d){

    }

    @Override
    public void onError(Throwable e){

    }

    @Override
    public void onComplete(){

    }
}
