package com.reikyz.jandan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.mvp.activity.itemPager.ItemPagerActivity;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.TimeUtils;
import com.reikyz.jandan.utils.Utils;

import org.simple.eventbus.EventBus;

import java.text.ParseException;
import java.util.List;

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

        final GeneralPostModel joke = models.get(position);
//        postModel = joke;

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

        Integer upVoted = null, downVoted = null;
        if (joke.getVote_positive() != null)
            upVoted = Integer.parseInt(joke.getVote_positive());
        if (joke.getVote_negative() != null)
            downVoted = Integer.parseInt(joke.getVote_negative());

        MyApp.voteDAO.setPositive(joke.getComment_ID(), upVoted);
        MyApp.voteDAO.setNegative(joke.getComment_ID(), downVoted);

        int vote = checkVoted(joke.getComment_ID());
        if (vote != 0) {
            if (vote == 1) {
                // set RED
                upVoted++;
                tvOoNum.setText("OO " + upVoted);
                tvXxNum.setText("XX " + downVoted);

                tvOoNum.setTextColor(context.getResources().getColor(R.color.red));
                tvXxNum.setTextColor(context.getResources().getColor(R.color.grey));
                tvOoNum.setTypeface(null, Typeface.BOLD);
                tvXxNum.setTypeface(null, 0);
            } else {
                tvOoNum.setText("OO " + upVoted);
                downVoted++;
                tvXxNum.setText("XX " + downVoted);

                tvOoNum.setTextColor(context.getResources().getColor(R.color.grey));
                tvXxNum.setTextColor(context.getResources().getColor(R.color.blue));
                tvOoNum.setTypeface(null, 0);
                tvXxNum.setTypeface(null, Typeface.BOLD);
            }
        } else {
            tvOoNum.setTextColor(context.getResources().getColor(R.color.strong_grey));
            tvXxNum.setTextColor(context.getResources().getColor(R.color.strong_grey));

            tvOoNum.setText("OO " + upVoted);
            tvXxNum.setText("XX " + downVoted);

            tvOoNum.setTypeface(null, 0);
            tvXxNum.setTypeface(null, 0);

        }


        tvOoNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkVoted(joke.getComment_ID()) == 0)
                    postVote(joke.getComment_ID(), 1, 1);
                else Utils.showToast(context, "似乎已经投过了");
            }
        });
        tvXxNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkVoted(joke.getComment_ID()) == 0)
                    postVote(joke.getComment_ID(), 0, -1);
                else Utils.showToast(context, "似乎已经投过了");
            }
        });


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


    private int checkVoted(String post_id) {
        int result = MyApp.voteDAO.getVoted(post_id);
        return result;
    }

    private void postVote(final String post_id, final int option, final int voted) {
        new ResponseSimpleNetTask(context, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return MyApp.getApi().postVote("true",
                        option,
                        post_id,
//                        "voted_comments_" + mPicModel.getComment_ID() + "=" + voted);
//                        "voted_comments_3238970=1");
                        Prefs.getString(Config.COOKIE, ""));
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                MyApp.voteDAO.setVoted(post_id, voted);
                Utils.showToast(context, "Thanks~");
                String cookie = Prefs.getString(Config.COOKIE);
                if (TextUtils.isEmpty(cookie)) {
                    Prefs.save(Config.COOKIE, "voted_comments_" + post_id + "=" + voted);
                } else
                    Prefs.save(Config.COOKIE, cookie + ";voted_comments_" + post_id+ "=" + voted);

                Utils.log(TAG, cookie + "==" + result + Utils.getLineNumber(new Exception()));
                EventBus.getDefault().post(0, EventConfig.REFRESH_JOKE_LIST);
            }

            @Override
            protected void onFailure() {
                postVote(post_id, option, voted);
//                Utils.showToast(mContext, "网络不好,稍后再试");
            }
        }.execute();
    }
}
