package com.dhh.mvp.main;

import android.animation.ValueAnimator;
import android.util.Log;

import com.dhh.mvp.base.mvp.BasePresenterImpl;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by dhh on 2017/3/14.
 */

public class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {

    @Override
    protected void initPresenterData() {
        Log.d(TAG, "initPresenterData: 初始化数据");
        Log.d("MainPresenterImpl", "mContext==null:" + (mContext == null));
        if (mView != null) {
            Log.d("MainPresenterImpl", mView.getClass().getSimpleName());
        }
    }


    @Override
    public void getDate() {
        mView.setText("333333333");
        Observable.interval(1, TimeUnit.SECONDS)
//                .takeUntil(new Func1<Long, Boolean>() {
//                    @Override
//                    public Boolean call(Long aLong) {
//                        return isDestroy;
//                    }
//                })
//                .filter(new Func1<Long, Boolean>() {
//                    @Override
//                    public Boolean call(Long aLong) {
//                        return !isDestroy;
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d("MainActivity", "call: doOnUnsubscribe");
                    }
                })
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (isDestroy) {
                            unsubscribe();
                            return;
                        }
                        Log.d("MainActivity", "aLong:" + aLong);

                    }
                });
    }

    @Override
    public void getrobotData() {
        Observable.interval(2, TimeUnit.SECONDS)
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.i(TAG, "Unsubscribing subscription from onCreate()");
                        Log.d("MainPresenterImpl", "mView==null:" + (mView == null));
                        Log.d("MainPresenterImpl", "mContext==null:" + (mContext == null));
                    }
                })
                .compose(this.<Long>bindOnDestroy())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d("MainPresenterImpl", "aLong:" + aLong);

                    }
                });
        bindLife(Observable.just("err"))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 1000);

                        valueAnimator.setDuration(500);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                Log.d("MainPresenterImpl", "animation.getAnimatedValue():" + animation.getAnimatedValue().toString());
                                Log.d("MainPresenterImpl", "animation.getAnimatedFraction():" + animation.getAnimatedFraction());
                            }
                        });
                        valueAnimator.start();
                    }
                });
        final long l = System.currentTimeMillis();


        bindLife(Observable.interval(0, 2, TimeUnit.MILLISECONDS))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d("MainPresenterImpl", "aLong:" + aLong);
                        if (aLong == 499) {
                            long l1 = System.currentTimeMillis();
                            Log.d("MainPresenterImpl", "l1-l:" + (l1 - l));
                        }
                    }
                });
    }


    @Override
    public void setData() {

    }
}
