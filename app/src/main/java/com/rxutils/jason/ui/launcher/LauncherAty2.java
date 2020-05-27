package com.rxutils.jason.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.rxutils.jason.MainActivity;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        } else {
            return false;
        }
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
