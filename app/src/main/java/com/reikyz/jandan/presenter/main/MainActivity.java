package com.reikyz.jandan.presenter.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.presenter.BaseActivity;
import com.reikyz.jandan.presenter.BaseFragment;
import com.reikyz.jandan.presenter.listfragment.ListFragment;
import com.reikyz.jandan.utils.Utils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    final static String TAG = "==MainActivity==";

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        // init Main Fragment
        initFragment(savedInstanceState);

        // 抽屉菜单
        initView();
    }


    FragmentManager fragmentManager;
    Fragment newsFragment, funPicFragment, girlPicFragment, jokeFragment, videoFragment;

    private void initFragment(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            setSelect(0);
        } else {
            newsFragment = fragmentManager.findFragmentByTag(Config.FRAGMENT_NEWS);
            funPicFragment = fragmentManager.findFragmentByTag(Config.FRAGMENT_FUN_PIC);
            girlPicFragment = fragmentManager.findFragmentByTag(Config.FRAGMENT_GIRL_PIC);
            jokeFragment = fragmentManager.findFragmentByTag(Config.FRAGMENT_JOKE);
            videoFragment = fragmentManager.findFragmentByTag(Config.FRAGMENT_VIDEO);
        }
    }


    private void setSelect(int checkedId) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utils.hideFragment(fragmentManager, fragmentTransaction);
        switch (checkedId) {
            case 0:
                if (newsFragment == null) {
                    newsFragment = ListFragment.newInstance(Config.FRAGMENT_NEWS);
                    fragmentTransaction.add(R.id.container, newsFragment, Config.FRAGMENT_NEWS);
                } else {
                    fragmentTransaction.show(newsFragment);
                }
                break;
            case 1:

                break;
            case 2:
                break;
            case 3:
                if (jokeFragment == null) {
                    jokeFragment = ListFragment.newInstance(Config.FRAGMENT_JOKE);
                    fragmentTransaction.add(R.id.container, jokeFragment, Config.FRAGMENT_JOKE);
                } else {
                    fragmentTransaction.show(jokeFragment);
                }
                break;
            case 4:


                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        super.finish();
        System.exit(0);
    }

    @Subscriber(tag = EventConfig.CLOASE_DRAWER)
    void closeDrawer(int i) {
        if (i < 5)
            setSelect(i);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

}
