package com.reikyz.jandan.mvp.fragment.list.model;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by reikyZ on 16/10/9.
 */

public interface ListModel {

    void loadListNews(Context context, SwipeRefreshLayout refreshLayout, int page, ListModelImpl.OnLoadNewsListener listener);

    void loadListJoke(Context context, SwipeRefreshLayout refreshLayout, int page, ListModelImpl.OnLoadJokeListener listener);

    void loadDataComment(Context context, String threads, ListModelImpl.OnDataCommentListener listener);
}
