package com.reikyz.jandan.mvp.fragment.flow.model;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;

import org.json.JSONException;

/**
 * Created by reikyZ on 16/10/8.
 */

public class FlowModelImpl implements FlowModel {


    @Override
    public void loadFlowData(final Context context, final SwipeRefreshLayout refreshLayout, final int page, final String type, final OnLoadFlowDataListener listener) {
        new ResponseSimpleNetTask(context, false, refreshLayout) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                switch (type) {
                    case Config.FUN_PIC:
                        return MyApp.api.generalApi(
                                Config.API_GET_FUN_PICS, null, page, null, null, null);
                    case Config.GIRL_PIC:
                        return MyApp.api.generalApi(
                                Config.API_GET_GIRL_PIS, null, page, null, null, null);
                    case Config.VIDEO:
                        return MyApp.api.generalApi(
                                Config.API_GET_VIDEOS, null, page, null, null, null);

                }
                return null;
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                listener.onDataSuccess(result);
            }

            @Override
            protected void onFailure() {
                listener.onDataFailure();
            }
        }.execute();
    }

    @Override
    public void loadDataComment(Context context, final String threads, final OnDataCommentListener listener) {
        new ResponseSimpleNetTask(context, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return MyApp.api.getDuoshuo(threads);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                listener.onCommentSuccess(result);
            }

            @Override
            protected void onFailure() {
                listener.onCommentFailure(threads);
            }
        }.execute();
    }

    public interface OnLoadFlowDataListener {
        void onDataSuccess(String string) throws JSONException;

        void onDataFailure();
    }

    public interface OnDataCommentListener {
        void onCommentSuccess(String string) throws JSONException;

        void onCommentFailure(String threads);
    }
}
