package com.reikyz.jandan.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.reikyz.jandan.R;


public class LMListView extends ListView implements SwipeRefreshLayout.OnRefreshListener {

    View footView;
    CircleProgressBar progressBar;
    TextView tvLoadMore, tvNoMore;
    ImageView ivFantwanIcon;
    LayoutInflater inflater;
    SwipeRefreshLayout refreshLayout;
    DataChangeListener listener = null;

    public LMListView(Context context) {
        this(context, null);

    }

    public LMListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LMListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        initFooter();
    }

    //初始化底部view
    private void initFooter() {
        footView = inflater.inflate(R.layout.view_list_footer, null);
        progressBar = (CircleProgressBar) footView.findViewById(R.id.proBar);
        tvLoadMore = (TextView) footView.findViewById(R.id.tv_load_more);
        ivFantwanIcon = (ImageView) footView.findViewById(R.id.iv_fantwan_icon);
        tvNoMore = (TextView) footView.findViewById(R.id.tv_no_more);
        progressBar.setColorSchemeResources(R.color.red, R.color.yellow, R.color.blue);
        progressBar.setVisibility(INVISIBLE);
        ivFantwanIcon.setVisibility(INVISIBLE);
        tvNoMore.setVisibility(INVISIBLE);
        this.addFooterView(footView, "", false);
        tvLoadMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    loading();
                    listener.loadMore();
                }
            }
        });
    }


    public void setRefreshLayout(final SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setColorSchemeResources(R.color.red, R.color.yellow, R.color.blue);
        this.refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }


    public void setDataChangeListener(DataChangeListener listener) {
        this.listener = listener;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    //通知listview数据加载完毕
    public void loadComplete() {
        progressBar.setVisibility(GONE);
        tvLoadMore.setVisibility(VISIBLE);
        ivFantwanIcon.setVisibility(GONE);
        tvNoMore.setVisibility(GONE);
    }

    public void loading() {
        progressBar.setVisibility(VISIBLE);
        tvLoadMore.setVisibility(GONE);
        ivFantwanIcon.setVisibility(GONE);
        tvNoMore.setVisibility(GONE);
    }


    public void showProgressBar(boolean showProgressBar) {
        if (showProgressBar) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(INVISIBLE);
        }
    }

    public void noMore() {
        progressBar.setVisibility(GONE);
        tvLoadMore.setVisibility(GONE);
//        ivFantwanIcon.setVisibility(VISIBLE);
        tvNoMore.setVisibility(VISIBLE);
    }

    public void loadShow() {
        footView.setVisibility(VISIBLE);
    }

    @Override
    public void onRefresh() {
        if (listener != null)
            listener.refresh();
    }

    /**
     * 实现该接口，实现刷新和加载更多
     */
    public interface DataChangeListener {

        void refresh();

        void loadMore();
    }

    public void hideFooter() {
        this.removeFooterView(footView);
    }
}
