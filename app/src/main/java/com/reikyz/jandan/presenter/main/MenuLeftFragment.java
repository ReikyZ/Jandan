package com.reikyz.jandan.presenter.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reikyz.jandan.R;

import butterknife.ButterKnife;

public class MenuLeftFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
