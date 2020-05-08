package com.rxutils.jason.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.rxutils.jason.BuildConfig;
import com.rxutils.jason.MainActivity;
import com.rxutils.jason.R;
import com.rxutils.jason.global.GlobalAdapter;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.utils.MMKVUtil;
import com.rxutils.jason.utils.ToastUtil;
import com.rxutils.jason.widget.Gloading;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;

import static com.rxutils.jason.common.SetConfig.BUGLY_APPID;

/**
 * Created by jason-何伟杰，19/8/20
 * des:
 */
public class RxApp extends Application {

    private static Context mContext;
    private String devLanguage;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        doSetIp(getString(R.string.SERVER_IP));
        Gloading.debug(BuildConfig.DEBUG);
        Gloading.initDefault(new GlobalAdapter());
        initTBS();
//        initBugly();
        new MMKVUtil.Builder().setSavePath(getPadCacheDir().getAbsolutePath()).build();
        devLanguage = MMKVUtil.getStr(SetConfig.CODE_LANGUAGE_SET, SetConfig.CODE_LANGUAGE_CHINESE);
        onLanguageChange();
    }

    /**
     * 初始化x5浏览器
     */
    private void initTBS() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                GlobalCode.printLog(" onViewInitFinished is " + arg0);
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 初始化Bugly
     */
    private void initBugly() {
        /**
         * Beta高级设置
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         * 每次杀后台就能弹窗，其他运行时要调用checkUpdate()
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.upgradeCheckPeriod = 60 * 1000;  //60
        /**
         * 设置启动延时为3s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 3 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.mipmap.ic_launcher;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /*设置是否显示消息通知*/
//        Beta.enableNotification = true;
        /*设置wifi下自动下载*/
//        Beta.autoDownloadOnWifi = true;
        /*设置是否显示弹窗中的apk信息*/
//        Beta.canShowApkInfo = true;

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false;
        Beta.strUpgradeDialogCancelBtn = "下次再說";  //左右按鈕文字
        Beta.strNetworkTipsConfirmBtn = "立即更新";
        Beta.strUpgradeDialogVersionLabel = "版本";
        Beta.strUpgradeDialogFileSizeLabel = "包大小";
        Beta.strUpgradeDialogUpdateTimeLabel = "更新時間";
        Beta.strUpgradeDialogFeatureLabel = "更新說明";

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        //监听安装包下载状态
        Beta.downloadListener = new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {
                GlobalCode.printLog("bugly_update_downloadListener receive apk file2");
            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                ToastUtil.showToast("新的apk已下載完成,在通知欄處點擊安裝");
                GlobalCode.printLog("bugly_update_downloadListener download apk file success3");
            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {
                GlobalCode.printLog("bugly_update_downloadListener download apk file fail");
            }
        };

        //监听APP升级状态
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                GlobalCode.printLog("bugly_update_upgradeStateListener upgrade fail");
            }

            @Override
            public void onUpgradeSuccess(boolean b) {
                GlobalCode.printLog("bugly_update_upgradeStateListener upgrade success1");
                getAppUpdateInfo();
            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                GlobalCode.printLog("bugly_update_upgradeStateListener upgrade has no new version");
            }

            @Override
            public void onUpgrading(boolean b) {
                GlobalCode.printLog("bugly_update_upgradeStateListener upgrading");
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                GlobalCode.printLog("bugly_update_upgradeStateListener download apk file success4");
            }
        };
        /**
         * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
         * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
         * 参数1：applicationContext
         * 参数2：appId
         * 参数3：是否开启debug
         */
        Bugly.init(getApplicationContext(), BUGLY_APPID, !BuildConfig.DEBUG);
    }

    private void getAppUpdateInfo() {
    }


    /**
     * attachBaseContext（）中还没生成context,此方法不能调用
     */
    public File getPadCacheDir() {
        return getExternalFilesDir("gree_pad_dir");
    }

    /**
     * @return 本软件的下载目录
     */
    public File getDownLoadDir() {
        return getExternalFilesDir("gree_download_dir");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(base, getAppLanguage(base)));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onLanguageChange();
    }

    private void onLanguageChange() {
//        AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getSupportLanguage(getAppLanguage()));
        AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getSupportLanguage(devLanguage));
    }

    private String getAppLanguage(Context context) {
        MMKV.initialize(context.getExternalFilesDir("gree_pad_dir").getAbsolutePath());
        MMKV kv = MMKV.mmkvWithID(context.getExternalFilesDir("gree_pad_dir").getAbsolutePath(), MMKV.MULTI_PROCESS_MODE);
        String strLang = kv.decodeString(SetConfig.CODE_LANGUAGE_SET, SetConfig.CODE_LANGUAGE_CHINESE);
        return strLang;
    }

    //生命周期为整个app,不能应用于dialog等场景
    public static Context getContext() {
        return mContext;
    }

    public static void doSetIp(String ip) {
        GlobalCode.API_HOST = "http://" + ip;
    }
}
