package com.reikyz.jandan.presenter.itempager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.presenter.BaseActivity;
import com.reikyz.jandan.presenter.newsdetailfragment.NewsDetailFragment;
import com.reikyz.jandan.utils.Utils;

/**
 * Created by reikyZ on 16/8/24.
 */
public class ItemPagerActivity extends BaseActivity {

    final static String TAG = "==ItemPagerActivity==";

    private int mIndex;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex = getIntent().getIntExtra(Config.INDEX, 0);

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {

                Utils.log(TAG, "" +  Utils.getLineNumber(new Exception()));
                return NewsDetailFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return MyApp.newsList.size();

            }
        });

        viewPager.setCurrentItem(mIndex);

        Utils.log(TAG, "" +  Utils.getLineNumber(new Exception()));
    }
}
