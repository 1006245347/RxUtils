package com.rxutils.jason.ui.launcher;

import android.content.Context;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.rxutils.jason.R;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.global.HttpPresenter;
import com.rxutils.jason.ui.videoview.JZDataSource;
import com.rxutils.jason.ui.videoview.JzvdStd;
import com.rxutils.jason.ui.videoview.MyJzvdStd;
import com.rxutils.jason.ui.videoview.VideoStateListenerImp;
import com.rxutils.jason.ui.workflow.Node;
import com.rxutils.jason.ui.workflow.WorkFlow;
import com.rxutils.jason.ui.workflow.WorkNode;
import com.rxutils.jason.ui.workflow.Worker;
import com.rxutils.jason.utils.GlideUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LauncherPresenter2 extends HttpPresenter<LauncherContract2.ILauncherView> {

    public LauncherPresenter2(LauncherContract2.ILauncherView view) {
        super(view);
    }

    private ConstraintSet applyConstranintSet = new ConstraintSet();
    private ConstraintSet resetConstranintSet = new ConstraintSet();
    private HashMap<Integer, ViewBean> viewMaps = new HashMap<>();
    private MyJzvdStd jzvdStd;

    //工作流-节点执行顺序是有 array的值由小到大执行，不关代码顺序
    private static final int NODE_APP_INIT = 10;
    private static final int NODE_LAYOUT_CREATE = 20;
    private static final int NODE_PALY_VIDEO = 30;
    private static final int NODE_CACHE_CLEAR = 70;
    private static final int NODE_AUTO_H5 = 100;

    private WorkFlow mWorkFlow;

    public void startWorkFlow() {
        mWorkFlow = new WorkFlow.Builder()
                .withNode(getNodeInitApp())
                .create();
        mWorkFlow.addNode(getNodeLayoutCreate());
        mWorkFlow.addNode(getNodePlayVideo());
        mWorkFlow.addNode(getNodeClearCache());
        mWorkFlow.addNode(getNodeH5());
        mWorkFlow.start();
    }


    private WorkNode getNodeInitApp() {
        return WorkNode.build(NODE_APP_INIT, new Worker() {
            @Override
            public void doWork(Node curNote) {
                curNote.onCompleted();//进入下一节点标识
            }
        });
    }

    private WorkNode getNodeLayoutCreate() {
        return WorkNode.build(NODE_LAYOUT_CREATE, new Worker() {
            @Override
            public void doWork(Node curNote) {
                resetConstranintSet.clone(mView.$constraintLayout());
                applyConstranintSet.clone(mView.$constraintLayout());
                onApplyLayout();
                GlobalCode.printLog("views=" + viewMaps);
                GlobalCode.printLog("cl_main=" + mView.$constraintLayout().getChildCount());
                curNote.onCompleted();
            }
        });
    }

    //https://blog.csdn.net/Xiao_Gangbeng/article/details/56291762?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-21.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-21.nonecase
    //https://www.jianshu.com/p/16e34f919e1a
    private void onApplyLayout() {
        viewMaps.clear();
        mView.$constraintLayout().removeAllViews();
        //确认控件的数量和属性、起始坐标，宽高px，margin,padding,
        //控件类型，index,value,gravity/font/color/background/click //动画延时布局

        createVideoView(mView.getCurActivity(), new ViewBean(ViewBean.getViewType_VideoView(), 5, 0, 0, 1440, 720, 0, 0, 0, 0, 0, 0, 0, 0, Gravity.CENTER, 0, "url", ""));
        createImageView(mView.getCurActivity(), new ViewBean(ViewBean.getViewType_Image(), 4, 1441, 100, 480, 400, 0, 0, 0, 0, 0, 0, 0, 0, Gravity.CENTER, 0, "url", SetConfig.URL_GREE_TEST2));
        createButton(mView.getCurActivity(), new ViewBean(ViewBean.getViewType_Button(), 1, 0, 721, 480, 360, 0, 0, 0, 0, 0, 0, 0, 0, Gravity.CENTER,
                "vr1", 20.0f, UIhelper.getColor(R.color.cBlack_txt), 0, "url", SetConfig.URL_GREE_TEST1));
        createButton(mView.getCurActivity(), new ViewBean(ViewBean.getViewType_Button(), 2, 481, 721, 480, 360, 0, 0, 0, 0, 1, 1, 1, 1, Gravity.CENTER,
                "vr2", 20.0f, UIhelper.getColor(R.color.cBlack_txt), 0, "url", SetConfig.URL_GREE_TEST3));
        createButton(mView.getCurActivity(), new ViewBean(ViewBean.getViewType_Button(), 3, 961, 721, 480, 360, 0, 0, 0, 0, 1, 1, 1, 1, Gravity.CENTER,
                "photo", 20.0f, UIhelper.getColor(R.color.cBlack_txt), 0, "url", SetConfig.URL_GREE_TEST4));
        createButton(mView.getCurActivity(), new ViewBean(ViewBean.getViewType_Button(), 3, 1441, 721, 480, 360, 0, 0, 0, 0, 0, 0, 0, 0, Gravity.CENTER,
                "photo", 20.0f, UIhelper.getColor(R.color.cBlack_txt), 0, "url", SetConfig.URL_GREE_TEST4));

        TransitionManager.beginDelayedTransition(mView.$constraintLayout());
        applyConstranintSet.applyTo(mView.$constraintLayout());
        onApplyLayout2();
//        createSetView();
    }

    ConstraintSet constraintSet2 = new ConstraintSet();

    private void onApplyLayout2() {
        delayFun(333, new Runnable() {
            @Override
            public void run() {
                constraintSet2.clone(mView.$constraintLayout());
                for (Map.Entry<Integer, ViewBean> entry : viewMaps.entrySet()) {
//                    System.out.println("key is " + entry.getKey());
//                    System.out.println("value is " + entry.getValue());
                    constraintSet2.clear(entry.getKey());   //去除每个控件的约束
                    constraintSet2.constrainWidth(entry.getKey(), (int) entry.getValue().getWidth());
                    constraintSet2.constrainHeight(entry.getKey(), (int) entry.getValue().getHeight());
                    constraintSet2.connect(entry.getKey(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, (int) entry.getValue().getStartX());
                    constraintSet2.connect(entry.getKey(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (int) entry.getValue().getEndY());
                }
                createSetView();
                constraintSet2.applyTo(mView.$constraintLayout());
                TransitionManager.beginDelayedTransition(mView.$constraintLayout());
            }
        });
    }

    private void createSetView() {
        ImageView iv_set = new ImageView(mView.getCurActivity());
        iv_set.setId(View.generateViewId());
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             UIhelper.showAdminDialog(mView.getCurActivity());
            }
        });
        constraintSet2.constrainWidth(iv_set.getId(), 40);
        constraintSet2.constrainHeight(iv_set.getId(), 40);
        constraintSet2.connect(iv_set.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet2.connect(iv_set.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        mView.$constraintLayout().addView(iv_set);
    }

    private Button createButton(Context context, final ViewBean viewBean) {
        Button button = new Button(context);
        button.setId(View.generateViewId());
        button.setPadding(viewBean.getPaddingLeft(), viewBean.getMarginTop(), viewBean.getPaddingRight(), viewBean.getPaddingBottom());
        if (!TextUtils.isEmpty(viewBean.getValue())) {
            button.setText(viewBean.getValue());
            button.setTextColor(viewBean.getFontColor());
            button.setTextSize(viewBean.getFontSize());
            button.setGravity(viewBean.getGravity());
        }
        if (TextUtils.isEmpty(viewBean.getBackgroundUrl())) {
            button.setBackgroundResource(viewBean.getBackgroundRes());
        } else {
            switch (UIhelper.getRandom()) {
                case 1:
                case 2:
                case 3:
                    button.setBackgroundColor(UIhelper.getColor(R.color.colorPrimary));
                    break;
                case 4:
                case 5:
                case 6:
                    button.setBackgroundColor(UIhelper.getColor(R.color.colorAccent));
                    break;
                case 7:
                case 8:
                case 9:
                case 10:
                    button.setBackgroundColor(UIhelper.getColor(R.color.colorPrimaryDark));
                    break;
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebHtmlAty.launchWebAty(mView.getCurActivity(), viewBean.getLink());
            }
        });
        applyConstranintSet.constrainWidth(button.getId(), (int) viewBean.getWidth());
        applyConstranintSet.constrainHeight(button.getId(), (int) viewBean.getHeight());
        //直接确定坐标，没有过渡感
//        applyConstranintSet.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, (int) viewBean.getStartX());
//        applyConstranintSet.connect(button.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (int) viewBean.getEndY());
        // 居中显示
        applyConstranintSet.connect(button.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        applyConstranintSet.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        applyConstranintSet.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        applyConstranintSet.connect(button.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        viewMaps.put(button.getId(), viewBean);
        mView.$constraintLayout().addView(button);
        return button;
    }

    private TextView createTextView(Context context, final ViewBean viewBean) {
        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        textView.setPadding(viewBean.getPaddingLeft(), viewBean.getPaddingTop(), viewBean.getPaddingRight(), viewBean.getPaddingBottom());
        if (!TextUtils.isEmpty(viewBean.getValue())) {
            textView.setText(viewBean.getValue());
            textView.setTextSize(viewBean.getFontSize());
            textView.setTextColor(viewBean.getFontColor());
            textView.setGravity(viewBean.getGravity());
        }
        if (TextUtils.isEmpty(viewBean.getBackgroundUrl())) {
            textView.setBackgroundResource(viewBean.getBackgroundRes());
        } else {
            switch (UIhelper.getRandom()) {
                case 1:
                case 2:
                case 3:
                    textView.setBackgroundColor(UIhelper.getColor(R.color.colorPrimary));
                    break;
                case 4:
                case 5:
                case 6:
                    textView.setBackgroundColor(UIhelper.getColor(R.color.colorAccent));
                    break;
                case 7:
                case 8:
                case 9:
                case 10:
                    textView.setBackgroundColor(UIhelper.getColor(R.color.colorPrimaryDark));
                    break;
            }
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebHtmlAty.launchWebAty(mView.getCurActivity(), viewBean.getLink());
            }
        });
//        applyConstranintSet.setMargin(textView.getId(), ConstraintSet.START, (int) viewBean.getStartX());
//        applyConstranintSet.setMargin(textView.getId(), ConstraintSet.TOP, (int) viewBean.getEndY());
        applyConstranintSet.constrainWidth(textView.getId(), (int) viewBean.getWidth());
        applyConstranintSet.constrainHeight(textView.getId(), (int) viewBean.getHeight());
//        applyConstranintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, (int) viewBean.getStartX());
//        applyConstranintSet.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (int) viewBean.getEndY());

        applyConstranintSet.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        applyConstranintSet.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        applyConstranintSet.connect(textView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        applyConstranintSet.connect(textView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        viewMaps.put(textView.getId(), viewBean);
        mView.$constraintLayout().addView(textView);
        return textView;
    }

    private ImageView createImageView(Context context, final ViewBean viewBean) {
        final ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setPadding(viewBean.getPaddingLeft(), viewBean.getPaddingTop(), viewBean.getPaddingRight(), viewBean.getPaddingBottom());
        GlideUtils.loadImageViewLoding(mView.getCurActivity(), viewBean.getBackgroundUrl(), imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebHtmlAty.launchWebAty(mView.getCurActivity(), viewBean.getLink());
            }
        });
        applyConstranintSet.constrainWidth(imageView.getId(), (int) viewBean.getWidth());
        applyConstranintSet.constrainHeight(imageView.getId(), (int) viewBean.getHeight());
//        applyConstranintSet.connect(imageView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, (int) viewBean.getStartX());
//        applyConstranintSet.connect(imageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (int) viewBean.getEndY());
        applyConstranintSet.connect(imageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        applyConstranintSet.connect(imageView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        applyConstranintSet.connect(imageView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        applyConstranintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        viewMaps.put(imageView.getId(), viewBean);
        mView.$constraintLayout().addView(imageView);
        return imageView;
    }

    private MyJzvdStd createVideoView(Context context, ViewBean viewBean) {
        MyJzvdStd jzvdStd = new MyJzvdStd(context);
        jzvdStd.setId(View.generateViewId());
        jzvdStd.setPadding(viewBean.getPaddingLeft(), viewBean.getPaddingTop(), viewBean.getPaddingRight(), viewBean.getPaddingBottom());
        jzvdStd.setBackgroundResource(R.mipmap.ic_default1);
        this.jzvdStd = jzvdStd;
        applyConstranintSet.constrainWidth(jzvdStd.getId(), (int) viewBean.getWidth());
        applyConstranintSet.constrainHeight(jzvdStd.getId(), (int) viewBean.getHeight());
//        applyConstranintSet.connect(jzvdStd.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, (int) viewBean.getStartX());
//        applyConstranintSet.connect(jzvdStd.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (int) viewBean.getEndY());
        applyConstranintSet.connect(jzvdStd.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        applyConstranintSet.connect(jzvdStd.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        applyConstranintSet.connect(jzvdStd.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        applyConstranintSet.connect(jzvdStd.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        viewMaps.put(jzvdStd.getId(), viewBean);
        GlideUtils.loadImageView(mView.getCurActivity(), "", jzvdStd.thumbImageView);
        mView.$constraintLayout().addView(jzvdStd);
        return jzvdStd;
    }

    private void onResetLayout() {
        resetConstranintSet.applyTo(mView.$constraintLayout());
    }

    private WorkNode getNodeClearCache() {
        return WorkNode.build(NODE_CACHE_CLEAR, new Worker() {
            @Override
            public void doWork(Node curNote) {
                curNote.onCompleted();
            }
        });
    }

    private WorkNode getNodeH5() {
        return WorkNode.build(NODE_AUTO_H5, new Worker() {
            @Override
            public void doWork(Node curNote) {
//                curNote.onCompleted();
                mWorkFlow.dispose();
            }
        });
    }

    private WorkNode getNodePlayVideo() {
        return WorkNode.build(NODE_PALY_VIDEO, new Worker() {
            @Override
            public void doWork(Node curNote) {
                if (null == jzvdStd) {
                    GlobalCode.printLog("err:videoview" + null);
                    curNote.onCompleted();
                } else {
                    LinkedHashMap map = new LinkedHashMap();
                    map.put("high", SetConfig.URL_VIDEO1);
//            map.put("high", SetConfig.URL_VIDEO2);
                    final JZDataSource jzDataSource = new JZDataSource(map, "");
                    jzDataSource.looping = true;
                    jzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_NORMAL);
                    jzvdStd.setAllControlsVisiblity(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
                    jzvdStd.tinyBackImageView.setVisibility(View.GONE);
                    jzvdStd.titleTextView.setText("jason test");

                    jzvdStd.setVideoStateListener(new VideoStateListenerImp() {
                        @Override
                        public void onStartClick() {
                            super.onStartClick();
                        }
                    });
                    jzvdStd.startVideo();
                    curNote.onCompleted();
                }
            }
        });
    }

    @Override
    public void detach() {
        mWorkFlow.dispose();
        super.detach();
    }
}
