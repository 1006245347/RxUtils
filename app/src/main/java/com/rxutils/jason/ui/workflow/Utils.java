package com.rxutils.jason.ui.workflow;

import android.os.Looper;


/**
 * Created by jason-何伟杰，19/11/26
 * des:
 */
public class Utils {

    static android.os.Handler handler;

    public static void fakeRequest(String url, final HttpCallBack callBack) {
        handler = new android.os.Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onOk();
            }
        }, 500);
    }

}
