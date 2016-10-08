package com.reikyz.jandan.mvp.fragment.flow.view;

import org.json.JSONException;

/**
 * Created by reikyZ on 16/10/8.
 */

public interface FlowView {

    void initFlow();

    void isLoadingFirst();

    void loadFirstFinish();

    void isLoadingMore();

    void loadMoreFinish();

    void handleData(String result) throws JSONException;

    void onDataFailure();

    void handleComment(String result) throws JSONException;

    void onCommentFailure(String threads);
}
