package com.reikyz.jandan.presenter.main;

import android.os.Bundle;

import com.reikyz.jandan.R;
import com.reikyz.jandan.presenter.BaseActivity;


public class MainActivity extends BaseActivity {

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
    }


}
