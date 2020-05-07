package com.rxutils.jason.ui.launcher;

import android.widget.LinearLayout;

import com.rxutils.jason.R;
import com.rxutils.jason.base.BaseActivity;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.ui.videoview.Jzvd;
import com.rxutils.jason.ui.videoview.MyJzvdStd;

public class LauncherAty extends BaseActivity<LauncherPresenter> implements LauncherContract.ILauncherView {

    @Override
    protected void initAty() {
        mPresenter.initView();
        clickFun(R.id.line_bottom, new Runnable() {
            @Override
            public void run() {
                WebHtmlAty.launchWebAty(getCurActivity(), SetConfig.URL_GERR_TEST1);
            }
        });
        clickFun(R.id.line_right, new Runnable() {
            @Override
            public void run() {
                WebHtmlAty.launchWebAty(getCurActivity(), SetConfig.URL_GERR_TEST2);
            }
        });
//        delayFun(2000, new Runnable() {
//            @Override
//            public void run() {
//                mPresenter.playVideo();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        delayFun(1000, new Runnable() {
            @Override
            public void run() {
                mPresenter.playVideo();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Jzvd.releaseAllVideos();
        } catch (Exception e) {
            GlobalCode.printLog(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Jzvd.releaseAllVideos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected LauncherPresenter onCreatePresenter() {
        return new LauncherPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher_aty;
    }


    @Override
    public LinearLayout $line_bottom() {
        return bindView(R.id.line_bottom);
    }

    @Override
    public LinearLayout $line_right() {
        return bindView(R.id.line_right);
    }

    @Override
    public MyJzvdStd $jzvdStd() {
        return bindView(R.id.videoview);
    }
}
