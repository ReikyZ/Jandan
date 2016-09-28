package com.reikyz.jandan.presenter.newsdetailfragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.JsonUtils;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.model.NewsModel;
import com.reikyz.jandan.model.PageHtmlModel;
import com.reikyz.jandan.presenter.BaseFragment;
import com.reikyz.jandan.utils.Utils;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by reikyZ on 16/8/24.
 */
public class WebPageFragment extends BaseFragment {

    final String TAG = "==WebPageFragment==";

    WebView webView;
    String mType;
    private NewsModel mNewsModel;
    private GeneralPostModel mPost;
    private PageHtmlModel mPageHtml;
    private Integer mIndex;

    public static Fragment newInstance(String type, Integer position) {
        Bundle bundle = new Bundle();
        bundle.putString(Config.TYPE, type);
        bundle.putInt(Config.INDEX, position);
        WebPageFragment webPageFragment = new WebPageFragment();
        webPageFragment.setArguments(bundle);

        return webPageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mType = getArguments().getString(Config.TYPE);
        mIndex = getArguments().getInt(Config.INDEX);

        MyApp.currentNewsIndex = mIndex;
        switch (mType) {
            case Config.NEWS:
                MyApp.currentNewsIndex = mIndex;
                break;
            case Config.VIDEO:
                MyApp.currentVideoIndex = mIndex;
                break;
        }

    }
//
//    boolean isVisible;
//    boolean isPrepared;
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Utils.log(TAG, this.hashCode() + Utils.getLineNumber(new Exception()));
//    }

//    protected void onVisible() {
//        lazyLoad();
//    }
//
//    protected void lazyLoad() {
//        if (!isPrepared || !isVisible) {
//            return;
//        }
//    }
//
//    protected void onInvisible() {
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.log(TAG, this.hashCode() + Utils.getLineNumber(new Exception()));
        View view = inflater.inflate(R.layout.activity_web_view, container, false);

        webView = (WebView) view.findViewById(R.id.wv);

        switch (mType) {
            case Config.NEWS:
//                if (MyApp.currentNewsIndex != mIndex) break;
                if (html != null && html.length() > 100) break;
                mNewsModel = MyApp.newsList.get(mIndex);
                getPage();
                break;
            case Config.VIDEO:
                Utils.log(TAG, mIndex + Utils.getLineNumber(new Exception()));
                if (MyApp.currentVideoIndex != mIndex) break;
                mPost = MyApp.videoLIst.get(mIndex);
                initVideoPage();
                break;
        }
        return view;
    }


    private void getPage() {
        initProgressDialog();

        new ResponseSimpleNetTask(getActivity(), false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.generalApi("get_post",
                        "content",
                        null,
                        null,
                        null,
                        mNewsModel.getId());
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                EventBus.getDefault().post(0, EventConfig.CLOSE_LOADING);
                String results = JsonUtils.getString(new JSONObject(result), "post");
                mPageHtml = JSON.parseObject(results, PageHtmlModel.class);
                initWebView();
            }
        }.execute();
    }

    String html;

    void initWebView() {
        Utils.log(TAG, "get News Succeed==" + mNewsModel.getTitle() + Utils.getLineNumber(new Exception()));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        html = "<h3 style=\"font-family:verdana;color:red\">" + mNewsModel.getTitle() + "</h3>" +
                "<p style=\"color:grey\">" + mNewsModel.getAuthor().getName() + "@" + mNewsModel.getTags().get(0).getTitle() + "</p>"
                + mPageHtml.getContent()
                .replace("<img ", "<img style=\"width:100%;height:auto\" ")
                .replace("width=\"600\"", "width=100%")
                .replace("width=\"300\"", "width=100%")
                .replace("width=\"480\"", "width=100%")
                .replace("allowfullscreen=\"true\"", "allowfullscreen=\"false\"")
                .replace("height=\"480\"", "");

        int end = html.indexOf("share-links");
        webView.loadData(html.substring(0, end - 10), "text/html; charset=UTF-8", "utf-8");
    }


    private void initVideoPage() {
        initProgressDialog();
        Utils.log(TAG, mIndex + Utils.getLineNumber(new Exception()));
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(mPost.getComment_agent());
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.loadUrl(mPost.getText_content());
//        webView.loadUrl("http://yanwenzi.net");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }
        });
    }


    ProgressDialog dialog;

    void initProgressDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.show();
    }

    @Subscriber(tag = EventConfig.CLOSE_LOADING)
    void closeLoading(int i) {
        if (dialog != null)
            dialog.dismiss();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
