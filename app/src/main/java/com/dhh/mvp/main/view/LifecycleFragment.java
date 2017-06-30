package com.dhh.mvp.main.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dhh.mvp.base.mvp.LifecycleManager;

/**
 * Created by dhh on 2017/6/19.
 */

public class LifecycleFragment extends Fragment {
    private LifecycleManager  mLifecycleManager;

    public LifecycleManager getLifecycleManager() {
        return mLifecycleManager;
    }

    public void setLifecycleManager(LifecycleManager lifecycleManager) {
        mLifecycleManager = lifecycleManager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifecycleFragment", "onCreate:");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LifecycleFragment", "onStart:");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LifecycleFragment", "onResume:");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LifecycleFragment", "onPause:");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("LifecycleFragment", "onStop:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LifecycleFragment", "onDestroy:");
    }
}
