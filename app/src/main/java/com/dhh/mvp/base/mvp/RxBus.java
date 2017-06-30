package com.dhh.mvp.base.mvp;

import android.app.Activity;
import android.os.Message;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by dhh on 2017/5/13.
 */
public class RxBus {
    private static RxBus instance;
    private SerializedSubject<Object, Object> bus;
    private Map<Integer, Object> stickEventMap;

    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
        stickEventMap = Collections.synchronizedMap(new ConcurrentHashMap<Integer, Object>());
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public void post(int code, Object event) {
        Message message = Message.obtain();
        message.what = code;
        message.obj = event;
        bus.onNext(message);
    }

    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
        return bus.ofType(Message.class)
                .filter(new Func1<Message, Boolean>() {
                    @Override
                    public Boolean call(Message message) {
                        return message.what == code && eventType.isInstance(message.obj);
                    }
                })
                .map(new Func1<Message, Object>() {
                    @Override
                    public Object call(Message message) {
                        return message.obj;
                    }
                })
                .cast(eventType);
    }

    public <T> Observable<T> toObservable(final int code) {
        return bus.ofType(Message.class)
                .filter(new Func1<Message, Boolean>() {
                    @Override
                    public Boolean call(Message message) {
                        return message.what == code;
                    }
                })
                .map(new Func1<Message, T>() {
                    @Override
                    public T call(Message message) {
                        return (T) message.obj;
                    }
                });
    }

    public void postStick(int code, Object event) {
        stickEventMap.put(code, event);
        post(code, event);
    }

    public <T> Observable<T> toObservableStick(int code, Class<T> eventType) {
        Object event = stickEventMap.get(code);
        if (eventType.isInstance(event)) {
            return toObservable(code, eventType).startWith((T) event);
        }
        return toObservable(code, eventType);
    }

    public <T> Observable<T> toObservableStick(int code) {
        Object event = stickEventMap.get(code);
        if (event != null) {
            return this.<T>toObservable(code).startWith((T) event);

        } else {
            return toObservable(code);
        }
    }

    public <T> Observable<T> toObservableStickOneTime(int code, Class<T> eventType) {
        Object event = stickEventMap.get(code);
        if (eventType.isInstance(event)) {
            stickEventMap.remove(code);
            return toObservable(code, eventType).startWith((T) event);
        }
        return toObservable(code, eventType);
    }

    public void removeStickEvent(int code) {
        stickEventMap.remove(code);
    }

    public void removeAllStickEvents() {
        stickEventMap.clear();
    }

    public void reset() {
        stickEventMap.clear();
        bus = null;
    }

    public void unsubscribed(Subscription sr) {
        if (sr != null && !sr.isUnsubscribed()) {
            sr.unsubscribe();
        }
    }
}

