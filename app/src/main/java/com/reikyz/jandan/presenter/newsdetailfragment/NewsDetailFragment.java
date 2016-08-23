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
public class NewsDetailFragment extends BaseFragment {

    final static String TAG = "==NewsDetailFragment==";

    WebView webView;
    private NewsModel mNewsModel;
    private PageHtmlModel mPageHtml;
    private Integer mIndex;

    public static Fragment newInstance(Integer position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Config.INDEX, position);
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        newsDetailFragment.setArguments(bundle);

        Utils.log(TAG, "" + Utils.getLineNumber(new Exception()));
        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        Utils.log(TAG, "" + Utils.getLineNumber(new Exception()));

        mIndex = getArguments().getInt(Config.INDEX);
        MyApp.currentIndex = mIndex;
        mNewsModel = MyApp.newsList.get(mIndex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_web_view, container, false);

        webView = (WebView) view.findViewById(R.id.wv);

        getPage();

        initProgressDialog();

        Utils.log(TAG, "" + Utils.getLineNumber(new Exception()));
        return view;
    }

    private void getPage() {
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


    ProgressDialog dialog;

    void initProgressDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.show();
    }

    String html;

    void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        html = "<h3 style=\"font-family:verdana;color:red\">" + mNewsModel.getTitle() + "</h3>" +
                "<p style=\"color:grey\">" + mNewsModel.getAuthor().getName() + "@" + mNewsModel.getTags().get(0).getTitle() + "</p>"
                + mPageHtml.getContent();

        webView.loadData(html, "text/html; charset=UTF-8", "utf-8");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

    }

    @Subscriber(tag = EventConfig.CLOSE_LOADING)
    void closeLoading(int i) {
        dialog.dismiss();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
