package com.rxutils.jason.base;

import android.app.Activity;

import com.rxutils.jason.widget.Gloading;


/**
 * Created by jason-何伟杰，19/8/19
 * des:获取上下文
 */
public interface BaseView {
    Activity getCurActivity();

    Gloading.Holder getLoading();

}
