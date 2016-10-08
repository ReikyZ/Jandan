package com.reikyz.jandan.mvp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.JsonUtils;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.adapter.PicFlowAdapter;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.model.CommentThreadModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.mvp.base.BaseFragment;
import com.reikyz.jandan.utils.ShakeListener;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.LMRecycleView;

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
public class FlowFragment extends BaseFragment implements LMRecycleView.DataChangeListener, ShakeListener.OnShakeListener {

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

    }

    String results;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flow, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        StaggeredGridLayoutManager sglm;

        switch (mType) {
            case Config.FUN_PIC:
                sglm = new StaggeredGridLayoutManager(
                        Prefs.getInt(Config.FUN_PIC_COL_NUM, 2),
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(sglm);

                adapter = new PicFlowAdapter(getActivity(), MyApp.funPicList, refreshLayout, mType);

                results = Prefs.getString(Config.PRELOAD_FUN_PIC);
                if (results != null) {
                    List<GeneralPostModel> tmpNewsList = JSON.parseArray(results, GeneralPostModel.class);
                    adapter.refreshItems(tmpNewsList, getMore);
                }
                break;
            case Config.GIRL_PIC:

                sglm = new StaggeredGridLayoutManager(
                        Prefs.getInt(Config.GIRL_PIC_COL_NUM, 2),
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(sglm);

                adapter = new PicFlowAdapter(getActivity(), MyApp.girlPicLIst, refreshLayout, mType);

                results = Prefs.getString(Config.PRELOAD_GIRL_PIC);

                if (results != null) {
                    List<GeneralPostModel> tmpNewsList = JSON.parseArray(results, GeneralPostModel.class);
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
//        recyclerView.addItemDecoration(new SpacesItemDecoration(5));

        recyclerView.setAdapter(adapter);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recyclerView.setRefreshLayout(refreshLayout);
        recyclerView.setDataChangeListener(this);
        recyclerView.setFocusable(false);
        refreshLayout.setEnabled(true);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!fetching)
            getData(mPage);
    }

    ShakeListener shakeListener;

    @Override
    public void onResume() {
        super.onResume();
        shakeListener = new ShakeListener(getActivity());
        shakeListener.setOnShakeListener(this);
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


    private void getData(final Integer page) {
        fetching = true;
        recyclerView.setLoading(true);

        if (page > 1)
            EventBus.getDefault().post(View.VISIBLE, EventConfig.SHOW_GLOBAL_PRO);
        else if (isFirstIn)
            EventBus.getDefault().post(View.VISIBLE, EventConfig.SHOW_GLOBAL_PRO_TOP);

        new ResponseSimpleNetTask(getActivity(), false, refreshLayout) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                switch (mType) {
                    case Config.FUN_PIC:
                        return api.generalApi(
                                Config.API_GET_FUN_PICS, null, page, null, null, null);
                    case Config.GIRL_PIC:
                        return api.generalApi(
                                Config.API_GET_GIRL_PIS, null, page, null, null, null);
                    case Config.VIDEO:
                        return api.generalApi(
                                Config.API_GET_VIDEOS, null, page, null, null, null);

                }
                return null;
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                String results = JsonUtils.getString(new JSONObject(result), "comments");
                tmpList = JSON.parseArray(results, GeneralPostModel.class);

                StringBuilder sb = new StringBuilder();
                if (tmpList.size() > 0)
                    sb.append("comment-" + tmpList.get(0).getComment_ID());

                for (int i = 1; i < tmpList.size(); i++) {
                    sb.append(",");
                    sb.append("comment-" + tmpList.get(i).getComment_ID());
                }
                getComment(sb.toString());

            }

            @Override
            protected void onFailure() {
                EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO);
                EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO_TOP);
                getData(page);
            }
        }.execute();
    }

    private void getComment(final String threads) {
        new ResponseSimpleNetTask(getActivity(), false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.getDuoshuo(threads);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
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
                    Utils.log(TAG, "fun pic size==" + MyApp.funPicList.size() + Utils.getLineNumber(new Exception()));
                    Utils.log(TAG, "girl pic size==" + MyApp.girlPicLIst.size() + Utils.getLineNumber(new Exception()));
                    EventBus.getDefault().post(0, EventConfig.PICS_CHANGED);
                    getMore = true;
                }

                mPage++;

                isFirstIn = false;
                shakeActive = true;
                recyclerView.setLoading(false);
                EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO);
                EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO_TOP);

                fetching = false;
            }

            @Override
            protected void onFailure() {
                EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO);
                EventBus.getDefault().post(View.INVISIBLE, EventConfig.SHOW_GLOBAL_PRO_TOP);
                getComment(threads);
            }
        }.execute();
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
            getData(mPage);
    }

    @Override
    public void loadMore() {
        if (!fetching)
            getData(mPage);
    }

    boolean shakeActive = true;

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
            }, 200);
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
        shakeListener.setOnShakeListener(null);
    }

}
