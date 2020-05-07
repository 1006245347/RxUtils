package com.rxutils.jason.ui.launcher;

import android.view.View;
import android.view.ViewGroup;

import com.rxutils.jason.R;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.global.HttpPresenter;
import com.rxutils.jason.ui.videoview.JZDataSource;
import com.rxutils.jason.ui.videoview.JzvdStd;
import com.rxutils.jason.ui.videoview.VideoStateListener;
import com.rxutils.jason.ui.workflow.Node;
import com.rxutils.jason.ui.workflow.WorkFlow;
import com.rxutils.jason.ui.workflow.WorkNode;
import com.rxutils.jason.ui.workflow.Worker;
import com.rxutils.jason.utils.GlideUtils;

import java.util.LinkedHashMap;

public class LauncherPresenter extends HttpPresenter<LauncherContract.ILauncherView> {
    //节点执行顺序是有 array的值由小到大执行，不关代码顺序
    private static final int NODE_APP_INIT = 10;
    private static final int NODE_CACHE_CLEAR = 20;
    private static final int NODE_AUTO_H5 = 30;

    private WorkFlow mWorkFlow;

    public LauncherPresenter(LauncherContract.ILauncherView view) {
        super(view);
        startWorkFlow();
    }

    private void startWorkFlow() {
        mWorkFlow = new WorkFlow.Builder()
                .withNode(getNodeInitApp())
                .create();
        mWorkFlow.addNode(getNodeClearCache());
        mWorkFlow.addNode(getNodeH5());
        mWorkFlow.start();
    }


    private WorkNode getNodeInitApp() {
        return WorkNode.build(NODE_APP_INIT, new Worker() {
            @Override
            public void doWork(Node curNote) {
                GlobalCode.printLog("node_init>>");
                curNote.onCompleted();//进入下一节点标识
            }
        });
    }

    private WorkNode getNodeClearCache() {
        return WorkNode.build(NODE_CACHE_CLEAR, new Worker() {
            @Override
            public void doWork(Node curNote) {
                GlobalCode.printLog("node_cache>>");
                curNote.onCompleted();
            }
        });
    }

    private WorkNode getNodeH5() {
        return WorkNode.build(NODE_AUTO_H5, new Worker() {
            @Override
            public void doWork(Node curNote) {
                GlobalCode.printLog("node_h5>>");
//                curNote.onCompleted();
                mWorkFlow.dispose();
            }
        });
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

    @Override
    public void detach() {
        mWorkFlow.dispose();
        super.detach();
    }
}
