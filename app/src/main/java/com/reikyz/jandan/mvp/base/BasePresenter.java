package com.reikyz.jandan.mvp.base;

/**
 * Created by reikyZ on 16/8/17.
 */
public interface BasePresenter<T> {

    void attachView(T view);

    T getView();

    boolean isViewAttached();

    void detachView();


}
