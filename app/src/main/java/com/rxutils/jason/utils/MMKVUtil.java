package com.rxutils.jason.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rxutils.jason.common.RxApp;
import com.rxutils.jason.global.GlobalCode;
import com.tencent.mmkv.MMKV;

import java.util.List;

/**
 * Created by jason-何伟杰，19/8/19
 * des:本地读写数据工具类
 */
public class MMKVUtil {
    // 使用
// new MMKVUtil.Builder()
//         .setSavePaht("")
//                .build();
    private static String savePath;

    //建造者模式
    public static final class Builder {
        private String savePath;

        public Builder() {
        }

        public Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public MMKVUtil build() {
            return new MMKVUtil(this);
        }
    }

    private MMKVUtil(Builder builder) {
        savePath = builder.savePath;
        init(savePath);
    }


    /*//单例
    private static MMKVUtil util;
    public static MMKVUtil initile() {
        if (util == null) {
            synchronized ((MMKVUtil.class)) {
                if (util == null) {
                    util = new MMKVUtil();
                }
            }
        }
        return util;
    }*/

    public static MMKV kv = null;

    private static MMKV getKv() {
        if (kv == null) {
            init(savePath);
        }
        return kv;
    }

    public static void init(@Nullable String savePath) {
        if (TextUtils.isEmpty(savePath)) {
            GlobalCode.printLog("path="+savePath);
            MMKV.initialize(RxApp.getContext());
            kv = MMKV.defaultMMKV();
        } else {
            MMKV.initialize(savePath);
            kv = MMKV.mmkvWithID(savePath);
        }
    }

    //直接保存自定义实体对象
    public static void addObj(String key, Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        addStr(key, json);
    }

    public static <T> T getObj(String key, Class<T> cls) {
        String json = getStr(key);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //保存集合数据
    private void test() {
        Gson gson = new Gson();
        List<String> list = null;
        addStr("your_list_key", gson.toJson(list)); //以json文本格式 保存
        list = gson.fromJson("strJson", new TypeToken<List<String>>() {
        }.getType());    //从文本中 获取集合
    }

    //是否存在
    public static boolean hasKey(String key) {
        return getKv().containsKey(key);
    }

    //删除某个key
    public static void remove(String key) {
        getKv().remove(key);
    }

    public static void clear() {
        getKv().clearAll();
    }

    public static boolean addStr(String key, String value) {
        return getKv().encode(key, value);
    }

    public static boolean addBool(String key, boolean flag) {
        return getKv().encode(key, flag);
    }

    public static boolean addInteger(String key, int i) {
        return getKv().encode(key, i);
    }

    public static boolean addDouble(String key, double v) {
        return getKv().encode(key, v);
    }

    public static boolean addLong(String key, long l) {
        return getKv().encode(key, l);
    }

    public static boolean addFloat(String key, float f) {
        return getKv().encode(key, f);
    }


    public static String getStr(String key) {
        return getKv().decodeString(key);
    }

    public static String getStr(String key, String def) {
        return getKv().decodeString(key, def);
    }

    public static boolean getBool(String key) {
        return getKv().decodeBool(key);
    }

    public static boolean getBool(String key, boolean def) {
        return getKv().decodeBool(key, def);
    }

    public static int getInteger(String key) {
        return getKv().decodeInt(key);
    }

    public static int getInteger(String key, int def) {
        return getKv().decodeInt(key, def);
    }

    public static double getDouble(String key) {
        return getKv().decodeDouble(key);
    }

    public static double getDouble(String key, double def) {
        return getKv().decodeDouble(key, def);
    }

    public static float getFloat(String key, float def) {
        return getKv().decodeFloat(key, def);
    }

    public static long getLong(String key) {
        return getKv().decodeLong(key);
    }

    public static long getLong(String key, long def) {
        return getKv().decodeLong(key, def);
    }


}
