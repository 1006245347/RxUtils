package com.rxutils.jason.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.rxutils.jason.BuildConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

/**
 * @author by jason-何伟杰，2020/5/9
 * des:全兼容的热更新
 */
public class RxApplicationLike extends DefaultApplicationLike {
    private static Context mContext;

    public RxApplicationLike(Application application, int tinkerFlags,
                             boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                             long applicationStartMillsTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillsTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Bugly.init(getApplication(), SetConfig.BUGLY_APPID, !BuildConfig.DEBUG);
    }

    //生命周期为整个app,不能应用于dialog等场景
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        this.mContext = base;
        //安装tinker
        MultiDex.install(base);
        Beta.installTinker(this);
    }

}
