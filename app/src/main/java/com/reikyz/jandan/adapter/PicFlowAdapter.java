package com.reikyz.jandan.adapter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reikyz.jandan.R;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.PicCardView;

import java.util.List;

/**
 * Created by reikyZ on 16/8/25.
 */
public class PicFlowAdapter extends RecyclerView.Adapter {

    final String TAG = "==PicFlowAdapter==";

    private List<GeneralPostModel> itemList;
    private Context context;
    boolean hasMore = false;
    String mType;
    SwipeRefreshLayout refreshLayout;

    ImageView preLoadIV;

    public PicFlowAdapter(Context context, List<GeneralPostModel> itemList,
                          SwipeRefreshLayout refreshLayout, String type) {
        this.itemList = itemList;
        this.context = context;
        this.refreshLayout = refreshLayout;
        mType = type;
        preLoadIV = new ImageView(context);

        preLoad(itemList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_card, null);
        PicViewHolder holder = new PicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PicViewHolder) holder).picCardView.showCard(itemList.get(position), position, mType);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void refreshItems(List<GeneralPostModel> items, boolean hasMore) {
        preLoad(items);
        this.hasMore = hasMore;
        itemList.clear();
        for (int i = 0; i < items.size(); i++) {
            this.itemList.add(i, items.get(i));
        }
        notifyDataSetChanged();
    }

    public void addMoreItem(List<GeneralPostModel> items, boolean hasMore) {
        preLoad(items);
        this.hasMore = hasMore;
        this.itemList.addAll(items);
        Utils.log(TAG, "type==" + mType + "==new pics size==" + itemList.size() + Utils.getLineNumber(new Exception()));
        notifyDataSetChanged();
    }


    private void preLoad(List<GeneralPostModel> itemList) {
        for (GeneralPostModel pic : itemList) {
            if (pic.getPics() != null &&
                    pic.getPics().size() > 0 &&
                    pic.getPics().get(0) != null)
                BitmapUtils.showJpg(context, pic.getPics().get(0), preLoadIV);
        }
    }


    class PicViewHolder extends RecyclerView.ViewHolder {

        PicCardView picCardView;

        public PicViewHolder(View itemView) {
            super(itemView);
            picCardView = (PicCardView) itemView.findViewById(R.id.pic_card);
        }
    }
}
