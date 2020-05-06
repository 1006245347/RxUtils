package com.rxutils.jason.global;

import com.rxutils.jason.base.BasePresenterImpl;
import com.rxutils.jason.base.BaseView;
import com.rxutils.jason.http.ApiEngine;
import com.rxutils.jason.http.RxSchedulers;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by jason-何伟杰，19/8/20
 * des:一个http请求的共用model
 */
public class HttpPresenter<T extends BaseView> extends BasePresenterImpl<T> implements GlobalPresenter {
    protected GlobalModel mModel;

    /**
     * 默认是post请求，url为全路径，参数是map,回调方式接受结果
     */
    public HttpPresenter(T view) {
        super(view);
        this.mModel = new GlobalModel() {
            @Override
            public void httpMap(String url, Map<String, String> map, DisposableObserver observer) {
                GlobalCode.printLog("post_url>>>" + url);
                ApiEngine.getInstance().getApiService().doPostRequest(url, GlobalCode.encryArgs(map))
                        .compose(RxSchedulers.<ResponseBody>io2main())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) {
                                System.out.println("Primary Action do!");
                                addDispoable(disposable);
                                showLoading();
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() {
                                System.out.println("Finally Action will be done!");
                                showLoadSuccess();
                            }
                        })
                        .subscribe(observer);
            }

            @Override
            public void httpMap(String url, DisposableObserver observer) {
                GlobalCode.printLog("get_url>>>" + url);
                ApiEngine.getInstance().getApiService().doGetRequest(url)
                        .compose(RxSchedulers.<ResponseBody>io2main())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                addDispoable(disposable);
                                showLoading();
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() {
                                showLoadSuccess();
                            }
                        })
                        .subscribe(observer);
            }

            @Override
            public void httpMap(String url, String paramsJson, DisposableObserver observer) {
                MediaType type = MediaType.parse("");
                RequestBody body=RequestBody.create(type, paramsJson);
                ApiEngine.getInstance().getApiService().doPostRequest(url,body)
                        .compose(RxSchedulers.<ResponseBody>io2main())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                addDispoable(disposable);
                                showLoading();
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                showLoadSuccess();
                            }
                        }).subscribe(observer);
            }
        };

    }

    //外部手动调用
    public void doHttpRequest() {

    }


    //外部手动调用  参数是自定义实体
    public void doRequestByObj(Object ojb) {

    }

    public void showLoading() {
        mView.getLoading().showLoading();
    }

    public void showLoadSuccess() {
        mView.getLoading().showLoadSuccess();
    }

    public void showLoadFailed() {
        mView.getLoading().showLoadFailed();
    }

    public void showEmpty() {
        mView.getLoading().showEmpty();
    }


}
