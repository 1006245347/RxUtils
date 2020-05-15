package com.rxutils.jason.ui.launcher;

import android.view.View;
import android.view.ViewGroup;

import com.rxutils.jason.R;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.global.HttpPresenter;
import com.rxutils.jason.ui.videoview.JZDataSource;
import com.rxutils.jason.ui.videoview.JzvdStd;
import com.rxutils.jason.ui.videoview.VideoStateListener;
import com.rxutils.jason.ui.videoview.VideoStateListenerImp;
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
        GlideUtils.loadImageView(mView.getCurActivity(),"",mView.$jzvdStd().thumbImageView);
        LinkedHashMap map = new LinkedHashMap();
        //测试播放地址
//        map.put("high", SetConfig.URL_VIDEO1);
        map.put("high", SetConfig.URL_VIDEO2);
        JZDataSource jzDataSource = new JZDataSource(map, "");
        jzDataSource.looping=true;
        mView.$jzvdStd().setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_NORMAL);
        mView.$jzvdStd().setAllControlsVisiblity(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
        mView.$jzvdStd().tinyBackImageView.setVisibility(View.GONE);
        mView.$jzvdStd().titleTextView.setText("jason test");
        mView.$jzvdStd().setVideoStateListener(new VideoStateListenerImp(){
            @Override
            public void onStartClick() {
                super.onStartClick();
            }
        });
        mView.$jzvdStd().startVideo();
    }

    @Override
    public void detach() {
        mWorkFlow.dispose();
        super.detach();
    }
}
