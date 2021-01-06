package com.dgtle.lib.rxandroid;


import io.reactivex.ObservableOnSubscribe;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 20/9/18
 * @desc ...
 */
public abstract class DataObservableOnSubscribe<T,D> implements ObservableOnSubscribe<T>{
    private D data;

    public DataObservableOnSubscribe(D data){
        this.data = data;
    }

    public D getData(){
        return data;
    }
}
