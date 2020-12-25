package com.app.lib.rxview;

public interface OnConsumers<V,T>{
    void accept(V v,T t);

    void complete(V v);
}
