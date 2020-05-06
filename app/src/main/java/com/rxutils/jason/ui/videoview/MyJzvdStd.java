package com.rxutils.jason.ui.videoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.rxutils.jason.R;


/**
 * 播放器管理-该类主要定义是否滑动自动播放的标识，重写onClick方法获取点击播放的事件。
 */
public class MyJzvdStd extends JzvdStd {
    public MyJzvdStd(Context context) {
        super(context);
    }

    public static boolean AUTOPLAY = true;

    public MyJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.start:
            case R.id.start_layout:
//                DBLog.getInstance().v("click_start");
                if (jzDataSource == null || jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.nourl), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentState == CURRENT_STATE_NORMAL) {
                    startVideo();
                    onEvent(JZUserAction.ON_CLICK_START_ICON);
                    AUTOPLAY = true;
                } else if (currentState == CURRENT_STATE_PLAYING) {
                    onEvent(JZUserAction.ON_CLICK_PAUSE);
                    JZMediaManager.pause();
                    onStatePause();
                    AUTOPLAY = false;
                } else if (currentState == CURRENT_STATE_PAUSE) {
                    onEvent(JZUserAction.ON_CLICK_RESUME);
                    JZMediaManager.start();
                    onStatePlaying();
                    AUTOPLAY = true;
                } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                    onEvent(JZUserAction.ON_CLICK_START_AUTO_COMPLETE);
                    startVideo();
                }
                break;
            default:
                super.onClick(v);
                break;
        }

    }


    @Override
    public int getLayoutId() {
        return super.getLayoutId();
//        return R.layout.jz_layout_std;
//        return  R.layout.jz_my_layout_std;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (null != mListener) {
            mListener.onTouch();
        }
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {  //触发这里会重置播放进度
        super.startVideo();
        if (null != mListener) {
            mListener.onStart();
        }
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        if (null != mListener) {
            mListener.onStateNormal();
        }
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (mListener != null) {
            mListener.onPreparing();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (null != mListener) {
            mListener.onPlaying();
        }
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStatePrepared() {
        super.onStatePrepared();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if (null != mListener) {
            mListener.onPause();
        }
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (null != mListener) {
            mListener.onComplete();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (mListener != null) {
            mListener.onProgressChanged(progress);
        }
    }

    @Override
    public void startDismissControlViewTimer() {
        super.startDismissControlViewTimer();
        if (mListener != null) {
            mListener.onStartDismissControlViewTimer();
        }
    }

    private VideoStateListener mListener;

    public VideoStateListener getListener() {
        return mListener;
    }

    public void setVideoStateListener(VideoStateListener listener) {
        mListener = listener;
    }
}
