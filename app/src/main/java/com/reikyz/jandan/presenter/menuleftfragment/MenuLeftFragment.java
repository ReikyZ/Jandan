package com.reikyz.jandan.presenter.menuleftfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.mvp.MenuLeftContract;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.FontUtils;
import com.reikyz.jandan.utils.TimeUtils;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.FlipDotView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuLeftFragment extends Fragment {

    final static String TAG = "==MenuLeftFragment==";

    View view;

    @Bind(R.id.fdv)
    FlipDotView fdv;

    String time;
    String showTime;
    boolean wizDiv = true;

    @OnClick(R.id.fdv)
    void setTime() {

    }

    @Subscriber(tag = EventConfig.SET_TIME)
    void changeTime(int i) {
        FontUtils fontUtils = new FontUtils();
//        time = TimeUtils.getCurrentTime();
//        showTime = time.substring(time.length() - 5, time.length());
        Utils.log(TAG, time + Utils.getLineNumber(new Exception()));
        if (showTime == null) {
            time = TimeUtils.getPastTime(MyApp.timeCount);
            showTime = time;
        }

        if (wizDiv)
            showTime = showTime.replace(":", " ");
        else
            showTime = showTime.replace(" ", ":");
        fdv.flip(getMap(fontUtils.show(getActivity(), showTime)));

        time = TimeUtils.getPastTime(MyApp.timeCount);
        showTime = time;


        wizDiv = !wizDiv;
    }

    @Bind(R.id.riv_pre)
    RoundedImageView riv;

    @OnClick(R.id.ll_invalid)
    void clickInvalid() {

    }

    @OnClick(R.id.ll_news)
    void checkNews() {
        EventBus.getDefault().post(0, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), getString(R.string.news));
    }

    @OnClick(R.id.ll_fun_pic)
    void checkFunPic() {
        EventBus.getDefault().post(1, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), getString(R.string.fun_pic));
    }

    @OnClick(R.id.ll_girl_pic)
    void checkGirlPic() {
        EventBus.getDefault().post(2, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), getString(R.string.girl_pic));
    }

    @OnClick(R.id.ll_joke)
    void checkJoke() {
        EventBus.getDefault().post(3, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), getString(R.string.joke));
    }

    @OnClick(R.id.ll_video)
    void checkVideo() {
        EventBus.getDefault().post(4, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), getString(R.string.video));
    }

    @OnClick(R.id.ll_setting)
    void checkSetting() {
        EventBus.getDefault().post(5, EventConfig.CLOASE_DRAWER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscriber(tag = EventConfig.PRELOAD_IMG)
    void preload(String url) {
//        BitmapUtils.displayImage(url, riv);
        BitmapUtils.showJpg(MyApp.getContext(), url, riv);
    }


    private List<List<Integer>> getMap(boolean[][] array) {
        List<List<Integer>> list = new ArrayList<>();

        for (int i = 0; i < fdv.getmHeightNum(); i++) {
            List<Integer> l = new ArrayList<>();
            l.clear();
            for (int j = 0; j < fdv.getmWidthNum(); j++) {
                if (i < array.length &&
                        j < array[i].length &&
                        array[i][j]) {
                    l.add(1);
                } else {
                    l.add(0);
                }
            }
            list.add(l);
        }
        return list;
    }
}
