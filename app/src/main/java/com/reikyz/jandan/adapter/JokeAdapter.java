package com.reikyz.jandan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reikyz.jandan.R;
import com.reikyz.jandan.model.JokeModel;

import java.util.List;

/**
 * Created by reikyZ on 16/8/23.
 */
public class JokeAdapter extends BaseListAdapter<JokeModel> {

    public JokeAdapter(Context context, List<JokeModel> models) {
        super(context, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.getViewHolder(context,
                convertView,
                parent,
                R.layout.item_joke,
                position);

        TextView tvJokeContent = holder.getView(R.id.tv_joke_content);


        JokeModel joke = models.get(position);

        if (joke.getComment_content() != null)
            tvJokeContent.setText(joke.getComment_content());

        return holder.getConvertView();
    }


    @Override
    public void refreshItems(List<JokeModel> items) {
        models.clear();
        for (int i = 0; i < items.size(); i++) {
            models.add(i, items.get(i));
        }
        super.notifyDataSetChanged();
    }

    @Override
    public void addMoreItem(List<JokeModel> items) {
        models.addAll(items);
        super.notifyDataSetChanged();
    }

}
