package com.reikyz.jandan.mvp;

import com.reikyz.jandan.mvp.BasePresenter;
import com.reikyz.jandan.mvp.BaseView;

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
