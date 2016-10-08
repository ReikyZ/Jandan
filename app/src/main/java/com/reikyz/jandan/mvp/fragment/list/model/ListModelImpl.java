package com.reikyz.jandan.mvp.fragment.list.model;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;

import org.json.JSONException;

/**
 * Created by reikyZ on 16/10/9.
 */

public class ListModelImpl implements ListModel {


    @Override
    public void loadListNews(final Context context, final SwipeRefreshLayout refreshLayout, final int page, final OnLoadNewsListener listener) {
        new ResponseSimpleNetTask(context, false, refreshLayout) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return MyApp.api.generalApi(Config.API_GET_NEWS,
                        "url,date,tags,author,title,comment_count,custom_fields",
                        page,
                        "thumb_c,views",
                        1,
                        null);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                listener.onNewsSuccess(result);
            }

            @Override
            protected void onFailure() {
                listener.onNewsFailure();
            }
        }.execute();
    }

    @Override
    public void loadListJoke(final Context context, final SwipeRefreshLayout refreshLayout, final int page, final OnLoadJokeListener listener) {
        new ResponseSimpleNetTask(context, false, refreshLayout) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return MyApp.api.generalApi(Config.API_GET_JOKES, null, page, null, 1, null);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                listener.onJokeSuccess(result);
            }

            @Override
            protected void onFailure() {
                listener.onJokeFailure();
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

    public interface OnLoadNewsListener {
        void onNewsSuccess(String string) throws JSONException;

        void onNewsFailure();
    }

    public interface OnLoadJokeListener {
        void onJokeSuccess(String string) throws JSONException;

        void onJokeFailure();
    }

    public interface OnDataCommentListener {
        void onCommentSuccess(String string) throws JSONException;

        void onCommentFailure(String threads);
    }
}
