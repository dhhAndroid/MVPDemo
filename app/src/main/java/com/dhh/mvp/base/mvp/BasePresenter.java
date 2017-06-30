package com.dhh.mvp.base.mvp;

import android.content.Intent;
import android.support.annotation.StringRes;

import rx.Observable;

/**
 * Created by dhh on 2017/3/13.
 */

public interface BasePresenter {
    void setData();

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void finishActivity();

    void startActivity(Intent intent);

    String getString(@StringRes int strid);

    <T> Observable<T> bindLife(Observable<T> observable);

}
