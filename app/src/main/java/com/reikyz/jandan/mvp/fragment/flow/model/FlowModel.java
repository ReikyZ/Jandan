package com.reikyz.jandan.mvp.fragment.flow.model;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by reikyZ on 16/10/8.
 */

public interface FlowModel {

    void loadFlowData(Context context, SwipeRefreshLayout refreshLayout, int page, String type, FlowModelImpl.OnLoadFlowDataListener listener);

    void loadDataComment(Context context, String threads, FlowModelImpl.OnDataCommentListener listener);
}
