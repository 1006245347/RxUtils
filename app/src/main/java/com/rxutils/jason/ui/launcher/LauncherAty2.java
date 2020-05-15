package com.rxutils.jason.ui.launcher;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.rxutils.jason.R;
import com.rxutils.jason.base.BaseActivity;
import com.rxutils.jason.ui.videoview.Jzvd;
import com.rxutils.jason.ui.videoview.JzvdStd;

public class LauncherAty2 extends BaseActivity<LauncherPresenter2> implements LauncherContract2.ILauncherView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher_aty2;
    }

    @Override
    protected void initAty() {
        mPresenter.startWorkFlow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        Jzvd.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    protected LauncherPresenter2 onCreatePresenter() {
        return new LauncherPresenter2(this);
    }

    @Override
    public ConstraintLayout $constraintLayout() {
        return bindView(R.id.cl_main);
    }
}
