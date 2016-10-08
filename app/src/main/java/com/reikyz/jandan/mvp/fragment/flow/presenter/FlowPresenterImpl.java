package com.reikyz.jandan.mvp.fragment.flow.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.reikyz.jandan.mvp.fragment.flow.model.FlowModelImpl;
import com.reikyz.jandan.mvp.fragment.flow.view.FlowView;

import org.json.JSONException;

/**
 * Created by reikyZ on 16/10/8.
 */

public class FlowPresenterImpl implements FlowPresenter, FlowModelImpl.OnLoadFlowDataListener, FlowModelImpl.OnDataCommentListener {

    private FlowView mFlowView;
    private FlowModelImpl mFlowModel;

    public FlowPresenterImpl(FlowView flowView) {
        mFlowView = flowView;
        mFlowModel = new FlowModelImpl();
    }


    @Override
    public void initFlow() {
        mFlowView.initFlow();
    }

    @Override
    public void loadData(Context context, SwipeRefreshLayout refreshLayout, Integer page, String type) {
        if (page == 1)
            mFlowView.isLoadingFirst();
        else
            mFlowView.isLoadingMore();

        mFlowModel.loadFlowData(context, refreshLayout, page, type, this);
    }

    @Override
    public void onDataSuccess(String string) throws JSONException {
        mFlowView.handleData(string);
    }

    @Override
    public void onDataFailure() {
        mFlowView.onDataFailure();
    }

    @Override
    public void getComment(Context context, String threads) {

        mFlowModel.loadDataComment(context, threads, this);
    }

    @Override
    public void onCommentSuccess(String string) throws JSONException {
        mFlowView.handleComment(string);
    }

    @Override
    public void onCommentFailure(String threads) {
        mFlowView.onCommentFailure(threads);
    }

    @Override
    public void detachView() {
        if (mFlowView != null)
            mFlowView = null;
        if (mFlowModel != null)
            mFlowModel = null;
    }
}
