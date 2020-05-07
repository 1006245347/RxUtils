package com.rxutils.jason.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.utils.MMKVUtil;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author by jason-何伟杰，2020/4/30
 * des:android7.0后系统语言是一组优先列表
 * https://www.cnblogs.com/Sharley/p/9155824.html
 */
public class AppLanguageUtils {

    public static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(2) {{
        put(SetConfig.CODE_LANGUAGE_ENGLISH, Locale.ENGLISH);
        put(SetConfig.CODE_LANGUAGE_CHINESE, Locale.CHINESE);
    }};

    /**
     * @param newLanguage 设置新的语言
     */
    public static void onChangeAppLanguage(Context context, String newLanguage) {
        MMKVUtil.addStr(SetConfig.CODE_LANGUAGE_SET, newLanguage);
        AppLanguageUtils.changeAppLanguage(context, newLanguage);
        AppLanguageUtils.changeAppLanguage(RxApp.getContext(), newLanguage);
//        this.recreate();    //Activity重启
    }

    @SuppressWarnings("deprecation")  //app全局设置语言
    public static void changeAppLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    private static boolean isSupportLanguage(String language) {
        return mAllLanguages.containsKey(language);
    }

    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }

        if (null == language) {//为空则表示首次安装或未选择过语言，获取系统默认语言-这里是中文
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale.getLanguage();
                }
            }
        }
        return SetConfig.CODE_LANGUAGE_CHINESE;
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #mAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #mAllLanguages}，返回中文
     *
     * @param language language
     * @return 注意返回的默认是中文
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mAllLanguages.get(language);
        } else {
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.SIMPLIFIED_CHINESE;
    }


    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = AppLanguageUtils.getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}
