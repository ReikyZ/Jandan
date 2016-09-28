package com.reikyz.jandan.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
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
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.db.VoteDAOImpl;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.presenter.itempager.ItemPagerActivity;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.DentistyConvert;
import com.reikyz.jandan.utils.TimeUtils;
import com.reikyz.jandan.utils.Utils;

import java.text.ParseException;
import java.util.Random;

/**
 * Created by reikyZ on 16/8/25.
 */
public class PicCardView extends LinearLayout {

    final String TAG = "==PicCardView==";

    Context mContext;
    VoteDAOImpl voteDAO;

    public PicCardView(Context context) {
        this(context, null);
    }

    public PicCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        voteDAO = VoteDAOImpl.getInstance(context);
        initView(context);
    }

    LinearLayout llVideoHide;
    TextView tvVideoDes;
    ImageView ivVideo;

    LinearLayout llPic;
    TextView tvPicAuthor;
    TextView tvPicDate;
    TextView tvPicContent;
    ImageView ivPicContent;

    ViewGroup voteView;
    TextView tvOoNum;
    TextView tvXxNum;
    TextView tvSpitNum;
    ImageView ivPicAction;

    ImageView ivMask;

    private void initView(Context context) {
        View.inflate(context, R.layout.view_pic_card, this);

        llVideoHide = (LinearLayout) findViewById(R.id.ll_video_hide);
        tvVideoDes = (TextView) findViewById(R.id.tv_video_des);
        ivVideo = (ImageView) findViewById(R.id.iv_video);

        llPic = (LinearLayout) findViewById(R.id.ll_pic);
        tvPicAuthor = (TextView) findViewById(R.id.tv_pic_author);
        tvPicDate = (TextView) findViewById(R.id.tv_pic_date);
        tvPicContent = (TextView) findViewById(R.id.tv_pic_content);
        ivPicContent = (ImageView) findViewById(R.id.iv_pic);

        voteView = (ViewGroup) findViewById(R.id.view_vote);

        tvOoNum = (TextView) findViewById(R.id.tv_oo_num);
        tvXxNum = (TextView) findViewById(R.id.tv_xx_num);
        tvSpitNum = (TextView) findViewById(R.id.tv_spit_num);
        ivPicAction = (ImageView) findViewById(R.id.iv_pic_action);

        ivMask = (ImageView) findViewById(R.id.iv_mask);
    }

    GeneralPostModel mPicModel;
    Integer width;

    public void showCard(final GeneralPostModel picModel, final Integer position, final String type) {
        mPicModel = picModel;
        Utils.log(TAG, picModel.toString() + Utils.getLineNumber(new Exception()));

        llPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemPagerActivity.class);
                intent.putExtra(Config.TYPE, type);
                intent.putExtra(Config.INDEX, position);

                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(
                        R.anim.activity_horizonal_entry,
                        R.anim.activity_half_horizonal_exit);
            }
        });


        if (picModel.getComment_author() != null)
            tvPicAuthor.setText(picModel.getComment_author());
        if (picModel.getComment_date() != null) {
            try {
                tvPicDate.setText(TimeUtils.parseMark(picModel.getComment_date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (picModel.getComment_content() != null)
            tvPicContent.setText(picModel.getComment_content());

        LayoutParams params;
        if (type.equals(Config.VIDEO)) {
            ivVideo.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.blank_vertical));

            if (width == null)
                width = (Utils.getDisplayMetrics((Activity) mContext).widthPixels - DentistyConvert.dp2px(15)) / 2;
            params = (LayoutParams) ivVideo.getLayoutParams();
            params.height = width / 5 * 3;
            ivVideo.setLayoutParams(params);

            llVideoHide.setVisibility(GONE);
            ivPicContent.setVisibility(GONE);
            ivVideo.setVisibility(VISIBLE);
            tvVideoDes.setVisibility(VISIBLE);


            if (picModel.getVideos() != null &&
                    picModel.getVideos().size() > 0 &&
                    picModel.getVideos().get(0) != null &&
                    picModel.getVideos().get(0).getThumbnail() != null) {
                BitmapUtils.displayImageWithAnim(picModel.getVideos().get(0).getThumbnail(), ivVideo);
            }

            if (picModel.getVideos() != null &&
                    picModel.getVideos().size() > 0 &&
                    picModel.getVideos().get(0) != null &&
                    !TextUtils.isEmpty(picModel.getVideos().get(0).getBigPicUrl())) {
                BitmapUtils.displayImageWithAnim(picModel.getVideos().get(0).getBigPicUrl(), ivVideo);
            }

            if (picModel.getVideos() != null &&
                    picModel.getVideos().size() > 0 &&
                    picModel.getVideos().get(0) != null &&
                    picModel.getVideos().get(0).getTitle() != null)
                tvVideoDes.setText(picModel.getVideos().get(0).getTitle());

        } else {
            ivPicContent.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.blank_vertical));


            if (width == null) width = ivPicContent.getWidth();
            params = (LayoutParams) ivPicContent.getLayoutParams();
            params.height = position % 5 * 100 + 400;
            ivPicContent.setLayoutParams(params);

            llVideoHide.setVisibility(VISIBLE);
            ivPicContent.setVisibility(VISIBLE);
            ivVideo.setVisibility(GONE);
            tvVideoDes.setVisibility(GONE);

            if (picModel.getPics() != null &&
                    picModel.getPics().size() > 0 &&
                    picModel.getPics().get(0) != null) {

                if (picModel.getPics().get(0).indexOf(".gif") > 0) {
                    BitmapUtils.showGif(mContext, picModel.getPics().get(0), ivPicContent);
                } else {
                    BitmapUtils.displayImageWithAnim(picModel.getPics().get(0), ivPicContent);

                }
            }
        }


