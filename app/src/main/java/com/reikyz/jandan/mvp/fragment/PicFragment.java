package com.reikyz.jandan.mvp.fragment;

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
import com.reikyz.jandan.adapter.PicDetailAdapter;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.model.DuoshuoCommentModel;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.mvp.base.BaseFragment;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reikyZ on 16/9/6.
 */
public class PicFragment extends BaseFragment {
    final String TAG = "==PicFragment==";

    String mType;
    private GeneralPostModel mPost;
    private Integer mIndex;
    boolean fetching = false;

    public static Fragment newInstance(String type, Integer position) {
        Bundle bundle = new Bundle();
        bundle.putString(Config.TYPE, type);
        bundle.putInt(Config.INDEX, position);
        PicFragment picFragment = new PicFragment();
        picFragment.setArguments(bundle);

        return picFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        mType = getArguments().getString(Config.TYPE);
        mIndex = getArguments().getInt(Config.INDEX);

//        MyApp.currentNewsIndex = mIndex;
        switch (mType) {
            case Config.FUN_PIC:
                mPost = MyApp.funPicList.get(mIndex);
                break;
            case Config.GIRL_PIC:
                mPost = MyApp.girlPicLIst.get(mIndex);
                break;
        }

    }

    ListView lv;
    PicDetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        lv = (ListView) view.findViewById(R.id.lv);
        lv.setDivider(null);

//        ivPic = (ImageView) view.findViewById(R.id.iv_pic);
//        progressBar = (CircleProgressBar) view.findViewById(R.id.proBar);
//        progressBar.setColorSchemeResources(R.color.red, R.color.yellow, R.color.blue);
////        progressBar.setVisibility(View.INVISIBLE);
//
//
//        ivPic.setImageDrawable(null);
//        if (mPost.getPics().get(0).indexOf(".gif") > 0) {
//            Utils.log(TAG, "show GIF" + Utils.getLineNumber(new Exception()));
//            BitmapUtils.playGif(getActivity(), mPost.getPics().get(0), ivPic);
//        } else {
//            Utils.log(TAG, "show JPG" + Utils.getLineNumber(new Exception()));
////            BitmapUtils.displayImageWithAnim(mPost.getPics().get(0), ivPic);
//            BitmapUtils.showJpg(getActivity(), mPost.getPics().get(0), ivPic);
//        }
        adapter = new PicDetailAdapter(getActivity(), duoshuoComments, mPost);
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


    @Subscriber(tag = EventConfig.REFRESH_PIC_DETAIL)
    void refreshPicDetail(int i) {
        adapter.notifyDataSetChanged();
    }
}
