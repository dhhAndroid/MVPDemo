package com.dhh.mvp.base.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;


/**
 * Created by dhh on 2017/3/13.
 */

public abstract class BasePresenterImpl<V extends BaseView> implements BasePresenter, LifecycleProvider<ActivityEvent> {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    protected String TAG;
    protected V mView;
    protected Context mContext;
    protected boolean isDestroy;

    public BasePresenterImpl() {
        TAG = getClass().getSimpleName();
    }

    public BasePresenterImpl(V view) {
        bindView(view);

    }

    public final void bindView(V view) {
        mView = view;
        if (view instanceof Activity) {
            setContext((Context) view);

        } else if (view instanceof Fragment) {
            setContext(((Fragment) view).getContext());
        }
    }

    void setContext(Context context) {
        mContext = context;
        onCreate();
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindOnDestroy() {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, ActivityEvent.DESTROY);
    }

    protected abstract void initPresenterData();

    @Override
    @CallSuper
    public void onCreate() {
        Log.d(TAG, "onCreate");
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        initPresenterData();
    }


    @Override
    @CallSuper
    public void onStart() {
        Log.d(TAG, "onStart");
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        Log.d(TAG, "onResume");
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        Log.d(TAG, "onPause");
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    @CallSuper
    public void onStop() {
        Log.d(TAG, "onStop");
        lifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    @CallSuper
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        mView = null;
        mContext = null;
        isDestroy = true;
    }

    @Override
    public void finishActivity() {
        if (mView instanceof Activity) {
            ((Activity) mView).finish();

        } else if (mView instanceof Fragment) {
            ((Fragment) mView).getActivity().finish();

        }
    }

    @Override
    public void startActivity(Intent intent) {
        mContext.startActivity(intent);
    }

    @Override
    public String getString(@StringRes int strid) {
        return mContext.getString(strid);
    }

    @Override
    public final <T> Observable<T> bindLife(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<T>bindOnDestroy());
    }
}
