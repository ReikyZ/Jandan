package com.reikyz.jandan.mvp.activity.main.presenter;

import android.os.Bundle;

/**
 * Created by reikyZ on 16/10/8.
 */

public interface MainPresenter {
    void setSelect(int id);
    void initFragment(Bundle savedInstanceState);
}
