package com.reikyz.jandan.mvp.activity.itemPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.mvp.fragment.JokeFragment;
import com.reikyz.jandan.mvp.fragment.PicFragment;
import com.reikyz.jandan.mvp.fragment.WebPageFragment;
import com.reikyz.jandan.mvp.base.BaseActivity;
import com.reikyz.jandan.utils.Utils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by reikyZ on 16/8/24.
 */
public class ItemPagerActivity extends BaseActivity {

    final static String TAG = "==ItemPagerActivity==";

    private String mType;
    private int mIndex;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        mType = getIntent().getStringExtra(Config.TYPE);
        mIndex = getIntent().getIntExtra(Config.INDEX, 0);

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {

                switch (mType) {
                    case Config.NEWS:
                        return WebPageFragment.newInstance(mType, position);
                    case Config.JOKE:
                        return JokeFragment.newInstance(position);
                    case Config.FUN_PIC:
                    case Config.GIRL_PIC:
                        return PicFragment.newInstance(mType, position);
                    case Config.VIDEO:
                        return WebPageFragment.newInstance(mType, position);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                switch (mType) {
                    case Config.NEWS:
                        return MyApp.newsList.size();
                    case Config.JOKE:
                        return MyApp.jokeList.size();
                    case Config.FUN_PIC:
                        return MyApp.funPicList.size();
                    case Config.GIRL_PIC:
                        return MyApp.girlPicLIst.size();
                    case Config.VIDEO:
                        return MyApp.videoLIst.size();
                    default:
                        return 0;
                }

            }
        };
        viewPager.setAdapter(adapter);
        Utils.log(TAG, "Index==" + mIndex + "==total==" + adapter.getCount() + Utils.getLineNumber(new Exception()));
        viewPager.setCurrentItem(mIndex);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (mType) {
                    case Config.NEWS:
                        MyApp.currentNewsIndex = position;
                        if (MyApp.newsList.size() - position < 5) {
                            EventBus.getDefault().post(0, EventConfig.LOAD_MORE_LIST);
                        }
                        break;
                    case Config.JOKE:
                        MyApp.currentJokeIndex = position;
                        if (MyApp.jokeList.size() - position < 5) {
                            EventBus.getDefault().post(0, EventConfig.LOAD_MORE_LIST);
                        }
                        break;
                    case Config.FUN_PIC:
                        MyApp.currentFunPicIndex = position;
                        if (MyApp.funPicList.size() - position < 5) {
                            EventBus.getDefault().post(0, EventConfig.LOAD_MORE_FLOW);
                        }
                        break;
                    case Config.GIRL_PIC:
                        MyApp.currentGirlPicIndex = position;
                        if (MyApp.girlPicLIst.size() - position < 5) {
                            EventBus.getDefault().post(0, EventConfig.LOAD_MORE_FLOW);
                        }
                        break;
                    case Config.VIDEO:
                        MyApp.currentVideoIndex = position;
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscriber(tag = EventConfig.NEWS_CHANGED)
    void newsChanged(int i) {
        adapter.notifyDataSetChanged();
    }

    @Subscriber(tag = EventConfig.DATA_CHANGED)
    void dataChanged(int i) {
        adapter.notifyDataSetChanged();
    }

    @Subscriber(tag = EventConfig.PICS_CHANGED)
    void picsChanged(int i) {
        Utils.log(TAG, mType + "==total==" + adapter.getCount() + Utils.getLineNumber(new Exception()));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(
                R.anim.activity_half_horizonal_entry,
                R.anim.activity_horizonal_exit);
    }

}
