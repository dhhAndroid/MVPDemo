package com.dhh.gslibrary;

import java.util.Map;

import rx.Observable;

/**
 * Created by dhh on 2017/6/14.
 */

public class Tetew {
    public void Text() {
        RetrofitService retrofitService = null;
        Observable<Map<String, String>> observable = retrofitService.<Map<String, String>>get("", null);
    }
}
