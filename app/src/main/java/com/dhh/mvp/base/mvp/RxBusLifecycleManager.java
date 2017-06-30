package com.dhh.mvp.base.mvp;

import android.app.Activity;
import android.app.FragmentManager;

import com.dhh.mvp.main.view.LifecycleFragment;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by dhh on 2017/6/28.
 */

public class RxBusLifecycleManager {
    private static RxBusLifecycleManager instance;
    public static final String RXBUS_TAG = "RxBus_tag";
    private Map<FragmentManager, LifecycleFragment> lifecycleFragments;

    private RxBusLifecycleManager() {
        lifecycleFragments = new HashMap<>();
    }

    public static RxBusLifecycleManager getInstance() {
        if (instance == null) {
            synchronized (RxBusLifecycleManager.class) {
                if (instance == null) {
                    instance = new RxBusLifecycleManager();
                }
            }
        }
        return instance;
    }

    public LifecycleManager get(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        LifecycleManager lifecycleManager = getLifecycleManager(activity, fm);

        return lifecycleManager;
    }

    private LifecycleManager getLifecycleManager(Activity activity, FragmentManager fm) {
        LifecycleFragment fragment = (LifecycleFragment) fm.findFragmentByTag(RXBUS_TAG);
        if (fragment == null) {
            fragment = lifecycleFragments.get(fm);
            if (fragment == null) {
                fragment = new LifecycleFragment();
                lifecycleFragments.put(fm, fragment);
                fm.beginTransaction().add(fragment, RXBUS_TAG).commitAllowingStateLoss();
            }
        }
        LifecycleManager lifecycleManager = fragment.getLifecycleManager();
        if (lifecycleManager == null) {
            lifecycleManager = new LifecycleManager();
            fragment.setLifecycleManager(lifecycleManager);
        }
        return lifecycleManager;
    }

}
