package com.rxutils.jason.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;


/**
 * Created by jason-何伟杰，19/8/20
 * des:界面跳转
 */
public class UIhelper {
    public static void switch2Aty(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * @return 得到应用的版本名
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
     * @return 得到colors.xml中的颜色
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

}