//        if (tvPicContent.getHeight() > DentistyConvert.dp2px(280)) {
//            ivMask.setVisibility(View.VISIBLE);
//        } else {
        ivMask.setVisibility(View.GONE);
//        }


//        if (Prefs.getInt(Config.FUN_PIC_COL_NUM, 2) == 1)
//            ivPicAction.setVisibility(VISIBLE);
//        else ivPicAction.setVisibility(GONE);


        if (picModel.getThread() != null) {
            tvSpitNum.setText("吐槽 " + picModel.getThread().getComments());
        }

        Integer upVoted = null, downVoted = null;
        if (picModel.getVote_positive() != null)
            upVoted = Integer.parseInt(picModel.getVote_positive());
        if (picModel.getVote_negative() != null)
            downVoted = Integer.parseInt(picModel.getVote_negative());

        voteDAO.setPositive(picModel.getComment_ID(), upVoted);
        voteDAO.setNegative(picModel.getComment_ID(), downVoted);

        if (checkVoted(picModel.getComment_ID()) != 0) {
            if (checkVoted(picModel.getComment_ID()) == 1) {
                // set RED
                if (upVoted != null && upVoted == 0) upVoted++;
                tvOoNum.setText("OO " + upVoted);
                tvXxNum.setText("XX " + downVoted);

                tvOoNum.setTextColor(mContext.getResources().getColor(R.color.red));
                tvXxNum.setTextColor(mContext.getResources().getColor(R.color.grey));
                tvOoNum.setTypeface(null, Typeface.BOLD);
                tvXxNum.setTypeface(null, 0);
            } else {
                tvOoNum.setText("OO " + upVoted);
                if (downVoted != null && downVoted == 0) upVoted++;
                tvXxNum.setText("XX " + downVoted);

                tvOoNum.setTextColor(mContext.getResources().getColor(R.color.grey));
                tvXxNum.setTextColor(mContext.getResources().getColor(R.color.blue));
                tvOoNum.setTypeface(null, 0);
                tvXxNum.setTypeface(null, Typeface.BOLD);
            }
        } else {
            tvOoNum.setTextColor(mContext.getResources().getColor(R.color.strong_grey));
            tvXxNum.setTextColor(mContext.getResources().getColor(R.color.strong_grey));

            tvOoNum.setText("OO " + upVoted);
            tvXxNum.setText("XX " + downVoted);

            tvOoNum.setTypeface(null, 0);
            tvXxNum.setTypeface(null, 0);

        }


        tvOoNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkVoted(picModel.getComment_ID()) == 0)
                    postVote(picModel.getComment_ID(), 1, 1);
                else Utils.showToast(mContext, "似乎已经投过了");
            }
        });
        tvXxNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkVoted(picModel.getComment_ID()) == 0)
                    postVote(picModel.getComment_ID(), 0, -1);
                else Utils.showToast(mContext, "似乎已经投过了");
            }
        });

    }

    private int checkVoted(String post_id) {
        int result = voteDAO.getVoted(post_id);
        Utils.log(TAG, post_id + "===" + result + Utils.getLineNumber(new Exception()));
        return result;
    }

    private void postVote(final String post_id, final int option, final int voted) {
        new ResponseSimpleNetTask(mContext, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return MyApp.getApi().postVote("true",
                        option,
                        mPicModel.getComment_ID(),
//                        "voted_comments_" + mPicModel.getComment_ID() + "=" + voted);
//                        "voted_comments_3238970=1");
                        Prefs.getString(Config.COOKIE, ""));
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                voteDAO.setVoted(post_id, voted);

                String cookie = Prefs.getString(Config.COOKIE);
                if (TextUtils.isEmpty(cookie)) {
                    Prefs.save(Config.COOKIE, "voted_comments_" + mPicModel.getComment_ID() + "=" + voted);
                } else
                    Prefs.save(Config.COOKIE, cookie + ";voted_comments_" + mPicModel.getComment_ID() + "=" + voted);

                Utils.log(TAG, cookie + "==" + result + Utils.getLineNumber(new Exception()));
            }

            @Override
            protected void onFailure() {
                Utils.showToast(mContext, "网络不好,稍后再试");
            }
        }.execute();
    }
}
