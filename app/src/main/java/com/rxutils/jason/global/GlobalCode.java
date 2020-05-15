package com.rxutils.jason.global;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rxutils.jason.common.RxApp;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.http.ApiEngine;
import com.rxutils.jason.http.FileUploadObserver;
import com.rxutils.jason.utils.ActivityStackUtil;
import com.rxutils.jason.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by jason on 18/9/1.
 */

public class GlobalCode {

    public static String API_HOST = "";             //服务器ip
    public static String APP_TOKEN = "";            //保持的token
    public static String USER_NAME = "";
    public static String USER_LOGO = "";

    private static boolean IS_DEBUG = true;         //本地日志是否打印
    private static boolean IS_ENCRY = false;        //服务器接口是否加密

    /**
     * 弹出警告对话框
     *
     * @param self
     * @param title
     * @param message
     */
    static public void alert(Context self, String title, String message) {
        new AlertDialog.Builder(self)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();


    }

    public static void showAlertDialog(Activity self, String title, String msg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(self)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.v("TAG", dialog + " _dismiss>>");   //事件后自动dismiss,不然泄漏
                    }
                }).show();
    }

    public static void uploadFile(String url, Map<String, RequestBody> map, String key, File file, FileUploadObserver<ResponseBody> observer) {
        if (file != null) {
            ApiEngine.getInstance().upLoad2File(url, encryImgArgs(map), key, file, observer);
        }
    }

    public static void uploadMoreFile(String url, Map<String, RequestBody> map, String key, List<File> files, FileUploadObserver<ResponseBody> observer) {
        if (files != null && files.size() > 0) {
            ApiEngine.getInstance().upload2MoreFile(url, encryImgArgs(map), key, files, observer);
        }
    }

    //加密的公共参数
    public static Map<String, String> encryArgs(Map<String, String> map) {
        if (map == null)
            map = new HashMap<>();
        String mydata = null;
        String mykey = null;
        if (!IS_ENCRY) {
            map.put("app_type", "USER");
        }
        return map;
    }


    public static Map<String, RequestBody> encryImgArgs(Map<String, RequestBody> map) {
        if (map == null)
            map = new HashMap<>();
        String mykey = null;
        String mydata = null;
        if (!IS_ENCRY) {
            map.put("post_time", toRequestBody("123456"));
        }
        return map;
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

    //传入的result对象内存地址会GC，要对这个对象进行开始保存
    public static JSONObject httpJson(String result) {
        String strResult = "";
        strResult = result;
        JSONObject jsonObject = null;
        int code = -1;
        try {
            jsonObject = new JSONObject(strResult);
            code = jsonObject.getInt("code");
            GlobalCode.printLog(String.valueOf(jsonObject));
            switch (code) {
                case 0:
                    if (!IS_ENCRY) {
                        return jsonObject;
                    } else {

                    }
                default:
//                    LoadingDialog.dismissprogress();
                    GlobalCode.alert(ActivityStackUtil.getScreenManager().getCurAty(), "提示", jsonObject.getString("message"));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //解析一个对象 //存在错误收集 httpjson
    public static <T> T getHttpResponse(String result, Class<T> cls) {
        String httpResponse = result;
        JSONObject jsonObject = null;
        jsonObject = httpJson(httpResponse);
        if (null == jsonObject) return null;
        String jsonstring = String.valueOf(jsonObject);
        T t = null;
        try {
            JSONObject jsonObject1 = new JSONObject(jsonstring);
            JSONObject jsondata = jsonObject1.getJSONObject("data");
//            printLog(String.valueOf(jsondata));
            Gson gson = new Gson();
            t = gson.fromJson(String.valueOf(jsondata), cls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @return 字符串转时间搓
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CHINESE);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 权限请求
     * 支持Activity,fragment 在onCreate()调用
     */
    public static void grantCamera(Activity activity) {
        new RxPermissions(activity)
                .requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d("TAG", permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d("TAG", permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d("TAG", permission.name + " is denied.");
                        }
                    }
                });
    }

    public static void grantLocation(Activity activity) {
        new RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                    }
                });
    }

    /**
     * 打印日志 不限行数打印
     *
     * @param log
     */
    public static void printLog(String log) {
        if (!IS_DEBUG) return;
        if (TextUtils.isEmpty(log)) return;
        int segmentSize = 3 * 1024;
        long length = log.length();
        if (length <= segmentSize) {
//            Log.e("TAG", "////////////********0*******/////////////" + "\n" + log);
            Log.e("TAG", log);
        } else {
            while (log.length() > segmentSize) {
                String logContent = log.substring(0, segmentSize);
                log = log.replace(logContent, "");
                Log.e("TAG", "////////////***************/////////////" + "\n" + logContent);
            }
            Log.e("TAG", "////////////***************/////////////" + "\n" + log);
        }
    }

    public static void printLog(Throwable throwable) {
        Log.v("TAG", Log.getStackTraceString(throwable));
    }

    public static void clearImgCache(final Context context) {
        //清除内存
        Glide.get(context).clearMemory();
        //清除磁盘
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size-error";
        }
    }

    //post 数据传参 将自定义对象中的字段取出来放到map
    public static <T> Map<String, String> setArgField(T obj) {

        if (obj == null) return null;
        Map<String, String> map = new HashMap<>();
        Field[] fieldArray = getT(obj).getClass().getDeclaredFields();
        for (int i = 0; i < fieldArray.length; i++) {
            Object o = getFieldValueByName(fieldArray[i].getName(), getT(obj));
            GlobalCode.printLog(fieldArray[i].getName() + "=" + (o == null ? "" : (o).toString()));
            map.put(fieldArray[i].getName(), (o == null ? "" : (o).toString()));
        }
        return map;
    }

    public static <T> T getT(T t) {
        return t;
    }


    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return 判断是否真机还是模拟器
     */
    private static boolean isRealDev(Context context) {
        String url = "tel:" + "13232508893";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        // 是否可以处理跳转到拨号的 Intent
        boolean canCallPhone = intent.resolveActivity(context.getPackageManager()) != null;
        return Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.toLowerCase()
                .contains("vbox") || Build.FINGERPRINT.toLowerCase()
                .contains("test-keys") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL
                .contains("MuMu") || Build.MODEL.contains("virtual") || Build.SERIAL.equalsIgnoreCase("android") || Build.MANUFACTURER
                .contains("Genymotion") || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk"
                .equals(Build.PRODUCT) || ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName()
                .toLowerCase()
                .equals("android") || !canCallPhone;
    }


    /**
     * @param filePath 源文件夹名
     * @param devPath  目标文件夹名
     */
    public static void copyAssets2Dev(String filePath, String devPath) {
        try {
            String fileNames[] = RxApp.getContext().getAssets().list(filePath);
            GlobalCode.printLog("assets-" + fileNames.length);
            if (fileNames.length > 0) {
                File file = new File(devPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                for (String fileName : fileNames) {
                    if (!TextUtils.isEmpty(filePath)) { //assets文件夹
                        copyAssets2Dev(filePath + File.separator + fileName, devPath + File.separator + fileName);
                    } else {
                        copyAssets2Dev(fileName, devPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(devPath);
                InputStream is = RxApp.getContext().getAssets().open(filePath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            GlobalCode.printLog(e);
        } finally {
            ToastUtil.showToast("assets-finish");
        }
    }
}
