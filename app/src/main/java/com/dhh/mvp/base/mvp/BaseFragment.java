package com.dhh.mvp.base.mvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by dhh on 2017/3/14.
 */

public abstract class BaseFragment<P extends BasePresenterImpl> extends Fragment implements BaseView {
    public P presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = initPresenter();
        presenter.bindView(this);
        presenter.setContext(getContext());

    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    protected abstract P initPresenter();

}
