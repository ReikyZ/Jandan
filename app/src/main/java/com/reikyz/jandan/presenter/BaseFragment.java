package com.reikyz.jandan.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.reikyz.api.impl.ApiImpl;


public class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected ActionBar actionBar;
    protected ApiImpl api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        api = new ApiImpl();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AppCompatActivity) {
            actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        AVAnalytics.onFragmentEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
//        AVAnalytics.onFragmentStart(TAG);
    }


}
