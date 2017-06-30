package com.dhh.mvp.base.mvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dhh on 2017/3/13.
 */

public abstract class BaseActivity<P extends BasePresenterImpl> extends AppCompatActivity implements BaseView {
    public P presenter;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        if (presenter.mView == null) {
            presenter.bindView(this);

        }
        if (presenter.mContext == null) {
            presenter.setContext(this);
        }

    }

    @Override
    @CallSuper
    protected void onStart() {
        presenter.onStart();
        super.onStart();
    }

    @Override
    @CallSuper
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    @CallSuper
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    protected abstract P initPresenter();


}
