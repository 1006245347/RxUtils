package com.rxutils.jason.ui.test;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.rxutils.jason.R;
import com.rxutils.jason.base.BaseActivity;
import com.rxutils.jason.base.BasePresenter;
import com.rxutils.jason.common.RxApp;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.ui.launcher.LauncherAty;
import com.rxutils.jason.ui.server.IGetMessageCallBack;
import com.rxutils.jason.ui.server.MQTTService;
import com.rxutils.jason.ui.server.MyServiceConnection;
import com.rxutils.jason.utils.ToastUtil;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.TbsVideoUtils;

import java.io.DataOutputStream;
import java.io.OutputStream;

import static com.rxutils.jason.common.AppLanguageUtils.onChangeAppLanguage;

public class DarkTestAty extends BaseActivity implements View.OnClickListener, IGetMessageCallBack {


    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;

    @Override
    protected void initAty() {
        findViewById(R.id.btn_default).setOnClickListener(this);
        findViewById(R.id.btn_dark).setOnClickListener(this);
        findViewById(R.id.btn_light).setOnClickListener(this);
        findViewById(R.id.btn_system).setOnClickListener(this);
//        initMTQQ();
    }

    private void initMTQQ() {
        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(this);
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void setMessage(String message) {
        ToastUtil.showToast(message);
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }

    @Override
    protected void onDestroy() {
//        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_default) {
            onChangeAppLanguage(this, SetConfig.CODE_LANGUAGE_CHINESE);
        } else if (v.getId() == R.id.btn_light) {
//            GlobalCode.copyAssets2Dev("Whale Launcher.apk", ((RxApp)RxApp.getContext()).getDownLoadDir().getAbsolutePath()+"/launcher.apk");
//            MQTTService.publish("jason publish sth ,do you see it?");
            x5Play();
        } else if (v.getId() == R.id.btn_dark) {
            onChangeAppLanguage(this, SetConfig.CODE_LANGUAGE_ENGLISH);
        } else if (v.getId() == R.id.btn_system) {
            UIhelper.switch2Aty(getCurActivity(), LauncherAty.class);
        }
//        setResult(SetConfig.CODE_COMMON_BACK);
//        recreate();
//        finish();
    }

    private void x5Play() {
        if (TbsVideo.canUseTbsPlayer(this)) {
            Bundle bundle = new Bundle();
            bundle.putInt("screenMode",102);
            TbsVideo.openVideo(this,"https://youku.com-l-youku.com/20190207/20335_d1f19bfb/index.m3u8",bundle);
        }
    }

 /*   public void onChangeAppLanguage(String newLanguage) {
        MMKVUtil.addStr(SetConfig.CODE_LANGUAGE_SET, newLanguage);
        AppLanguageUtils.changeAppLanguage(this, newLanguage);
        AppLanguageUtils.changeAppLanguage(RxApp.getContext(), newLanguage);
        this.recreate();    //Activity重启
    }*/


    /**
     * 模拟用户点击
     */
    private void mockClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.obtain(downTime,
                downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        MotionEvent upEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,
                x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    private void mockClick2(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        int metaState = 0;
        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN,
                x, y, metaState);
        view.dispatchTouchEvent(downEvent);
        MotionEvent upEvent = MotionEvent.obtain(downTime + 1000, eventTime + 1000,
                MotionEvent.ACTION_UP, x, y, metaState);
        view.dispatchTouchEvent(upEvent);
    }

    /**
     * 执行shell命令
     */
    private void execShellCmd(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void testShell() {
        //https://blog.csdn.net/u011368551/article/details/85061000?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2
        execShellCmd("getevent -p");
        execShellCmd("sendevent /dev/N5 1 158 1");
        execShellCmd("sendevent /dev/N5 1 158 0");
        execShellCmd("input keyevent 3");//home
        execShellCmd("input text  'helloworld!' ");
        execShellCmd("input tap 168 252");
        execShellCmd("input swipe 100 250 200 280");
        //更换图标 https://www.jianshu.com/p/707e3e347361
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dark_test_aty;
    }
}
