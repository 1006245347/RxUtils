package com.rxutils.jason.http;


import com.rxutils.jason.global.GlobalCode;

import java.io.IOException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Created by jason-何伟杰，19/8/21
 * des:封装http结果回调
 */
public abstract class DisposeCallback extends DisposableObserver<ResponseBody> {
    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            onRequestSuc(responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        GlobalCode.printLog(e);
        onRequestErr(e);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onRequestSuc(String result);

    protected abstract void onRequestErr(Throwable throwable);
}
