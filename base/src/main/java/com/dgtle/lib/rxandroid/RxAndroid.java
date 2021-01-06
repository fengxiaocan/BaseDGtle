package com.dgtle.lib.rxandroid;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

/**
 * @author noah
 * @email fengxiaocan@gmail.com
 * @create 20/9/18
 * @desc rxandroid 封装一下,方便以后统一修改因为版本更新的问题而导致的大换血,第三方框架老这么干的
 */
public class RxAndroid<T>{
    Observable<T> observable;

    protected RxAndroid(Observable<T> obser){
        this.observable = obser;
    }

    public static <T> RxAndroid<T> create(ObservableOnSubscribe<T> source){
        return new RxAndroid<>(Observable.create(source));
    }

    /**
     * 创建一个不发射任何数据但是正常终止的Observable
     *
     * @param <T>
     * @return
     */
    public static <T> RxAndroid<T> empty(){
        return new RxAndroid<>(Observable.empty());
    }

    /**
     * 创建一个不发射数据也不终止的Observable；
     *
     * @param <T>
     * @return
     */
    public static <T> RxAndroid<T> never(){
        return new RxAndroid<>(Observable.never());
    }

    /**
     * 这里的initialDelay参数用来指示开始发射第一个整数的之前要停顿的时间，时间的单位与peroid一样，都是通过unit参数来指定的；
     * period参数用来表示每个发射之间停顿多少时间；
     * unit表示时间的单位，是TimeUnit类型的；
     * scheduler参数指定数据发射和等待时所在的线程。
     *
     * @param initialDelay
     * @param period
     * @param unit
     * @return
     */
    public static RxAndroid<Long> interval(long initialDelay, long period, TimeUnit unit)
    {
        return new RxAndroid<>(Observable.interval(initialDelay,period,unit));
    }

    /**
     * 每过period秒的时间发送一个整数，整数从0开始：
     *
     * @param period
     * @param unit
     * @return
     */
    public static RxAndroid<Long> interval(long period, TimeUnit unit){
        return new RxAndroid<>(Observable.interval(period,unit));
    }

    public static RxAndroid<Long> interval(long period){
        return new RxAndroid<>(Observable.interval(period, TimeUnit.MILLISECONDS));
    }

    /**
     * 这里的initialDelay参数用来指示开始发射第一个整数的之前要停顿的时间，时间的单位与peroid一样，都是通过unit参数来指定的；
     * period参数用来表示每个发射之间停顿多少时间；
     * unit表示时间的单位，是TimeUnit类型的；
     * scheduler参数指定数据发射和等待时所在的线程。
     *
     * @param start
     * @param count
     * @param initialDelay
     * @param period
     * @param unit
     * @return
     */
    public static RxAndroid<Long> intervalRange(long start, long count, long initialDelay, long period,
                                                TimeUnit unit)
    {
        return new RxAndroid<>(Observable.intervalRange(start,count,initialDelay,period,unit));
    }

    /**
     * 生成一个从 start开始一共count个的数据
     *
     * @param start
     * @param count
     * @return
     */
    public static RxAndroid<Integer> range(int start, int count)
    {
        return new RxAndroid<>(Observable.range(start,count));
    }

    /**
     * 生成一个从 start开始一共count个的Long数据
     *
     * @param start
     * @param count
     * @return
     */
    public static RxAndroid<Long> rangeLong(int start, int count)
    {
        return new RxAndroid<>(Observable.rangeLong(start,count));
    }


    public static <T> RxAndroid<T> just(T t){
        return new RxAndroid<>(Observable.just(t));
    }

    public static <T> RxAndroid<T> fromArray(T... t){
        return new RxAndroid<>(Observable.fromArray(t));
    }

    public static <T> RxAndroid<T> fromCallable(Callable<? extends T> supplier){
        return new RxAndroid<>(Observable.fromCallable(supplier));
    }

    public static <T> RxAndroid<T> fromFuture(Future<? extends T> future){
        return new RxAndroid<>(Observable.fromFuture(future));
    }

    public static <T> RxAndroid<T> createOnThread(ObservableOnSubscribe<T> source){
        return new RxAndroid<>(Observable.create(source)).subscribeOnThread().observeOnMain();
    }


