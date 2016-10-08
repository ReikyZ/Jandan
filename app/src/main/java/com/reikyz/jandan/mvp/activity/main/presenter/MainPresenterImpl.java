package com.reikyz.jandan.mvp.activity.main.presenter;

import android.os.Bundle;

import com.reikyz.jandan.mvp.activity.main.view.MainView;

/**
 * Created by reikyZ on 16/8/17.
 */
public class MainPresenterImpl implements MainPresenter {

    final static String TAG = "==MainPresenterImpl==";

    private MainView mMainView;

    public MainPresenterImpl(MainView mainView) {
        this.mMainView = mainView;
    }

    @Override
    public void setSelect(int id) {
        mMainView.setSelect(id);
    }

    @Override
    public void initFragment(Bundle savedInstanceState) {
        mMainView.initFragment(savedInstanceState);
    }
}
