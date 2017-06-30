package com.dhh.mvp.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dhh.mvp.R;
import com.dhh.mvp.base.mvp.BaseActivity;
import com.dhh.mvp.base.mvp.DhhScheduler;
import com.dhh.mvp.base.mvp.DhhSubject;
import com.dhh.mvp.base.mvp.RxBus;
import com.dhh.mvp.base.mvp.ThreadScheduler;
import com.dhh.mvp.main.bean.CommBean;
import com.dhh.mvp.main.view.LifecycleFragment;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ikidou.reflect.TypeBuilder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MainActivity extends BaseActivity<MainPresenterImpl> implements MainView {

    BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    private TextView mTextView;
    private WebView webView;
    private DhhSubject<String> subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(new LifecycleFragment(), "test").commitAllowingStateLoss();
        subject = DhhSubject.create();
        subject.asDhhScheduler()
                .map(new DhhScheduler.Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d("MainActivity--", "call:" + Thread.currentThread().getName());
                        return s;
                    }
                })
                .createOn(DhhScheduler.Schedulers.io())
                .execute(new DhhScheduler.Execute<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("MainActivity--", "onNext:" + Thread.currentThread().getName());
                        Log.d("MainActivity--", s);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        subject.onNext("3234");
    }

    private void RxBusText() {
        RxBus.getInstance().post(1, 1);


        final ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Type type = TypeBuilder.newInstance(List.class).addTypeParam(String.class).build();
        Log.d("MainActivity", "type:" + type);
        RxBus.getInstance().<List<String>>toObservable(2)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        for (String string : strings) {
                            Log.d("MainActivity", string);
                        }
                    }
                });
        RxBus.getInstance().post(2, list);
        RxBus.getInstance().postStick(2, list);


        RxBus.getInstance().<List<String>>toObservableStick(2)
                .map(new Func1<List<String>, List<String>>() {
                    @Override
                    public List<String> call(List<String> strings) {
                        Log.d("MainActivity---", "map:" + Thread.currentThread().getName());
                        return strings;
                    }
                })
                .takeUntil(bindOnDestroy())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Log.d("MainActivity---", "onNext:" + Thread.currentThread().getName());
                        for (String string : strings) {
                            Log.d("MainActivity", string);
                        }
                    }
                });

        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .takeUntil(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return isDestroyed();
                    }
                })
                .skipLast(1)

                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });

    }

    private Observable<ActivityEvent> bindOnDestroy() {
        return lifecycleSubject.filter(new Func1<ActivityEvent, Boolean>() {
            @Override
            public Boolean call(ActivityEvent activityEvent) {
                return activityEvent == ActivityEvent.PAUSE;
            }
        }).first();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    public void set(View view) throws IOException {
        subject.onNext(String.valueOf(UUID.randomUUID()));
//        presenter.getDate();
//        presenter.setData();
//        presenter.getrobotData();
//        String command = "pm clear com.gs.gscontrol";
//        ShellUtils.CommandResult result = ShellUtils.execCommand(command, false);
//        Log.d("MainActivity", result.toString());

//        DhhScheduler.create(new DhhScheduler.OnExecute<String>() {
//            @Override
//            public void call(DhhScheduler.Execute<String> execute) {
//                Log.d("MainActivity", "call:" + Thread.currentThread().getName());
//                execute.onNext("AAAAAAAA");
//            }
//        })
//                .createOn(Schedulers.io())
//                .executeOn(AndroidSchedulers.mainThread())
//                .execute(new DhhScheduler.Execute<String>() {
//                    @Override
//                    public void onNext(String s) {
//                        Log.d("MainActivity", "onNext:" + Thread.currentThread().getName());
//                        Log.d("MainActivity", s);
//                    }
//                });
//        String json = "[{\"name\":\"duhuihui\",\"age\":\"19\"}]";
//        Type type = TypeBuilder.newInstance(List.class)
//                .beginSubType(CommBean.class)
//                .addTypeParam(String.class)
//                .endSubType()
//                .build();
//        List<CommBean<String>> list1 = GsonUtlis.fromJson(json, type);
//        Log.d("MainActivity", "type:" + type);
//        Log.d("MainActivity", "list1.size():" + list1.size());
//        CommBean<String> bean = list1.get(0);
//        Log.d("MainActivity", list1.get(0).toString());


        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("DuHuiHui" + i);
        }
        CommBean<List<String>> commBean = new CommBean<>();
        commBean.data = list;
        DhhScheduler.just(commBean)
                .map(new DhhScheduler.Func1<CommBean<List<String>>, List<String>>() {
                    @Override
                    public List<String> call(CommBean<List<String>> listCommBean) {
                        Log.d("MainActivity", "map1:" + Thread.currentThread().getName());
                        return listCommBean.data;
                    }
                })
                .flatMap(new DhhScheduler.Func1<List<String>, DhhScheduler<String>>() {
                    @Override
                    public DhhScheduler<String> call(List<String> strings) {
                        Log.d("MainActivity", "flatMap:" + Thread.currentThread().getName());
                        return DhhScheduler.from(strings);
                    }
                })
                .filter(new DhhScheduler.Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Log.d("MainActivity", "filter:" + Thread.currentThread().getName());
                        return !s.contains("2");
                    }
                })
                .map(new DhhScheduler.Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d("MainActivity", "map2:" + Thread.currentThread().getName());
                        return s + "AAAA";
                    }
                })
                .takeFirst(2).initThread()
                .execute(new DhhScheduler.Execute<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("MainActivity", "onNext:" + Thread.currentThread().getName());
                        Log.d("MainActivity", s);
                        int i = 1 / 0;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("MainActivity", e.getLocalizedMessage());
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("MainActivity", "onCompleted:" + Thread.currentThread().getName());
                    }
                });
    }

    private void TestThreadScheduler() {
        //完整工作流程
        ThreadScheduler
                .post(new ThreadScheduler.PostEvent<String>() {
                    @Override
                    public String post() {
                        Log.d("MainActivity", "run:" + Thread.currentThread().getName());
                        SystemClock.sleep(10000);
                        return "睡觉三秒";
                    }
                })
                .postOn(ThreadScheduler.ThreadMode.backThread)
                .executeOn(ThreadScheduler.ThreadMode.mainThread)
                .execute(new ThreadScheduler.OnExecute<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("MainActivity", s);
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "onNext:" + Thread.currentThread().getName());
                    }
                });
        //在子线程执行run
        ThreadScheduler
                .post(new ThreadScheduler.PostEvent<List<String>>() {
                    @Override
                    public List<String> post() {
                        return null;
                    }
                })
                .postOn(ThreadScheduler.ThreadMode.backThread)
                .execute();
        Log.d("MainActivity", "-----------------------");
        ThreadScheduler
                .post(new ThreadScheduler.PostEvent<String>() {
                    @Override
                    public String post() {
                        Log.d("MainActivity", "post:" + Thread.currentThread().getName());
                        return "DFDF";
                    }
                })
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        Log.d("MainActivity", "call:" + Thread.currentThread().getName());
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            list.add(s + i);
                        }
                        return list;
                    }
                })
                .initThread()
                .execute(new ThreadScheduler.OnExecute<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        Log.d("MainActivity", "onNext:" + Thread.currentThread().getName());
                        for (int i = strings.size() - 1; i >= 0; i--) {
                            String s = strings.get(i);
                            Log.d("MainActivity", s);
                        }
                    }
                });
        Log.d("MainActivity", "-----------------------");
    }

    @Override
    protected MainPresenterImpl initPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public void setText(String s) {
        mTextView.setText(s);
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onDestroy:");
        super.onDestroy();

        Process.killProcess(Process.myPid());

        Glide.get(this).clearMemory();

        Glide.get(this).getBitmapPool().setSizeMultiplier(0.25F);
        RequestManager with = Glide.with(this);
    }

    @Override
    public void handleMessage(Message msg) {
        List<String> obj = (List<String>) msg.obj;
    }
}
