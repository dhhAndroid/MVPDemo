package com.dhh.mvp.main;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhh on 2017/5/19.
 */
public class AcitvityLruCache {
    private static AcitvityLruCache instance;
    private List<WeakReference<Activity>> mList;
    private int maxSize;

    private AcitvityLruCache() {
        mList = new ArrayList<>();
        setMaxSize(3);
    }

    public static AcitvityLruCache getInstance() {
        if (instance == null) {
            synchronized (AcitvityLruCache.class) {
                if (instance == null) {
                    instance = new AcitvityLruCache();
                }
            }
        }
        return instance;
    }

    public void add(Activity activity) {
        mList.add(0, new WeakReference<>(activity));
        trimTosize();
    }

    private void trimTosize() {
        while (mList.size() > maxSize) {
            Activity activity = mList.remove(mList.size() - 1).get();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        trimTosize();
    }
}
