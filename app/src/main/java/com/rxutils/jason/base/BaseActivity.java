package com.rxutils.jason.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.rxutils.jason.http.RxSchedulers;
import com.rxutils.jason.utils.ActivityStackUtil;
import com.rxutils.jason.utils.ViewUtil;
import com.rxutils.jason.widget.Gloading;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by jason-何伟杰，19/8/20
 * des:mvp框架基类
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected P mPresenter;
    protected Gloading.Holder mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenter = onCreatePresenter();
        ActivityStackUtil.getScreenManager().pushActivity(this);
        initLoadingStatusViewIfNeed();
        initAty();
    }

    protected abstract int getLayoutId();

    /**
     * after onCreate()
     */
    protected abstract void initAty();

    protected void initLoadingStatusViewIfNeed() {
        if (null == mLoading) {
            mLoading = Gloading.getDefault().wrap(this).withRetry(new Runnable() {
                @Override
                public void run() {
                    onLoadRetry();
                }
            });
        }
    }

    protected void onLoadRetry() {
        // override this method in subclass to do retry task
    }

    public void showLoading() {
        initLoadingStatusViewIfNeed();
        mLoading.showLoading();
    }

    public void showLoadSuccess() {
        initLoadingStatusViewIfNeed();
        mLoading.showLoadSuccess();
    }

    public void showLoadFailed() {
        initLoadingStatusViewIfNeed();
        mLoading.showLoadFailed();
    }

    public void showEmpty() {
        initLoadingStatusViewIfNeed();
        mLoading.showEmpty();
    }

    public <T> T bindView(int id) {
        return (T) ViewUtil.f(this, id);
    }

    //1500ms内只允许点击一次
    public void clickFun(int viewId, final Runnable runnable) {
        RxView.clicks(ViewUtil.f(this, viewId))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (mPresenter != null)
                            mPresenter.addDispoable(disposable);   //在Activity销毁后也会被终止
                    }
                })
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        runnable.run();
                    }
                });
    }

    //事件流延迟处理
    public void delayFun(long mills, final Runnable runnable) {
        Observable.timer(mills, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (mPresenter != null)
                            mPresenter.addDispoable(disposable);   //在Activity销毁后也会被终止
                    }
                })
                .compose(RxSchedulers.<Long>io2main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        runnable.run();
                    }
                });
    }

    //周期性事件延迟处理
    public void intervalFun(long mills, final Runnable runnable) {
        Observable.interval(mills, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (mPresenter != null)
                            mPresenter.addDispoable(disposable);
                    }
                })
                .compose(RxSchedulers.<Long>io2main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        runnable.run();
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detach();
        }
        ActivityStackUtil.getScreenManager().popActivity(this);
    }

    @Override
    public Activity getCurActivity() {
        return this;
    }

    @Override
    public Gloading.Holder getLoading() {
        return mLoading;
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
////        Locale newLocale;
////        Context context = LanguageContextWrapper.wrap(newBase, newLocale);
//        super.attachBaseContext(context);
//    }

    protected abstract P onCreatePresenter();
}
