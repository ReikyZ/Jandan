package com.reikyz.jandan.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.JsonUtils;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.adapter.JokeDetailAdapter;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.model.DuoshuoCommentModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.utils.Utils;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reikyZ on 16/9/29.
 */

public class JokeFragment extends BaseFragment {
    final String TAG = "==JokeFragment==";

    private GeneralPostModel mPost;
    private Integer mIndex;


    public static Fragment newInstance(Integer position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Config.INDEX, position);
        JokeFragment jokeFragment = new JokeFragment();
        jokeFragment.setArguments(bundle);
        return jokeFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mIndex = getArguments().getInt(Config.INDEX);

        mPost = MyApp.jokeList.get(mIndex);
    }


    ListView lv;
    JokeDetailAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.log(TAG, this.hashCode() + Utils.getLineNumber(new Exception()));
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        lv = (ListView) view.findViewById(R.id.lv);
        lv.setDivider(null);

        adapter = new JokeDetailAdapter(getActivity(), duoshuoComments, mPost);
        lv.setAdapter(adapter);

        getComments();

        return view;
    }

    private void getComments() {
        new ResponseSimpleNetTask(getActivity(), false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.getDuoshuoComments("comment-" + mPost.getComment_ID());
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                handleComments(result);
            }

            @Override
            protected void onFailure() {
                getComments();
//                Utils.showToast(getActivity(),"获取吐槽失败");
            }
        }.execute();
    }

    List<String> comments = new ArrayList<>();
    List<String> hotPost = new ArrayList<>();
    List<DuoshuoCommentModel> duoshuoComments = new ArrayList<>();

    private void handleComments(String result) throws Exception {
        String strResponse = JsonUtils.getString(new JSONObject(result), "response");
        comments = JSON.parseArray(strResponse, String.class);
        String strHotPost = JsonUtils.getString(new JSONObject(result), "hotPosts");
        hotPost = JSON.parseArray(strHotPost, String.class);


        String strComments = JsonUtils.getString(new JSONObject(result), "parentPosts");

        duoshuoComments.clear();
        for (String postID : comments) {
            String strComment = JsonUtils.getString(new JSONObject(strComments), postID);
            DuoshuoCommentModel comment = JSON.parseObject(strComment, DuoshuoCommentModel.class);
            duoshuoComments.add(comment);
        }
        adapter.setmHotPost(hotPost);
        adapter.notifyDataSetChanged();

    }

    @Subscriber(tag = EventConfig.REFRESH_JOKE_DETAIL)
    void refreshJokeDetail(int i) {
        Utils.log(TAG, "REFRESH_JOKE_DETAIL" + Utils.getLineNumber(new Exception()));
        adapter.notifyDataSetChanged();
    }
}