    /**
     * 创建一在主线程中运行的任务
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> RxAndroid<T> createOnMain(ObservableOnSubscribe<T> source){
        return new RxAndroid<>(Observable.create(source)).subscribeOnMain().observeOnMain();
    }

    /**
     * 限制时间内第一次生效
     *
     * @param windowDuration
     * @param unit
     * @return
     */
    public RxAndroid<T> throttleFirst(long windowDuration, TimeUnit unit){
        observable = observable.throttleFirst(windowDuration,unit);
        return this;
    }

    public RxAndroid<T> throttleFirst(long windowDuration){
        observable = observable.throttleFirst(windowDuration, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 限制时间内最后一次生效的
     *
     * @param windowDuration
     * @param unit
     * @return
     */
    public RxAndroid<T> throttleLast(long windowDuration, TimeUnit unit){
        observable = observable.throttleLast(windowDuration,unit);
        return this;
    }

    public RxAndroid<T> throttleLast(long windowDuration){
        observable = observable.throttleLast(windowDuration, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 无限次发送序列
     *
     * @return
     */
    public RxAndroid<T> repeat(){
        observable = observable.repeat();
        return this;
    }

    /**
     * 指定发射序列次数
     *
     * @param times
     * @return
     */
    public RxAndroid<T> repeat(long times){
        observable = observable.repeat(times);
        return this;
    }

    /**
     * 将指定的序列重复发射指定的次数
     *
     * @return
     */
    public RxAndroid<T> repeatUntil(BooleanSupplier stop){
        observable = observable.repeatUntil(stop);
        return this;
    }

    /**
     * 在新的线程中调度
     *
     * @return
     */
    public RxAndroid<T> subscribeOnThread(){
        observable = observable.subscribeOn(Schedulers.newThread());
        return this;
    }

    /**
     * 指定在哪个线程中调度
     *
     * @param scheduler
     * @return
     */
    public RxAndroid<T> subscribeOn(Scheduler scheduler){
        observable = observable.subscribeOn(scheduler);
        return this;
    }

    /**
     * 指定在哪个线程中接收数据
     *
     * @param scheduler
     * @return
     */
    public RxAndroid<T> observeOn(Scheduler scheduler){
        observable = observable.observeOn(scheduler);
        return this;
    }

    /**
     * 在主线程中接收
     *
     * @return
     */
    public RxAndroid<T> observeOnMain(){
        observable = observable.observeOn(AndroidSchedulers.mainThread());
        return this;
    }

    /**
     * 最多保留的事件数。
     *
     * @param count
     * @return
     */
    public RxAndroid<T> take(long count){
        observable = observable.take(count);
        return this;
    }

    /**
     * 条件过滤，去除不符合某些条件的事件。
     *
     * @param predicate
     * @return
     */
    public RxAndroid<T> filter(Predicate<? super T> predicate){
        observable = observable.filter(predicate);
        return this;
    }

    /**
     * io线程
     *
     * @return
     */
    public RxAndroid<T> io(){
        observable = observable.subscribeOn(Schedulers.io());
        return this;
    }

    public Disposable subscribe(Consumer<? super T> onNext){
        return observable.subscribe(onNext);
    }

    /**
     * Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件. 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。
     *
     * @param other
     * @param zipper
     * @param <U>
     * @param <R>
     * @return
     */
    public <U,R> Observable<R> zipWith(Iterable<U> other, BiFunction<? super T,? super U,? extends R> zipper)
    {
        return observable.zipWith(other,zipper);
    }

    /**
     * 通过Map, 可以将Observable发来的事件转换为任意的类型
     *
     * @param mapper
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Function<? super T,? extends R> mapper){
        return observable.map(mapper);
    }

    public Disposable subscribeWithError(Consumer<? super T> onNext){
        return observable.subscribe(onNext, throwable -> throwable.printStackTrace());
    }

    public Disposable subscribe(){
        return observable.subscribe();
    }

    public Disposable subscribeNoError(){
        return observable.subscribe(Functions.emptyConsumer(), throwable -> throwable.printStackTrace());
    }

    public void subscribe(Observer<? super T> observer){
        observable.subscribe(observer);
    }

    public RxAndroid<T> subscribeOnMain(){
        observable = observable.subscribeOn(AndroidSchedulers.mainThread());
        return this;
    }

    public RxAndroid<T> delay(long delay){
        observable = observable.delay(delay, TimeUnit.MILLISECONDS);
        return this;
    }
}
