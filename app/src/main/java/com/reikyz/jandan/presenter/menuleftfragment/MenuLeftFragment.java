package com.reikyz.jandan.presenter.menuleftfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reikyz.jandan.R;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.utils.Utils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuLeftFragment extends Fragment implements MenuLeftContract.View {

    final static String TAG = "==MenuLeftFragment==";

    MenuLeftContract.Presenter mPresenter;

    View view;

    @OnClick(R.id.ll_news)
    void checkNews() {
        EventBus.getDefault().post(0, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), "news");
    }

    @OnClick(R.id.ll_fun_pic)
    void checkFunPic() {
        EventBus.getDefault().post(1, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), "fun");
    }

    @OnClick(R.id.ll_girl_pic)
    void checkGirlPic() {
        EventBus.getDefault().post(2, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), "girl");
    }

    @OnClick(R.id.ll_joke)
    void checkJoke() {
        EventBus.getDefault().post(3, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), "joke");
    }

    @OnClick(R.id.ll_video)
    void checkVideo() {
        EventBus.getDefault().post(4, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), "video");
    }

    @OnClick(R.id.ll_setting)
    void checkSetting() {
        EventBus.getDefault().post(5, EventConfig.CLOASE_DRAWER);
        Utils.showToast(getActivity(), "setting");
    }


    public static MenuLeftFragment newInstance() {
        return new MenuLeftFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(@NonNull MenuLeftContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
