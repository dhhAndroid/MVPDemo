package com.dhh.mvp.base.mvp;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.functions.Func1;

/**
 * Created by dhh on 2017/6/13.
 */

public class ThreadScheduler<T> {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private PostEvent<T> mPostEvent;
    private ThreadMode mPostThreadMode;
    private ThreadMode mExecuteThreadMode;

    private ThreadScheduler(PostEvent<T> postEvent) {
        mPostEvent = postEvent;
    }

    public static synchronized <T> ThreadScheduler<T> post(PostEvent<T> postEvent) {
        return new ThreadScheduler<>(checNotkNull(postEvent, "postEvent == null"));
    }

    public static synchronized <T> ThreadScheduler<T> just(final T t) {
        return post(new PostEvent<T>() {
            @Override
            public T post() {
                return t;
            }
        });
    }

    public <R> ThreadScheduler<R> map(Func1<T, R> func1) {
        return just(func1.call(mPostEvent.post()));
    }

    public ThreadScheduler<T> filter(Func1<T, Boolean> func1) {
        T t = mPostEvent.post();
        return just(func1.call(t) ? t : null);
    }

    public ThreadScheduler<T> postOn(ThreadMode postThreadMode) {
        mPostThreadMode = postThreadMode;
        return this;
    }

    public ThreadScheduler<T> executeOn(ThreadMode executeThreadMode) {
        mExecuteThreadMode = executeThreadMode;
        return this;
    }

    public ThreadScheduler<T> initThread() {
        mPostThreadMode = ThreadMode.backThread;
        mExecuteThreadMode = ThreadMode.mainThread;
        return this;
    }

    public static void runOnUIThread(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public static void runOnBackThread(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public void execute() {
        if (mPostThreadMode != null) {
            if (mPostThreadMode == ThreadMode.backThread) {
                EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        mPostEvent.post();
                    }
                });
            } else {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        mPostEvent.post();
                    }
                });
            }
        } else {
            mPostEvent.post();
        }
    }

    public void execute(final OnExecute<T> onExecute) {
        checNotkNull(onExecute, "onExecute == null");
        if (mPostThreadMode != null) {
            if (mPostThreadMode == ThreadMode.backThread) {
                EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        final T t = mPostEvent.post();
                        if (mExecuteThreadMode != null && mExecuteThreadMode == ThreadMode.mainThread) {
                            HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    onExecute.onNext(t);
                                }
                            });
                        } else {
                            onExecute.onNext(t);
                        }
                    }
                });
            } else {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        final T t = mPostEvent.post();
                        if (mExecuteThreadMode != null && mExecuteThreadMode == ThreadMode.backThread) {
                            EXECUTOR.execute(new Runnable() {
                                @Override
                                public void run() {
                                    onExecute.onNext(t);
                                }
                            });
                        } else {
                            onExecute.onNext(t);
                        }
                    }
                });
            }
        } else {
            final T t = mPostEvent.post();
            if (mExecuteThreadMode != null) {
                if (mExecuteThreadMode == ThreadMode.mainThread) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            onExecute.onNext(t);
                        }
                    });
                } else {
                    EXECUTOR.execute(new Runnable() {
                        @Override
                        public void run() {
                            onExecute.onNext(t);
                        }
                    });
                }
            } else {
                onExecute.onNext(t);
            }
        }
    }

    public static <T> T checNotkNull(T t, String msg) {
        if (t == null) {
            throw new NullPointerException(msg);
        }
        return t;
    }

    public interface OnExecute<T> {
        void onNext(T t);
    }

    public interface PostEvent<T> {
        T post();
    }

    public enum ThreadMode {
        mainThread,
        backThread
    }
}



