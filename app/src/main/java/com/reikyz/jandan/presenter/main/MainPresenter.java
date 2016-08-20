package com.reikyz.jandan.presenter.main;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reikyz.jandan.utils.Utils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.fabric.sdk.android.services.concurrency.Task;

/**
 * Created by reikyZ on 16/8/17.
 */
public class MainPresenter implements MainContract.Presenter {

    final static String TAG = "==MainPresenter==";

    Reference<Activity> mViewRef;

    @Override
    public void attachView(Object view) {
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
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadTasks(boolean forceUpdate) {

    }

    @Override
    public void addNewTask() {

    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {

    }

    @Override
    public void completeTask(@NonNull Task completedTask) {

    }

    @Override
    public void activateTask(@NonNull Task activeTask) {

    }

    @Override
    public void clearCompletedTasks() {

    }

}
