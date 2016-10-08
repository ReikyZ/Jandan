package com.reikyz.jandan.mvp.fragment.flow.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by reikyZ on 16/10/8.
 */

public interface FlowPresenter {

    void initFlow();

    void loadData(Context context, SwipeRefreshLayout refreshLayout, Integer page, String type);

    void getComment(Context context, String threads);

    void detachView();
}
