package com.reikyz.jandan.presenter.main;

import android.support.annotation.NonNull;

import com.reikyz.jandan.presenter.BasePresenter;
import com.reikyz.jandan.presenter.BaseView;

import java.util.List;

import io.fabric.sdk.android.services.concurrency.Task;

/**
 * Created by reikyZ on 16/8/17.
 */
public abstract class MainContract {

    interface View extends BaseView<Presenter> {

        void initFragment();
    }

    interface Presenter extends BasePresenter {


        void getData();

    }
}
