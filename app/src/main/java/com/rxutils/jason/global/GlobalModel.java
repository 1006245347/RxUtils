package com.rxutils.jason.global;


import com.rxutils.jason.base.BaseModel;

import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * 接收参数进行网络请求，返回回调实例
 * Created by jason on 18/9/8.
 */

public interface GlobalModel extends BaseModel {

    /**
     * @param url                post请求url
     * @param map                post请求参数，可以加入公共参数
     * @param disposableObserver 回调结果
     */
    void httpMap(String url, Map<String, String> map, DisposableObserver disposableObserver);


    /**
     * @param url                get请求url
     * @param disposableObserver 回调结果
     */
    void httpMap(String url, DisposableObserver disposableObserver);

    /**
     * @param url                post请求urL
     * @param paramsJson         post请求json参数
     * @param disposableObserver 回调结果
     */
    void httpMap(String url, String paramsJson, DisposableObserver disposableObserver);
}
