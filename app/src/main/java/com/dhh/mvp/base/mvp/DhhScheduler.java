package com.dhh.mvp.base.mvp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by dhh on 2017/6/22.
 */

public class DhhScheduler<T> {

    final OnExecute<T> onExecute;

    protected DhhScheduler(OnExecute<T> onExecute) {
        this.onExecute = onExecute;
    }

    public static <T> DhhScheduler<T> create(OnExecute<T> onExecute) {
        return new DhhScheduler<>(onExecute);
    }

    public static <T> DhhScheduler<T> just(final T t) {
        return create(new OnExecute<T>() {
            @Override
            public void call(Execute<T> execute) {
                try {
                    execute.onNext(t);
                    execute.onCompleted();
                } catch (Exception e) {
                    execute.onError(e);
                }
            }
        });
    }

    public DhhScheduler<T> createOn(final Scheduler scheduler) {

        return create(new OnExecute<T>() {
            @Override
            public void call(final Execute<T> execute) {
                scheduler.post(new Runnable() {
                    @Override
                    public void run() {
                        execute(execute);
                    }
                });
            }
        });
    }

    public DhhScheduler<T> executeOn(final Scheduler scheduler) {
        return create(new OnExecute<T>() {
            @Override
            public void call(final Execute<T> execute) {
                execute(new Execute<T>() {
                    @Override
                    public void onNext(final T t) {
                        scheduler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    execute.onNext(t);
                                } catch (Exception e) {
                                    execute.onError(e);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final Throwable e) {
                        scheduler.post(new Runnable() {
                            @Override
                            public void run() {
                                execute.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {
                        scheduler.post(new Runnable() {
                            @Override
                            public void run() {
                                execute.onCompleted();
                            }
                        });
                    }
                });
            }
        });
    }

    public DhhScheduler<T> initThread() {
        return createOn(Schedulers.io()).executeOn(Schedulers.mainThread());
    }

    public <R> DhhScheduler<R> map(final Func1<T, R> func) {
        return create(new OnExecute<R>() {
            @Override
            public void call(final Execute<R> execute) {
                execute(new Execute<T>() {
                    @Override
                    public void onNext(T t) {
                        execute.onNext(func.call(t));
                    }

                    @Override
                    public void onError(Throwable e) {
                        execute.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        execute.onCompleted();

                    }
                });
            }
        });
    }

    public <R> DhhScheduler<R> flatMap(final Func1<? super T, ? extends DhhScheduler<? extends R>> func) {

        return create(new OnExecute<R>() {
            @Override
            public void call(final Execute<R> execute) {
                execute(new Execute<T>() {
                    @Override
                    public void onNext(T t) {
                        DhhScheduler<R> dhhScheduler = (DhhScheduler<R>) func.call(t);
                        dhhScheduler.execute(execute);

                    }

                    @Override
                    public void onError(Throwable e) {
                        execute.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                    }
                });
            }
        });
    }

    public DhhScheduler<T> filter(final Func1<T, Boolean> func) {
        return create(new OnExecute<T>() {
            @Override
            public void call(final Execute<T> execute) {
                execute(new Execute<T>() {
                    @Override
                    public void onNext(T t) {
                        Boolean filter = func.call(t);
                        if (filter) {
                            execute.onNext(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        execute.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        execute.onCompleted();
                    }
                });
            }
        });
    }

    public static <T> DhhScheduler<T> from(final Iterable<? extends T> iterable) {
        return create(new OnExecute<T>() {
            @Override
            public void call(Execute<T> execute) {
                try {
                    for (T t : iterable) {
                        execute.onNext(t);
                    }
                    execute.onCompleted();
                } catch (Exception e) {
                    execute.onError(e);
                }
            }
        });
    }

    public DhhScheduler<T> takeFirst(final int count) {
        return create(new OnExecute<T>() {
            @Override
            public void call(final Execute<T> execute) {
                execute(new Execute<T>() {
                    int index = 0;

                    @Override
                    public void onNext(T t) {
                        if (index < count) {
                            execute.onNext(t);
                            index++;
                        }

                    }

                    @Override
                    public void onCompleted() {
                        execute.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        execute.onError(e);
                    }
                });
            }
        });
    }

    public DhhScheduler<T> first() {
        return create(new OnExecute<T>() {
            @Override
            public void call(final Execute<T> execute) {
                execute(new Execute<T>() {
                    boolean isFirst = true;

                    @Override
                    public void onNext(T t) {
                        if (isFirst) {
                            execute.onNext(t);
                            isFirst = false;
                        }
                    }

                    @Override
                    public void onCompleted() {
                        execute.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        execute.onError(e);
                    }
                });
            }
        });
    }

    public void execute(Execute<T> execute) {
        try {
            onExecute.call(execute);
        } catch (Exception e) {
            execute.onError(e);
        }
    }


    //接口


    public interface OnExecute<T> extends Action1<Execute<T>> {
    }

    public interface Execute<T> {
        void onNext(T t);

        void onCompleted();

        void onError(Throwable e);

    }

    public interface Action1<T> {
        void call(T t);
    }

    public interface Func1<T, R> {
        R call(T t);
    }

    /**
     * 线程模型接口
     */
    public interface Scheduler {
        Handler HANDLER = new Handler(Looper.getMainLooper());
        Executor EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
            int size = -1;

            @Override
            public Thread newThread(@NonNull Runnable r) {
                size ++;
                return new Thread(r, "DhhPool-Thread-" + size);
            }
        });

        void post(Runnable r);
    }

    /**
     * 线程工具类
     */
    public static class Schedulers {
        /**
         * io线程 子线程
         *
         * @return
         */
        public static Scheduler io() {
            return new Scheduler() {
                @Override
                public void post(Runnable r) {
                    EXECUTOR.execute(r);
                }
            };
        }

        /**
         * UI线程 主线程
         *
         * @return
         */
        public static Scheduler mainThread() {
            return new Scheduler() {
                @Override
                public void post(Runnable r) {
                    HANDLER.post(r);
                }
            };
        }
    }

}

