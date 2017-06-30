package com.dhh.mvp.base.mvp;

/**
 * Created by dhh on 2017/6/29.
 */

public class SubjectExecute<T> implements DhhScheduler.Execute<T>, DhhScheduler.OnExecute<T> {
    private DhhScheduler.Execute<T> execute;

    @Override
    public void onNext(T t) {
        if (execute != null) {
            execute.onNext(t);
        }
    }

    @Override
    public void onCompleted() {
        if (execute != null) {
            execute.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (execute != null) {
            execute.onError(e);
        }
    }

    @Override
    public void call(final DhhScheduler.Execute<T> execute) {
        this.execute = execute;
    }
}
