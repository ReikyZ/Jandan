package com.reikyz.jandan.mvp.fragment.list.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.alibaba.fastjson.JSON;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.JsonUtils;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.adapter.JokeAdapter;
import com.reikyz.jandan.adapter.NewsAdapter;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.model.CommentThreadModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.model.NewsModel;
import com.reikyz.jandan.mvp.base.BaseFragment;
import com.reikyz.jandan.mvp.fragment.list.presenter.ListPresenter;
import com.reikyz.jandan.mvp.fragment.list.presenter.ListPresenterImpl;
import com.reikyz.jandan.utils.ShakeListener;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.LMListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by reikyZ on 16/8/23.
 */
public class ListFragment extends BaseFragment implements LMListView.DataChangeListener, AbsListView.OnScrollListener, ShakeListener.OnShakeListener, ListView {

    final static String TAG = "ListFragment";

    Bundle mBundle;
    String mType;
    Integer mPage = 1;
    boolean isFirstIn = true;
    boolean getMore = true;
    boolean fetchingNews = false;
    boolean fetchingJoke = false;

    // Adapter
    NewsAdapter newsAdapter;
    JokeAdapter jokeAdapter;
    List<GeneralPostModel> tmpList = new ArrayList<>();
    ListPresenter mListPresenter;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.lv)
    LMListView lv;

    public static Fragment newInstance(String type) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Config.TYPE, type);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        mType = mBundle.getString(Config.TYPE);

        mListPresenter = new ListPresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_listview, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        mListPresenter.initList();

        return view;
    }

    ShakeListener shakeListener;

    @Override
    public void onResume() {
        super.onResume();
        registerShakeListener();

        lv.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case Config.FRAGMENT_NEWS:
                        if (MyApp.currentNewsIndex != null) {
                            lv.smoothScrollToPosition(MyApp.currentNewsIndex);
                            newsAdapter.notifyDataSetChanged();
                        }
                        break;
                    case Config.FRAGMENT_JOKE:
                        if (MyApp.currentJokeIndex != null) {
                            lv.smoothScrollToPosition(MyApp.currentJokeIndex);
                            jokeAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }, 200);
    }

    private void getNews(final Context context, final SwipeRefreshLayout refreshLayout, final Integer page) {
        fetchingNews = true;
        lv.loading();
        mListPresenter.loadNews(context, refreshLayout, page);
    }


    private void getJoke(final Context context, final SwipeRefreshLayout refreshLayout, final Integer page) {
        fetchingJoke = true;
        lv.loading();

        mListPresenter.loadJoke(context, refreshLayout, page);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (lv.getChildAt(0) != null) {
            if (firstVisibleItem == 0 && lv.getChildAt(0).getTop() == 0) {
                lv.setDataChangeListener(this);
                refreshLayout.setEnabled(true);
            } else {
                lv.setDataChangeListener(null);
                refreshLayout.setEnabled(false);
            }
            switch (mType) {
                case Config.FRAGMENT_NEWS:
                    if (firstVisibleItem + visibleItemCount > MyApp.newsList.size() - 3) {
                        lv.setDataChangeListener(this);
                        if (getMore && !isFirstIn) {
                            getMore = false;
                            loadMore();
                        }
                    }
                    break;
                case Config.FRAGMENT_JOKE:
                    if (firstVisibleItem + visibleItemCount > MyApp.jokeList.size() - 3) {
                        lv.setDataChangeListener(this);
                        if (getMore && !isFirstIn) {
                            getMore = false;
                            loadMore();
                        }
                    }
                    break;
            }
        }
    }

    @Subscriber(tag = EventConfig.LOAD_MORE_LIST)
    void notifyLoadMore(int i) {
        loadMore();
    }

    @Subscriber(tag = EventConfig.REFRESH_JOKE_LIST)
    void refreshJokeList(int i) {
        switch (mType) {
            case Config.FRAGMENT_NEWS:
                break;
            case Config.FRAGMENT_JOKE:
                jokeAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void refresh() {
        mPage = 1;
        switch (mType) {
            case Config.FRAGMENT_NEWS:
                if (!fetchingNews)
                    getNews(getActivity(), refreshLayout, mPage);
                break;
            case Config.FRAGMENT_JOKE:
                if (!fetchingJoke)
                    getJoke(getActivity(), refreshLayout, mPage);
                break;
        }
    }

    @Override
    public void loadMore() {
        switch (mType) {
            case Config.FRAGMENT_NEWS:
                if (!fetchingNews)
                    getNews(getActivity(), refreshLayout, mPage);
                break;
            case Config.FRAGMENT_JOKE:
                if (!fetchingJoke)
                    getJoke(getActivity(), refreshLayout, mPage);
                break;
        }
    }

    boolean shakeActive = true;

    @Override
    public void onShake() {
        if (shakeActive) {
            shakeActive = false;
            lv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lv.smoothScrollToPosition(0);
                    refresh();
                }
            }, 200);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MyApp.currentNewsIndex = null;
            MyApp.currentJokeIndex = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterShakeListener();
        mListPresenter.detachView();
    }

    @Override
    public void initList() {
        lv.setDivider(null);
        lv.setRefreshLayout(refreshLayout);
        lv.setDataChangeListener(this);
        lv.setOnScrollListener(this);
        lv.loading();

        String results;
        switch (mType) {
            case Config.FRAGMENT_NEWS:
                MyApp.currentNewsIndex = null;
                newsAdapter = new NewsAdapter(getActivity(), MyApp.newsList);
                lv.setAdapter(newsAdapter);

                results = Prefs.getString(Config.PRELOAD_NEWS);
                if (results != null) {
                    List<NewsModel> tmpNewsList = JSON.parseArray(results, NewsModel.class);
                    newsAdapter.refreshItems(tmpNewsList);
                }
                if (!fetchingNews)
                    getNews(getActivity(), refreshLayout, mPage);
                break;
            case Config.FRAGMENT_JOKE:
                MyApp.currentJokeIndex = null;
                jokeAdapter = new JokeAdapter(getActivity(), MyApp.jokeList);
                lv.setAdapter(jokeAdapter);

                results = Prefs.getString(Config.PRELOAD_JOKE);
                if (results != null) {
                    List<GeneralPostModel> tmpNewsList = JSON.parseArray(results, GeneralPostModel.class);
                    jokeAdapter.refreshItems(tmpNewsList);
                }
                if (!fetchingJoke)
                    getJoke(getActivity(), refreshLayout, mPage);
                break;
        }
    }

    private void registerShakeListener() {
        shakeListener = new ShakeListener(getActivity());
        shakeListener.setOnShakeListener(this);
    }

    private void unregisterShakeListener() {
        shakeListener.setOnShakeListener(null);
    }


    @Override
    public void handleNews(String result) throws JSONException {
        String results = JsonUtils.getString(new JSONObject(result), "posts");
        List<NewsModel> tmpNewsList = JSON.parseArray(results, NewsModel.class);

        if (tmpNewsList.size() > 0) {
            if (mPage == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (NewsModel newsModel : tmpNewsList) {
                    if (sb.length() > 10)
                        sb.append(",");
                    sb.append(JSON.toJSONString(newsModel));
                }
                sb.append("]");
                Prefs.save(Config.PRELOAD_NEWS, sb.toString());
                newsAdapter.refreshItems(tmpNewsList);
            } else {
                newsAdapter.addMoreItem(tmpNewsList);
            }
            EventBus.getDefault().post(0, EventConfig.NEWS_CHANGED);
            lv.loadComplete();
            getMore = true;
        } else {
            lv.noMore();
        }

        lv.loadComplete();
        mPage++;

        isFirstIn = false;
        shakeActive = true;
        fetchingNews = false;
    }

    @Override
    public void onNewsFailure() {
        getNews(getActivity(), refreshLayout, mPage);
    }

    @Override
    public void handleJoke(String result) throws JSONException {
        String results = JsonUtils.getString(new JSONObject(result), "comments");
        tmpList = JSON.parseArray(results, GeneralPostModel.class);

        StringBuilder sb = new StringBuilder();
        if (tmpList.size() > 0)
            sb.append("comment-" + tmpList.get(0).getComment_ID());

        for (int i = 1; i < tmpList.size(); i++) {
            sb.append(",");
            sb.append("comment-" + tmpList.get(i).getComment_ID());
        }
        mListPresenter.getComment(getActivity(), sb.toString());
    }

    @Override
    public void onJokeFailure() {
        getJoke(getActivity(), refreshLayout, mPage);
    }


    @Override
    public void handleComment(String result) throws JSONException {
        String results = JsonUtils.getString(new JSONObject(result), "response");

        for (GeneralPostModel model : tmpList) {
            String threadStr = JsonUtils.getString(new JSONObject(results), "comment-" + model.getComment_ID());
            CommentThreadModel thread = JSON.parseObject(threadStr, CommentThreadModel.class);
            model.setThread(thread);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (GeneralPostModel post : tmpList) {
            if (sb.length() > 10)
                sb.append(",");
            sb.append(JSON.toJSONString(post));
        }
        sb.append("]");
        if (tmpList.size() > 0) {
            if (mPage == 1) {
                Prefs.save(Config.PRELOAD_JOKE, sb.toString());
                jokeAdapter.refreshItems(tmpList);
            } else {
                jokeAdapter.addMoreItem(tmpList);
            }
            EventBus.getDefault().post(0, EventConfig.DATA_CHANGED);
            lv.loadComplete();
            getMore = true;
        } else {
            lv.noMore();
        }
        lv.loadComplete();
        mPage++;
        isFirstIn = false;
        shakeActive = true;
        fetchingJoke = false;
    }

    @Override
    public void onCommentFailure(String threads) {
        mListPresenter.getComment(getActivity(), threads);
    }
}
