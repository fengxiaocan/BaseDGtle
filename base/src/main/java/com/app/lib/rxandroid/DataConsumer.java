package com.app.lib.rxandroid;


import io.reactivex.functions.Consumer;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 20/9/18
 * @desc ...
 */
public abstract class DataConsumer<T,D> implements Consumer<T>{
    private D data;

    public DataConsumer(D data){
        this.data = data;
    }

    public D getData(){
        return data;
    }
}
