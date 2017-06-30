package com.dhh.mvp.base.mvp;

/**
 * Created by dhh on 2017/6/29.
 */

public class DhhSubject<T> extends DhhScheduler<T> implements DhhScheduler.Execute<T> {
    final SubjectExecute<T> subjectExecute;

    public DhhSubject(SubjectExecute<T> subjectExecute) {
        super(subjectExecute);
        this.subjectExecute = subjectExecute;
    }

    @Override
    public void onNext(T t) {
        subjectExecute.onNext(t);
    }

    @Override
    public DhhScheduler<T> createOn(Scheduler scheduler) {

        return super.createOn(scheduler);
    }

    @Override
    public void onCompleted() {
        subjectExecute.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        subjectExecute.onError(e);
    }

    public static <T> DhhSubject<T> create() {
        return new DhhSubject<T>(new SubjectExecute<T>());
    }

    public DhhScheduler<T> asDhhScheduler() {
        return this;
    }
}
