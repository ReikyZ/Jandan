package com.reikyz.jandan.mvp.fragment.list.view;

import org.json.JSONException;

/**
 * Created by reikyZ on 16/10/9.
 */

public interface ListView {
    void initList();

    void handleNews(String result) throws JSONException;

    void onNewsFailure();

    void handleJoke(String result) throws JSONException;

    void onJokeFailure();

    void handleComment(String result) throws JSONException;

    void onCommentFailure(String threads);
}
