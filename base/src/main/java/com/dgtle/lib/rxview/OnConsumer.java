package com.dgtle.lib.rxview;

public interface OnConsumer<V>{
    void accept(V v);

    void complete(V v);
}
