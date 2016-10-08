package com.reikyz.jandan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.model.DuoshuoCommentModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.mvp.activity.ShowPicActivity;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.DentistyConvert;
import com.reikyz.jandan.utils.TimeUtils;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.CircleProgressBar;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by reikyZ on 16/9/6.
 */
public class PicDetailAdapter extends BaseListAdapter<DuoshuoCommentModel> {

    final String TAG = "==PicDetailAdapter==";

    final static int ITEM_PIC = 0x00;
    final static int ITEM_DESCRIP = 0x01;
    final static int ITEM_HOT_TITLE = 0x02;
    final static int ITEM_TITLE = 0x03;
    final static int ITEM_COMMENT = 0x04;
    final static int ITEM_VOTE = 0x05;


    private Context mContext;
    private GeneralPostModel mPostModel;
    List<String> mHotPost = new ArrayList<>();

    public void setmHotPost(List<String> mHotPost) {
        this.mHotPost = mHotPost;
    }

    public PicDetailAdapter(Context context, List<DuoshuoCommentModel> models, GeneralPostModel postModel) {
        super(context, models);
        mContext = context;
        mPostModel = postModel;
    }


    @Override
    public int getCount() {
        if (mHotPost.size() > 0) {
            return mPostModel.getPics().size() + 1 + 1 + 1 + mHotPost.size() + 1 + models.size();
        } else {
            return mPostModel.getPics().size() + 1 + 1 + 1 + models.size();
        }
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mPostModel.getPics().size()) {
            return ITEM_PIC;
        } else if (position == mPostModel.getPics().size()) {
            return ITEM_DESCRIP;
        } else if (position == mPostModel.getPics().size() + 1) {
            return ITEM_VOTE;
        } else if (position > mPostModel.getPics().size() + 1) {
            if (mHotPost.size() > 0) {
                if (position == mPostModel.getPics().size() + 2) {
                    return ITEM_HOT_TITLE;
                } else if (position == mPostModel.getPics().size() + 2 + mHotPost.size() + 1) {
                    return ITEM_TITLE;
                } else {
                    return ITEM_COMMENT;
                }

            } else {
                if (position > mPostModel.getPics().size() + 2) {
                    return ITEM_COMMENT;
                } else {
                    return ITEM_TITLE;
                }
            }
        }
        Utils.log(TAG, "ERROR ITEM==position==" + position + Utils.getLineNumber(new Exception()));
        return ITEM_COMMENT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        switch (getItemViewType(position)) {
            case ITEM_PIC:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.item_pic,
                        position
                );
                ImageView ivPic = holder.getView(R.id.iv_pic);
                CircleProgressBar progressBar = holder.getView(R.id.proBar);
                progressBar.setColorSchemeResources(R.color.red, R.color.yellow, R.color.blue);


