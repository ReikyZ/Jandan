package com.reikyz.jandan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.model.DuoshuoCommentModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.presenter.ShowPicActivity;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.TimeUtils;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by reikyZ on 16/9/29.
 */

public class JokeDetailAdapter extends BaseListAdapter<DuoshuoCommentModel> {

    final String TAG = "==JokeDetailAdapter==";

    final static int ITEM_JOKE = 0x00;
    final static int ITEM_DESCRIP = 0x01;
    final static int ITEM_HOT_TITLE = 0x02;
    final static int ITEM_TITLE = 0x03;
    final static int ITEM_COMMENT = 0x04;

    private Context mContext;
    private GeneralPostModel mPostModel;
    List<String> mHotPost = new ArrayList<>();

    public void setmHotPost(List<String> mHotPost) {
        this.mHotPost = mHotPost;
    }

    public JokeDetailAdapter(Context context, List<DuoshuoCommentModel> models, GeneralPostModel postModel) {
        super(context, models);
        mContext = context;
        mPostModel = postModel;
    }


    @Override
    public int getCount() {
        if (mHotPost.size() > 0) {
            return 1 + 1 + 1 + mHotPost.size() + 1 + models.size();
        } else {
            return 1 + 1 + 1 + models.size();
        }
    }


    @Override
    public int getViewTypeCount() {
        return 6;
    }


    @Override
    public int getItemViewType(int position) {
        if (position < 1) {
            return ITEM_JOKE;
        } else if (position == 1) {
            return ITEM_DESCRIP;
        } else if (position > 1) {
            if (mHotPost.size() > 0) {
                if (position == 1 + 1) {
                    return ITEM_HOT_TITLE;
                } else if (position == 1 + 1 + mHotPost.size() + 1) {
                    return ITEM_TITLE;
                } else {
                    return ITEM_COMMENT;
                }

            } else {
                if (position > 1 + 1) {
                    return ITEM_COMMENT;
                } else {
                    return ITEM_TITLE;
                }
            }
        }
        return ITEM_COMMENT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        switch (getItemViewType(position)) {
            case ITEM_JOKE:
                holder = BaseViewHolder.getViewHolder(
                        context,
                        convertView,
                        parent,
                        R.layout.item_joke_detail,
                        position
                );

                final TextView tvJokeContent = holder.getView(R.id.tv_joke_content);
                ImageView ivJokePic = holder.getView(R.id.iv_joke_pic);
                final ImageView ivMask = holder.getView(R.id.iv_mask);

                if (!TextUtils.isEmpty(mPostModel.getText_content())) {
                    tvJokeContent.setVisibility(View.VISIBLE);
                    tvJokeContent.setText(mPostModel.getText_content());
                } else tvJokeContent.setVisibility(View.GONE);

                if (mPostModel.getComment_content().indexOf("img src") > 0) {
                    ivJokePic.setVisibility(View.VISIBLE);
                    String picUrl = mPostModel.getComment_content().replace(mPostModel.getText_content(), "").trim().replace("<img src=\"", "").replace("\" />", "");
//            BitmapUtils.displayImage(picUrl, ivJokePic);
                    BitmapUtils.showJpg(context, picUrl, ivJokePic);
                } else {
                    ivJokePic.setVisibility(View.GONE);
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

                tvText.setVisibility(View.GONE);

                TextView tvAuthor = holder.getView(R.id.tv_author);
                if (!TextUtils.isEmpty(mPostModel.getComment_author())) {
                    tvAuthor.setText(mPostModel.getComment_author());
                }

                if (mPostModel.getComment_date() != null) {
                    tvPostTime.setText("@" + TimeUtils.getPastTimeMark(mPostModel.getComment_date()));
                }

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
                    commentModel = models.get(position - 2 - 1);
                } else {
                    if (position <= 1 + 2 + mHotPost.size()) {
                        int index = position - 1 - 2;
                        commentModel = getCommentByID(mHotPost.get(index));
                    } else {
                        commentModel = models.get(position - 1 - 3 - mHotPost.size());
                    }
                }

                if (commentModel != null) {
                    if (commentModel.getAuthor() != null && commentModel.getAuthor().getAvatar_url() != null) {
//                        BitmapUtils.displayImage(commentModel.getAuthor().getAvatar_url(), civAvatar);
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
}
