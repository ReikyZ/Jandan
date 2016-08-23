package com.reikyz.jandan.presenter.listfragment;

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
import com.reikyz.jandan.model.JokeModel;
import com.reikyz.jandan.model.NewsModel;
import com.reikyz.jandan.presenter.BaseFragment;
import com.reikyz.jandan.utils.ShakeListener;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.LMListView;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by reikyZ on 16/8/23.
 */
public class ListFragment extends BaseFragment implements LMListView.DataChangeListener, AbsListView.OnScrollListener, ShakeListener.OnShakeListener {

    final static String TAG = "ListFragment";

    Bundle mBundle;
    String mType;
    Integer mPage = 0;
    boolean isFirstIn = true;
    boolean getMore = true;
    // DATA

    NewsAdapter newsAdapter;

    List<JokeModel> jokeList = new ArrayList<>();
    JokeAdapter jokeAdapter;


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

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_listview, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        lv.setDivider(null);
        lv.setRefreshLayout(refreshLayout);
        lv.setDataChangeListener(this);
        lv.setOnScrollListener(this);
        lv.loading();


        switch (mType) {
            case Config.FRAGMENT_NEWS:
                newsAdapter = new NewsAdapter(getActivity(), MyApp.newsList);
                lv.setAdapter(newsAdapter);
                getNews(mPage);
                break;
            case Config.FRAGMENT_JOKE:
                jokeAdapter = new JokeAdapter(getActivity(), jokeList);
                lv.setAdapter(jokeAdapter);
                getJoke(mPage);
                break;
        }

        return view;
    }

    ShakeListener shakeListener;

    @Override
    public void onResume() {
        super.onResume();
        shakeListener = new ShakeListener(getActivity());
        shakeListener.setOnShakeListener(this);

        Utils.log(TAG, "refresh==" + Utils.getLineNumber(new Exception()));
        if (MyApp.currentIndex != null) {
            lv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lv.smoothScrollToPosition(MyApp.currentIndex);
                    Utils.log(TAG, "refresh==" + Utils.getLineNumber(new Exception()));
                }
            }, 200);
        }
    }

    private void getNews(final Integer page) {
        lv.loading();
        new ResponseSimpleNetTask(getActivity(), false, refreshLayout) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.generalApi(Config.API_GET_NEWS,
                        "url,date,tags,author,title,comment_count,custom_fields",
                        page,
                        "thumb_c,views",
                        1,
                        null);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                String results = JsonUtils.getString(new JSONObject(result), "posts");
                List<NewsModel> tmpList = JSON.parseArray(results, NewsModel.class);

                if (tmpList.size() > 0) {
                    if (page == 1) {
                        newsAdapter.refreshItems(tmpList);
                    } else {
                        newsAdapter.addMoreItem(tmpList);
                    }
                    lv.loadComplete();
                    getMore = true;
                } else {
                    lv.noMore();
                }

                lv.loadComplete();
                mPage++;

                isFirstIn = false;
                shakeActive = true;
            }
        }.execute();
    }


    private void getJoke(final Integer page) {
        lv.loading();
        new ResponseSimpleNetTask(getActivity(), false, refreshLayout) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.generalApi(Config.API_GET_JOKES,
                        null,
                        page,
                        null,
                        1,
                        null);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                String results = JsonUtils.getString(new JSONObject(result), "comments");
                List<JokeModel> tmpList = JSON.parseArray(results, JokeModel.class);

                if (tmpList.size() > 0) {
                    if (page == 1) {
                        jokeAdapter.refreshItems(tmpList);
                    } else {
                        jokeAdapter.addMoreItem(tmpList);
                    }
                    lv.loadComplete();
                    getMore = true;
                } else {
                    lv.noMore();
                }

                lv.loadComplete();
                mPage++;

                isFirstIn = false;
                shakeActive = true;
            }
        }.execute();
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
                    if (firstVisibleItem + visibleItemCount > MyApp.newsList.size()) {
                        lv.setDataChangeListener(this);
                        if (getMore && !isFirstIn) {
                            getMore = false;
                            loadMore();
                        }
                    }
                    break;
                case Config.FRAGMENT_JOKE:
                    if (firstVisibleItem + visibleItemCount > jokeList.size()) {
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


    @Override
    public void refresh() {

        mPage = 1;
        switch (mType) {
            case Config.FRAGMENT_NEWS:
                getNews(mPage);
                break;
            case Config.FRAGMENT_JOKE:
                getJoke(mPage);
                break;
        }
    }

    @Override
    public void loadMore() {
        Utils.log(TAG, "loadMore==" + Utils.getLineNumber(new Exception()));
        switch (mType) {
            case Config.FRAGMENT_NEWS:
                getNews(mPage);
                break;
            case Config.FRAGMENT_JOKE:
                getJoke(mPage);
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
    public void onDestroy() {
        super.onDestroy();
        shakeListener.setOnShakeListener(null);
        MyApp.currentIndex = null;
    }
}
