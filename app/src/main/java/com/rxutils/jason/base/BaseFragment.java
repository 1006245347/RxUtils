package com.rxutils.jason.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.rxutils.jason.utils.ViewUtil;
import com.rxutils.jason.widget.Gloading;

import java.util.concurrent.TimeUnit;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by jason-何伟杰，19/8/21
 * des:mvp框架的fragment基类
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    protected P mPresenter;
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getContentViewId(), container, false);
            initView();
        }
        ViewGroup group = (ViewGroup) mRootView.getParent();
        if (null != group) {
            group.removeView(mRootView);
        }

        //全局状态切换
        initLoadingStatusViewIfNeed();
        return mLoading.getWrapper();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = onCreatePresenter();
        initFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mPresenter) {
            mPresenter.unDisposable();
        }
    }

    protected Gloading.Holder mLoading;

    protected void initLoadingStatusViewIfNeed() {
        if (null == mLoading) {
            mLoading = Gloading.getDefault().wrap(mRootView).withRetry(new Runnable() {
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

    protected void showLoading() {
        initLoadingStatusViewIfNeed();
        mLoading.showLoading();
    }

    protected void showLoadSuccess() {
        initLoadingStatusViewIfNeed();
        mLoading.showLoadSuccess();
    }

    protected void showLoadFailed() {
        initLoadingStatusViewIfNeed();
        mLoading.showLoadFailed();
    }

    protected void showEmpty() {
        initLoadingStatusViewIfNeed();
        mLoading.showEmpty();
    }

    //1500ms内只允许点击一次
    protected void clickFun(int viewId, final Runnable runnable) {
        RxView.clicks(ViewUtil.f(mRootView, viewId))
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

    protected  <T extends View> T bindView(int id) {
        return (T) mRootView.findViewById(id);
    }

    @Override
    public Activity getCurActivity() {
        return getActivity();
    }

    @Override
    public Gloading.Holder getLoading() {
        return mLoading;
    }

    /**
     * 请将控件的初始化尽量放到这里，避免多次调用
     */
    protected abstract void initView();

    protected abstract void initFragment();

    protected abstract int getContentViewId();

    protected abstract P onCreatePresenter();
}
