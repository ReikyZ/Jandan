package com.reikyz.jandan.mvp.fragment.list.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by reikyZ on 16/10/9.
 */

public interface ListPresenter {
    void initList();

    void loadNews(Context context, SwipeRefreshLayout refreshLayout, Integer page);

    void loadJoke(Context context, SwipeRefreshLayout refreshLayout, Integer page);

    void getComment(Context context, String threads);

    void detachView();
}
