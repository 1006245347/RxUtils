package com.rxutils.jason.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jason on 18/6/1.
 */

public abstract class BasePresenterImpl<V extends BaseView> implements BasePresenter {
    public BasePresenterImpl(V view) {
        this.mView = view;
        start();
    }

    protected V mView;

    @Override
    public void detach() {
        this.mView = null;
        unDisposable();
    }


    @Override
    public void start() {

    }

    private CompositeDisposable compositeDisposable;

    @Override
    public void addDispoable(Disposable subscription) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(subscription);
    }


    @Override
    public void unDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

}