                ivPic.setImageDrawable(null);
                if (mPostModel.getPics().get(position).indexOf(".gif") > 0) {
                    BitmapUtils.playGif(context, mPostModel.getPics().get(position), ivPic);
                    ivPic.setOnClickListener(null);
                } else {
                    BitmapUtils.showJpg(context, mPostModel.getPics().get(position), ivPic);
                    ivPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ShowPicActivity.class);
                            intent.putExtra(Config.PIC_URL, mPostModel.getPics().get(position));
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(
                                    R.anim.activity_horizonal_entry,
                                    R.anim.activity_half_horizonal_exit);
                        }
                    });
                }
                break;
            case ITEM_DESCRIP:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.item_descrip,
                        position
                );
                TextView tvText = holder.getView(R.id.tv_text);
                TextView tvPostTime = holder.getView(R.id.tv_post_time);

                if (!TextUtils.isEmpty(mPostModel.getText_content())) {
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(mPostModel.getText_content().trim());
                } else {
                    tvText.setVisibility(View.GONE);
                }

                TextView tvAuthor = holder.getView(R.id.tv_author);
                if (!TextUtils.isEmpty(mPostModel.getComment_author())) {
                    tvAuthor.setText(mPostModel.getComment_author());
                }

                if (mPostModel.getComment_date() != null) {
                    tvPostTime.setText("@" + TimeUtils.getPastTimeMark(mPostModel.getComment_date()));
                }

                break;
            case ITEM_VOTE:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.view_vote,
                        position
                );

                TextView tvOoNum = holder.getView(R.id.tv_oo_num);
                TextView tvXxNum = holder.getView(R.id.tv_xx_num);
                TextView tvSpitNum = holder.getView(R.id.tv_spit_num);

                tvOoNum.setPadding(DentistyConvert.dp2px(15), DentistyConvert.dp2px(15), 0, DentistyConvert.dp2px(15));
                tvXxNum.setPadding(DentistyConvert.dp2px(15), DentistyConvert.dp2px(15), 0, DentistyConvert.dp2px(15));
                tvSpitNum.setVisibility(View.GONE);

                Integer upVoted = null, downVoted = null;
                if (mPostModel.getVote_positive() != null)
                    upVoted = Integer.parseInt(mPostModel.getVote_positive());
                if (mPostModel.getVote_negative() != null)
                    downVoted = Integer.parseInt(mPostModel.getVote_negative());

                int vote = checkVoted(mPostModel.getComment_ID());
                if (vote != 0) {
                    if (vote == 1) {
                        // set RED
                        upVoted++;
                        tvOoNum.setText("OO " + upVoted);
                        tvXxNum.setText("XX " + downVoted);

                        tvOoNum.setTextColor(mContext.getResources().getColor(R.color.red));
                        tvXxNum.setTextColor(mContext.getResources().getColor(R.color.grey));
                        tvOoNum.setTypeface(null, Typeface.BOLD);
                        tvXxNum.setTypeface(null, 0);
                    } else {
                        tvOoNum.setText("OO " + upVoted);
                        downVoted++;
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


                tvOoNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkVoted(mPostModel.getComment_ID()) == 0)
                            postVote(mPostModel.getComment_ID(), 1, 1);
                        else Utils.showToast(mContext, "似乎已经投过了");
                    }
                });
                tvXxNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkVoted(mPostModel.getComment_ID()) == 0)
                            postVote(mPostModel.getComment_ID(), 0, -1);
                        else Utils.showToast(mContext, "似乎已经投过了");
                    }
                });
                break;
            case ITEM_HOT_TITLE:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.item_hot_title,
                        position
                );
                break;
            case ITEM_TITLE:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.item_title,
                        position
                );
                TextView tvCommentNum = holder.getView(R.id.tv_comment_num);
                if (models.size() > 0) {
                    tvCommentNum.setText(models.size() + "  ");
                } else {
                    tvCommentNum.setText("还没有");
                }

                break;
            case ITEM_COMMENT:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.item_comment,
                        position
                );
                CircleImageView civAvatar = holder.getView(R.id.civ_head_image);
                TextView tvCommentAuthor = holder.getView(R.id.tv_comment_author);
                TextView tvCommentTime = holder.getView(R.id.tv_comment_time);
                TextView tvLikeNum = holder.getView(R.id.id_like_comment_num);

                ImageView ivComment = holder.getView(R.id.iv_comment);
                TextView tvComment = holder.getView(R.id.tv_comment);

                DuoshuoCommentModel commentModel;
                if (mHotPost.size() == 0) {
                    commentModel = models.get(position - 3 - mPostModel.getPics().size());
                } else {
                    if (position <= mPostModel.getPics().size() + 3 + mHotPost.size()) {
                        int index = position - mPostModel.getPics().size() - 3;
                        commentModel = getCommentByID(mHotPost.get(index));
                    } else {
                        commentModel = models.get(position - mPostModel.getPics().size() - 4 - mHotPost.size());
                    }
                }

                if (commentModel != null) {
                    if (commentModel.getAuthor() != null && commentModel.getAuthor().getAvatar_url() != null) {
                        BitmapUtils.showJpg(context, commentModel.getAuthor().getAvatar_url(), civAvatar);
                    } else {
                        civAvatar.setImageDrawable(context.getResources().getDrawable(R.mipmap.person));
                    }

                    if (commentModel.getAuthor() != null && commentModel.getAuthor().getName() != null) {
                        tvCommentAuthor.setText(commentModel.getAuthor().getName());
                    } else {
                        tvCommentAuthor.setText("");
                    }

                    if (commentModel.getCreated_at() != null) {
                        tvCommentTime.setText("@" + TimeUtils.getTimeFromIso(commentModel.getCreated_at()));
                    }

                    if (commentModel.getLikes() != null && commentModel.getLikes() > 0) {
                        tvLikeNum.setText(commentModel.getLikes() + "");
                        tvLikeNum.setTextColor(context.getResources().getColor(R.color.red));
                    } else {
                        tvLikeNum.setText("0");
                        tvLikeNum.setTextColor(context.getResources().getColor(R.color.strong_grey));
                    }

                    if (commentModel.getMessage() != null) {
                        String msg = commentModel.getMessage();
                        String imgUrl;
                        if (msg.indexOf("http:") > -1 && msg.indexOf(".jpg") > 0) {
                            imgUrl = msg.substring(msg.indexOf("http"), msg.indexOf(".jpg") + 4);
                            BitmapUtils.showJpg(context, imgUrl, ivComment);
                            tvComment.setVisibility(View.GONE);
                            ivComment.setVisibility(View.VISIBLE);
                        } else if (msg.indexOf("http:") > -1 && msg.indexOf(".gif") > 0) {
                            imgUrl = msg.substring(msg.indexOf("http"), msg.indexOf(".gif") + 4);
                            BitmapUtils.playGif(context, imgUrl, ivComment);
                            tvComment.setVisibility(View.GONE);
                            ivComment.setVisibility(View.VISIBLE);
                        } else {
                            tvComment.setText(commentModel.getMessage().replace("<br/>", "\n").replace("\n\n", "\n"));
                            tvComment.setVisibility(View.VISIBLE);
                            ivComment.setVisibility(View.GONE);
                        }


                    }
                }

                break;

        }

        return holder.getConvertView();
    }

    private DuoshuoCommentModel getCommentByID(String id) {
        for (DuoshuoCommentModel comment : models) {
            if (comment.getPost_id().equals(id)) {
                return comment;
            }
        }
        return null;
    }


    private int checkVoted(String post_id) {
        int result = MyApp.voteDAO.getVoted(post_id);
        return result;
    }

    private void postVote(final String post_id, final int option, final int voted) {
        new ResponseSimpleNetTask(mContext, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return MyApp.getApi().postVote("true",
                        option,
                        mPostModel.getComment_ID(),
                        Prefs.getString(Config.COOKIE, ""));
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                MyApp.voteDAO.setVoted(post_id, voted);
                Utils.showToast(context, "Thanks~");

                String cookie = Prefs.getString(Config.COOKIE);
                if (TextUtils.isEmpty(cookie)) {
                    Prefs.save(Config.COOKIE, "voted_comments_" + mPostModel.getComment_ID() + "=" + voted);
                } else
                    Prefs.save(Config.COOKIE, cookie + ";voted_comments_" + mPostModel.getComment_ID() + "=" + voted);

                EventBus.getDefault().post(0, EventConfig.REFRESH_PIC_DETAIL);
            }

            @Override
            protected void onFailure() {
                postVote(post_id, option, voted);
//                Utils.showToast(mContext, "网络不好,稍后再试");
            }
        }.execute();
    }
}
