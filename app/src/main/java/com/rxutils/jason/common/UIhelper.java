package com.rxutils.jason.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rxutils.jason.R;
import com.rxutils.jason.ui.sydialoglib.IDialog;
import com.rxutils.jason.ui.sydialoglib.SYDialog;
import com.rxutils.jason.utils.ApkUtils;
import com.rxutils.jason.utils.MMKVUtil;

import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;


/**
 * Created by jason-何伟杰，19/8/20
 * des:界面跳转
 */
public class UIhelper {

    public static void showAdminDialog(final Context context) {
        new SYDialog.Builder(context)
                .setDialogView(R.layout.dialog_admin_set)
                .setWindowBackgroundP(0.8f)
                .setAnimStyle(R.style.translate_style)
                .setBuildChildListener(new IDialog.OnBuildListener() {
                    @Override
                    public void onBuildChildView(final IDialog dialog, View view, int layoutRes) {
                        TextView tv_version, tv_sdk, tv_date, tv_mac, tv_save;
                        tv_save = view.findViewById(R.id.tv_save);
                        tv_version = view.findViewById(R.id.tv_version);
                        tv_date = view.findViewById(R.id.tv_date);
                        tv_sdk = view.findViewById(R.id.tv_sdkversion);
                        tv_mac = view.findViewById(R.id.tv_mac);
                        tv_version.setText("Ver: "+getVersionName());
                        tv_date.setText("" + new Date());
                        tv_sdk.setText("Sdk: " + Build.VERSION.RELEASE);
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
                                while (interfaceEnumeration.hasMoreElements()) {
                                    NetworkInterface networkInterface = interfaceEnumeration.nextElement();
                                    byte[] addr = networkInterface.getHardwareAddress();
                                    if (addr == null || addr.length == 0) {
                                        continue;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    for (byte b : addr) {
                                        sb.append(String.format("%02X:", b));
                                    }
                                    if (sb.length() > 0) {
                                        sb.deleteCharAt(sb.length() - 1);
                                    }
                                    tv_mac.setText("Mac: " + sb.toString());
                                }
                            } else {
                                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                                WifiInfo info = wifiManager.getConnectionInfo();
                                tv_mac.setText("Mac: " + info.getMacAddress());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            tv_mac.setText("Mac: err");
                        }
                        final CheckBox cb_web, cb_progress, cb_close;
                        cb_web = view.findViewById(R.id.cb_web);
                        cb_progress = view.findViewById(R.id.cb_progress);
                        cb_close = view.findViewById(R.id.cb_close);
                        boolean isWeb = MMKVUtil.getBool(SetConfig.CODE_USE_NATICE_WEB, false);
                        boolean isProgress = MMKVUtil.getBool(SetConfig.CODE_HIDE_PROGRESS, false);
                        boolean isClose = MMKVUtil.getBool(SetConfig.CODE_DOWN_CLOSE, false);
                        cb_web.setChecked(isWeb);
                        cb_progress.setChecked(isProgress);
                        cb_close.setChecked(isClose);
                        tv_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MMKVUtil.addBool(SetConfig.CODE_USE_NATICE_WEB, cb_web.isChecked());
                                MMKVUtil.addBool(SetConfig.CODE_DOWN_CLOSE, cb_close.isChecked());
                                MMKVUtil.addBool(SetConfig.CODE_HIDE_PROGRESS, cb_progress.isChecked());
                                dialog.dismiss();
                            }
                        });
                    }
                }).show();
    }

    public static void switch2Aty(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * @return 获取应用的版本名
     */
    public static String getVersionName() {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * @return 获取当前应用包名
     */
    public static String getPackageName() {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "package_name_err";
    }

    /**
     * @return 跳转应用详情页面
     */
    public static Intent getAppSettingIntent() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return intent;
    }

    /**
     * @return 得到上下文
     */
    public static Context getContext() {
        return RxApp.getContext();
    }

    /**
     * @return 得到resources对象
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * @return 得到string.xml中的字符串
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * @return 得到string.xml中的字符串，带点位符
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * @return 得到dimens.xml中的值
     */
    public static int getDimsion(int id) {
        return (int) getResource().getDimension(id);
    }

    /**
     * @return 得到colors.xml中的颜色
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * @return 获得随机数
     */
    public static int getRandom() {
        return (int) (1 + Math.random() * 10);
    }
}
