package com.rxutils.jason.global;


import com.rxutils.jason.base.BasePresenter;

/**
 * 接收view层的数据-》传达到model层-》接收回调数据处理后显示
 * Created by jason on 18/9/8.
 */

public interface GlobalPresenter extends BasePresenter {
    /**
     * mvp网络请求
     */
    void doHttpRequest();

}
