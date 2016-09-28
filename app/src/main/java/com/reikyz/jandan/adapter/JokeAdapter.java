package com.reikyz.jandan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.presenter.itempager.ItemPagerActivity;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.DentistyConvert;
import com.reikyz.jandan.utils.TimeUtils;
import com.reikyz.jandan.utils.Utils;

import java.text.ParseException;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by reikyZ on 16/8/23.
 */
public class JokeAdapter extends BaseListAdapter<GeneralPostModel> {

    final String TAG = "==JokeAdapter==";

    public JokeAdapter(Context context, List<GeneralPostModel> models) {
        super(context, models);
    }

    TextView tvJokeAuthor;

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.getViewHolder(context,
                convertView,
                parent,
                R.layout.item_joke,
                position);

        LinearLayout ll = holder.getView(R.id.ll_joke);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemPagerActivity.class);
                intent.putExtra(Config.TYPE, Config.JOKE);
                intent.putExtra(Config.INDEX, position);

                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(
                        R.anim.activity_horizonal_entry,
                        R.anim.activity_half_horizonal_exit);
            }
        });

        tvJokeAuthor = holder.getView(R.id.tv_joke_author);
        TextView tvJokeDate = holder.getView(R.id.tv_joke_date);
        final TextView tvJokeContent = holder.getView(R.id.tv_joke_content);
        ImageView ivJokePic = holder.getView(R.id.iv_joke_pic);
        final ImageView ivMask = holder.getView(R.id.iv_mask);
        TextView tvOoNum = holder.getView(R.id.tv_oo_num);
        TextView tvXxNum = holder.getView(R.id.tv_xx_num);
        TextView tvSpitNum = holder.getView(R.id.tv_spit_num);
        ImageView ivJokeAction = holder.getView(R.id.iv_joke_action);

        GeneralPostModel joke = models.get(position);

        if (joke.getComment_author() != null)
            tvJokeAuthor.setText(joke.getComment_author());
        if (joke.getComment_date() != null) {
            try {
                tvJokeDate.setText(TimeUtils.parseMark(joke.getComment_date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (!TextUtils.isEmpty(joke.getText_content())) {
            tvJokeContent.setVisibility(View.VISIBLE);
            tvJokeContent.setText(joke.getText_content());
        } else tvJokeContent.setVisibility(View.GONE);

        if (joke.getComment_content().indexOf("img src") > 0) {
            ivJokePic.setVisibility(View.VISIBLE);
            String picUrl = joke.getComment_content().replace(joke.getText_content(), "").trim().replace("<img src=\"", "").replace("\" />", "");
//            BitmapUtils.displayImage(picUrl, ivJokePic);
            BitmapUtils.showJpg(context, picUrl, ivJokePic);
        } else {
            ivJokePic.setVisibility(View.GONE);
        }

//        if (tvJokeContent.getHeight() > DentistyConvert.dp2px(290) &&
//                tvJokeContent.getHeight() < DentistyConvert.dp2px(302) &&
//                tvJokeContent.getLineCount() > 14) {
//            ivMask.setVisibility(View.VISIBLE);
//        } else {
//            ivMask.setVisibility(View.GONE);
//        }

        if (joke.getVote_positive() != null)
            tvOoNum.setText("OO " + joke.getVote_positive());
        if (joke.getVote_negative() != null)
            tvXxNum.setText("XX " + joke.getVote_negative());
        if (joke.getThread() != null)
            tvSpitNum.setText("吐槽 " + joke.getThread().getComments());

//        ivMask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ivMask.setVisibility(View.GONE);
//
//                tvJokeContent.setMaxHeight(5000);
//            }
//        });

        ivJokeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showToast(context, "Action-joke");
            }
        });
//        if (position > oldPos) {
//            AnimHelper.showItemLoading(context, holder.getConvertView());
//        }
//        oldPos = position;
        return holder.getConvertView();
    }

    int oldPos = 0;

    @Override
    public void refreshItems(List<GeneralPostModel> items) {
        models.clear();
        for (int i = 0; i < items.size(); i++) {
            models.add(i, items.get(i));
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void addMoreItem(List<GeneralPostModel> items) {
        models.addAll(items);
        super.notifyDataSetChanged();
    }

}
