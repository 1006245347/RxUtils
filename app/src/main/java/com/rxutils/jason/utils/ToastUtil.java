package com.rxutils.jason.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rxutils.jason.R;
import com.rxutils.jason.common.RxApp;
import com.rxutils.jason.common.UIhelper;


/**
 * Created by jason-何伟杰，19/8/20
 * des:自定义全局toast
 */
public class ToastUtil {

    private static Toast mToast;

    public static void showToast(String txt) {
        try {
            mToast = new Toast(UIhelper.getContext());
            View layout = View.inflate(RxApp.getContext(), R.layout.layout_toast, null);
            TextView tv_des = layout.findViewById(R.id.tv_des);
            tv_des.setText(txt);
            mToast.setView(layout);
            mToast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
