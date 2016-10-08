package com.reikyz.jandan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.model.NewsModel;
import com.reikyz.jandan.mvp.activity.itemPager.ItemPagerActivity;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.Utils;

import java.util.List;

/**
 * Created by reikyZ on 16/8/23.
 */
public class NewsAdapter extends BaseListAdapter<NewsModel> {

    final static String TAG = "==NewsAdapter==";

    public NewsAdapter(Context context, List<NewsModel> models) {
        super(context, models);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.getViewHolder(context,
                convertView,
                parent,
                R.layout.item_news,
                position);

        LinearLayout llNews = holder.getView(R.id.ll_news);

        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyApp.currentNewsIndex = position;
                Intent intent = new Intent(context, ItemPagerActivity.class);
                intent.putExtra(Config.TYPE, Config.NEWS);
                intent.putExtra(Config.INDEX, position);

                Utils.log(TAG, "" + Utils.getLineNumber(new Exception()));
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(
                        R.anim.activity_horizonal_entry,
                        R.anim.activity_half_horizonal_exit);
            }
        });


        TextView tvNewsTitle = holder.getView(R.id.tv_news_title);
        TextView tvNewsAuthor = holder.getView(R.id.tv_author);
        ImageView ivNewsThumb = holder.getView(R.id.iv_news_thumb);

        NewsModel news = models.get(position);

        if (news.getTitle() != null)
            tvNewsTitle.setText(news.getTitle());

        if (news.getAuthor() != null
                && news.getAuthor().getName() != null) {
            tvNewsAuthor.setText(news.getAuthor().getName());

            if (news.getTags().size() > 0
                    && news.getTags().get(0) != null &&
                    news.getTags().get(0).getTitle() != null) {
                tvNewsAuthor.setText(news.getAuthor().getName() + "@" + news.getTags().get(0).getTitle());
            }
        }
        ivNewsThumb.setImageDrawable(null);

        if (news.getCustom_fields() != null
                && news.getCustom_fields().getThumb_c().size() > 0
                && news.getCustom_fields().getThumb_c().get(0) != null
                && !TextUtils.isEmpty(news.getCustom_fields().getThumb_c().get(0))) {
//            BitmapUtils.displayImage(news.getCustom_fields().getThumb_c().get(0), ivNewsThumb);
            BitmapUtils.showJpg(context,news.getCustom_fields().getThumb_c().get(0),ivNewsThumb);
        }

//        AnimHelper.showItemLoading(context, holder.getConvertView());
        return holder.getConvertView();
    }


    @Override
    public void refreshItems(List<NewsModel> items) {
        models.clear();
        for (int i = 0; i < items.size(); i++) {
            models.add(i, items.get(i));
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void addMoreItem(List<NewsModel> items) {
        models.addAll(items);
        super.notifyDataSetChanged();
    }

}
