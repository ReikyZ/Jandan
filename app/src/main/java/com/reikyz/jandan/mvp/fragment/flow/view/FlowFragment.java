package com.reikyz.jandan.mvp.fragment.flow.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.reikyz.api.utils.JsonUtils;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.adapter.PicFlowAdapter;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.model.CommentThreadModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.mvp.base.BaseFragment;
import com.reikyz.jandan.mvp.fragment.flow.presenter.FlowPresenter;
import com.reikyz.jandan.mvp.fragment.flow.presenter.FlowPresenterImpl;
import com.reikyz.jandan.utils.ShakeListener;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.LMRecycleView;

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
public class FlowFragment extends BaseFragment implements LMRecycleView.DataChangeListener, ShakeListener.OnShakeListener, FlowView {

    final String TAG = "==FlowFragment==";

    Bundle mBundle;
    String mType;
    Integer mPage = 1;
    boolean isFirstIn = true;
    boolean getMore = true;
    boolean fetching = false;

    private DisplayMetrics metrics;
    // Adapter
    PicFlowAdapter adapter;
    List<GeneralPostModel> tmpList = new ArrayList<>();
    FlowPresenter mFlowPresenter;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.lv)
    LMRecycleView recyclerView;

    public static Fragment newInstance(String type) {
        FlowFragment listFragment = new FlowFragment();
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

        mFlowPresenter = new FlowPresenterImpl(this);
    }

    String preLoadStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flow, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        mFlowPresenter.initFlow();
        return view;
    }

    @Override
    public void initFlow() {
        StaggeredGridLayoutManager sglm;

        switch (mType) {
            case Config.FUN_PIC:
                sglm = new StaggeredGridLayoutManager(
                        Prefs.getInt(Config.FUN_PIC_COL_NUM, 2),
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(sglm);

                adapter = new PicFlowAdapter(getActivity(), MyApp.funPicList, refreshLayout, mType);

                preLoadStr = Prefs.getString(Config.PRELOAD_FUN_PIC);
                if (preLoadStr != null) {
                    List<GeneralPostModel> tmpNewsList = JSON.parseArray(preLoadStr, GeneralPostModel.class);
                    adapter.refreshItems(tmpNewsList, getMore);
                }
                break;
            case Config.GIRL_PIC:

                sglm = new StaggeredGridLayoutManager(
                        Prefs.getInt(Config.GIRL_PIC_COL_NUM, 2),
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(sglm);

                adapter = new PicFlowAdapter(getActivity(), MyApp.girlPicLIst, refreshLayout, mType);

                preLoadStr = Prefs.getString(Config.PRELOAD_GIRL_PIC);

                if (preLoadStr != null) {
                    List<GeneralPostModel> tmpNewsList = JSON.parseArray(preLoadStr, GeneralPostModel.class);
                    adapter.refreshItems(tmpNewsList, getMore);
                }
                break;
            case Config.VIDEO:
                sglm = new StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(sglm);

                adapter = new PicFlowAdapter(getActivity(), MyApp.videoLIst, refreshLayout, mType);
                break;
        }

        recyclerView.setAdapter(adapter);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recyclerView.setRefreshLayout(refreshLayout);
        recyclerView.setDataChangeListener(this);
        recyclerView.setFocusable(false);
        refreshLayout.setEnabled(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!fetching)
            getData(getActivity(), refreshLayout, mPage);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerShakeListener();
        switch (mType) {
            case Config.FUN_PIC:
                MyApp.updateFunCilently = true;
                break;
            case Config.GIRL_PIC:
                MyApp.updateGirlCilently = true;
                break;
        }

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case Config.FUN_PIC:
                        if (MyApp.currentFunPicIndex != null) {
                            recyclerView.smoothScrollToPosition(MyApp.currentFunPicIndex);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case Config.GIRL_PIC:
                        if (MyApp.currentGirlPicIndex != null) {
                            recyclerView.smoothScrollToPosition(MyApp.currentGirlPicIndex);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }, 200);
    }

    private void getData(final Context context, final SwipeRefreshLayout refreshLayout, final Integer page) {
        fetching = true;
        recyclerView.setLoading(true);

        mFlowPresenter.loadData(context, refreshLayout, page, mType);
    }

    private void filterEmptyVideo() {
        if (mType.equals(Config.VIDEO)) {
            for (int i = tmpList.size() - 1; i >= 0; i--) {
                GeneralPostModel video = tmpList.get(i);
                if (video.getVideos().size() == 0) {
                    tmpList.remove(video);
                }
            }
        }
    }

    @Subscriber(tag = EventConfig.LOAD_MORE_FLOW)
    void notifyLoadMore(int i) {
        Utils.log(TAG, Utils.getLineNumber(new Exception()));
        loadMore();
    }

    @Subscriber(tag = EventConfig.REFRESH_FLOW)
    void refreshFlow(int i) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        mPage = 1;
        if (!fetching)
            getData(getActivity(), refreshLayout, mPage);
    }

    @Override
    public void loadMore() {
        if (!fetching)
            getData(getActivity(), refreshLayout, mPage);
    }


    @Override
    public void onShake() {
        if (shakeActive) {
            shakeActive = false;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                    refresh();
                }
            }, 100);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        switch (mType) {
            case Config.FUN_PIC:
                MyApp.updateFunCilently = false;
                break;
            case Config.GIRL_PIC:
                MyApp.updateGirlCilently = false;
                break;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MyApp.currentFunPicIndex = null;
            MyApp.currentGirlPicIndex = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterShakeListener();
        mFlowPresenter.detachView();
    }

    boolean shakeActive = true;
    ShakeListener shakeListener;

    public void registerShakeListener() {
        shakeListener = new ShakeListener(getActivity());
        shakeListener.setOnShakeListener(this);
    }

    public void unregisterShakeListener() {
        shakeListener.setOnShakeListener(null);
    }

    @Override
    public void isLoadingFirst() {
        EventBus.getDefault().post(View.VISIBLE, EventConfig.SHOW_GLOBAL_PRO_TOP);
    }

    @Override
    public void loadFirstFinish() {
        EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO_TOP);
    }

    @Override
    public void isLoadingMore() {
        EventBus.getDefault().post(View.VISIBLE, EventConfig.SHOW_GLOBAL_PRO);
    }

    @Override
    public void loadMoreFinish() {
        EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO);
    }

    @Override
    public void handleData(String result) throws JSONException {
        String results = JsonUtils.getString(new JSONObject(result), "comments");
        tmpList = JSON.parseArray(results, GeneralPostModel.class);

        StringBuilder sb = new StringBuilder();
        if (tmpList.size() > 0)
            sb.append("comment-" + tmpList.get(0).getComment_ID());

        for (int i = 1; i < tmpList.size(); i++) {
            sb.append(",");
            sb.append("comment-" + tmpList.get(i).getComment_ID());
        }
        mFlowPresenter.getComment(getActivity(), sb.toString());
    }

    @Override
    public void onDataFailure() {
        getData(getActivity(), refreshLayout, mPage);
    }

    @Override
    public void handleComment(String result) throws JSONException {
        String results = JsonUtils.getString(new JSONObject(result), "response");

        for (GeneralPostModel model : tmpList) {
            String threadStr = JsonUtils.getString(new JSONObject(results), "comment-" + model.getComment_ID());
            CommentThreadModel thread = JSON.parseObject(threadStr, CommentThreadModel.class);
            model.setThread(thread);
        }

        if (tmpList.size() > 0) {
            if (mPage == 1) {
                filterEmptyVideo();
                adapter.refreshItems(tmpList, getMore);

                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (GeneralPostModel post : tmpList) {
                    if (sb.length() > 10)
                        sb.append(",");
                    sb.append(JSON.toJSONString(post));
                }
                sb.append("]");
                switch (mType) {
                    case Config.FUN_PIC:
                        Prefs.save(Config.PRELOAD_FUN_PIC, sb.toString());
                        break;
                    case Config.GIRL_PIC:
                        Prefs.save(Config.PRELOAD_GIRL_PIC, sb.toString());
                        break;
                }
            } else {
                filterEmptyVideo();
                adapter.addMoreItem(tmpList, getMore);
            }
            EventBus.getDefault().post(0, EventConfig.PICS_CHANGED);
            getMore = true;
        }

        mPage++;

        isFirstIn = false;
        shakeActive = true;
        recyclerView.setLoading(false);
        loadFirstFinish();
        loadMoreFinish();
        fetching = false;
    }

    @Override
    public void onCommentFailure(String threads) {
        mFlowPresenter.getComment(getActivity(), threads);
    }

}
