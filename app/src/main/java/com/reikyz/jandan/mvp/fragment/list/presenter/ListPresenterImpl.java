package com.reikyz.jandan.mvp.fragment.list.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.reikyz.jandan.mvp.fragment.list.model.ListModelImpl;
import com.reikyz.jandan.mvp.fragment.list.view.ListView;

import org.json.JSONException;

/**
 * Created by reikyZ on 16/10/9.
 */

public class ListPresenterImpl implements ListPresenter, ListModelImpl.OnLoadNewsListener, ListModelImpl.OnLoadJokeListener, ListModelImpl.OnDataCommentListener {

    private ListView mListView;
    private ListModelImpl mListModel;

    public ListPresenterImpl(ListView listView) {
        mListView = listView;
        mListModel = new ListModelImpl();
    }

    @Override
    public void initList() {
        mListView.initList();
    }

    @Override
    public void loadNews(Context context, SwipeRefreshLayout refreshLayout, Integer page) {
        mListModel.loadListNews(context, refreshLayout, page, this);
    }

    @Override
    public void loadJoke(Context context, SwipeRefreshLayout refreshLayout, Integer page) {
        mListModel.loadListJoke(context, refreshLayout, page, this);
    }


    @Override
    public void getComment(Context context, String threads) {
        mListModel.loadDataComment(context, threads, this);
    }


    @Override
    public void onNewsSuccess(String string) throws JSONException {
        mListView.handleNews(string);
    }

    @Override
    public void onNewsFailure() {
        mListView.onNewsFailure();
    }

    @Override
    public void onJokeSuccess(String string) throws JSONException {
        mListView.handleJoke(string);
    }

    @Override
    public void onJokeFailure() {
        mListView.onJokeFailure();
    }

    @Override
    public void onCommentSuccess(String string) throws JSONException {
        mListView.handleComment(string);
    }

    @Override
    public void onCommentFailure(String threads) {
        mListView.onCommentFailure(threads);
    }


    @Override
    public void detachView() {
        if (mListView != null)
            mListView = null;
        if (mListModel != null)
            mListModel = null;
    }
}
