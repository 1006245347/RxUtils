package com.rxutils.jason.ui.launcher;

import android.view.View;
import android.view.ViewGroup;

import com.rxutils.jason.R;
import com.rxutils.jason.global.HttpPresenter;
import com.rxutils.jason.ui.videoview.JZDataSource;
import com.rxutils.jason.ui.videoview.JzvdStd;
import com.rxutils.jason.ui.videoview.VideoStateListener;
import com.rxutils.jason.utils.GlideUtils;

import java.util.LinkedHashMap;

public class LauncherPresenter extends HttpPresenter<LauncherContract.ILauncherView> {
    public LauncherPresenter(LauncherContract.ILauncherView view) {
        super(view);
    }

    public void initView() {

    }

    public void playVideo() {
        //设置封面
//        GlideUtils.loadImageView(mView.getCurActivity(),"",mView.$jzvdStd().thumbImageView);
        LinkedHashMap map = new LinkedHashMap();
        //测试播放地址
//        map.put("high", "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//        map.put("high", "https://youku.com-l-youku.com/20190207/20335_d1f19bfb/index.m3u8");
        map.put("high", "https://ksv-video-publish.cdn.bcebos.com/56e71ec0d97ca97210e5ed624cbb6e135ba74722.mp4?auth_key=1635403544-0-0-9fe085acc9a635fbd9173c29719b6059");
        JZDataSource jzDataSource = new JZDataSource(map, "");
        mView.$jzvdStd().setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_NORMAL);
        mView.$jzvdStd().setAllControlsVisiblity(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
        mView.$jzvdStd().tinyBackImageView.setVisibility(View.GONE);
        mView.$jzvdStd().titleTextView.setText("jason test");
        mView.$jzvdStd().setVideoStateListener(new VideoStateListener() {
            @Override
            public void onStateNormal() {

            }

            @Override
            public void onPreparing() {

            }

            @Override
            public void onStartClick() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onPlaying() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onProgressChanged(int progress) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onTouch() {

            }

            @Override
            public void onStartDismissControlViewTimer() {

            }
        });
    }
}
