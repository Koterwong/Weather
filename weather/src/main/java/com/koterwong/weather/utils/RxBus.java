package com.koterwong.weather.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/09 23:38
 * <p>
 * Description:没时间解释了，快上车
 * =================================================
 */
public class RxBus {

    //thread unSafe as subscriber
    //private final PublishSubject<Object> _bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead

    private static RxBus mInstance = new RxBus();

    public static RxBus getInstance() {
        return mInstance;
    }

    private final Subject<Object, Object> rxBus =
            new SerializedSubject<>(PublishSubject.create());

    public void send(Object object) {
        rxBus.onNext(object);  //在这里向bus发布消息
    }

    public Observable<Object> toObervable() {
        return rxBus;          //拿到rxBus上的Observable，订阅消息
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return rxBus.ofType(eventType);
    }

    public boolean hasObservable() {
        return rxBus.hasObservers();
    }
}
