package com.reikyz.jandan.presenter.main;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reikyz.jandan.utils.Utils;

import org.simple.eventbus.EventBus;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import io.fabric.sdk.android.services.concurrency.Task;

/**
 * Created by reikyZ on 16/8/17.
 */
public class MainPresenter implements MainContract.Presenter {

    final static String TAG = "==MainPresenter==";

    Reference<Activity> mViewRef;
    Activity mActivity;

    MainContract.View mMainView;




    @Override
    public void attachView(Object view) {
        mActivity = (Activity) view;

        ButterKnife.bind(mActivity);
        EventBus.getDefault().register(mActivity);


        mViewRef = new WeakReference<Activity>((Activity) view);

        Utils.log(TAG, "attachView");
    }

    @Override
    public Object getView() {
        return mViewRef;
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        ButterKnife.unbind(mActivity);
        EventBus.getDefault().unregister(mActivity);

        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        mActivity = null;
    }

    @Override
    public void getData() {

    }
}
