package com.reikyz.jandan.async;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.reikyz.jandan.widget.LMListView;


public abstract class ResponseSimpleNetTask extends ResponseNetAsyncTask {

    protected ResponseSimpleNetTask(Context cxt) {
        super(cxt);
    }

    protected ResponseSimpleNetTask(Context cxt, boolean openDialog) {
        super(cxt, openDialog);
    }

    protected ResponseSimpleNetTask(Context cxt, boolean openDialog, SwipeRefreshLayout swipeRefreshLayout) {
        super(cxt, openDialog, swipeRefreshLayout);
    }

    protected ResponseSimpleNetTask(Context ctx, boolean openDialog, LMListView listView) {
        super(ctx, openDialog, listView);
    }

    protected ResponseSimpleNetTask(Context cxt, boolean openDialog, boolean showErrorMsg) {
        super(cxt, openDialog, showErrorMsg);
    }

    protected ResponseSimpleNetTask(Context cxt, boolean openDialog, SwipeRefreshLayout swipeRefreshLayout, boolean showErrorMsg) {
        super(cxt, openDialog, swipeRefreshLayout, showErrorMsg);
    }

    protected ResponseSimpleNetTask(Context ctx, boolean openDialog, LMListView listView, boolean showErrorMsg) {
        super(ctx, openDialog, listView, showErrorMsg);
    }

    @Override
    protected void onPost(String result) {
        try {
            onSucceed(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFailure() {

    }

    protected abstract void onSucceed(String result) throws Exception;
}
