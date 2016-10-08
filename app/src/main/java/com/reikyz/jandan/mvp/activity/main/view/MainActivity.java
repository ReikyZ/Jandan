package com.reikyz.jandan.mvp.activity.main.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.mvp.activity.main.presenter.MainPresenter;
import com.reikyz.jandan.mvp.activity.main.presenter.MainPresenterImpl;
import com.reikyz.jandan.mvp.base.BaseActivity;
import com.reikyz.jandan.mvp.fragment.flow.view.FlowFragment;
import com.reikyz.jandan.mvp.fragment.list.view.ListFragment;
import com.reikyz.jandan.utils.Utils;
import com.reikyz.jandan.widget.CircleProgressBar;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import at.favre.lib.dali.Dali;
import at.favre.lib.dali.builder.nav.DaliBlurDrawerToggle;
import at.favre.lib.dali.builder.nav.NavigationDrawerListener;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements MainView {

    final String TAG = "==MainActivity==";

    private DrawerLayout mDrawerLayout;
    private DaliBlurDrawerToggle mDrawerToggle;

    private MainPresenter mMainPresenter;

    @Bind(R.id.proBar)
    CircleProgressBar progressBar;
    @Bind(R.id.proBar_top)
    CircleProgressBar progressBarTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mMainPresenter = new MainPresenterImpl(this);

        // init Main Fragment
        mMainPresenter.initFragment(savedInstanceState);

        // 抽屉菜单
        initView();

        preIndex = 0;
        countTime.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        EventBus.getDefault().post(0, EventConfig.SET_TIME);
                    }
                    mHandler.removeMessages(1);
                    mHandler.sendEmptyMessageDelayed(1, 1500);


                    // 过了零点就是新的一天了
                    if (System.currentTimeMillis() / 1000 % (3600 * 24) < 10) {
                        MyApp.timeCount = 0;
                    }

                    long onLine = Long.parseLong(Prefs.getString(Config.ONLINE_TIME));
                    //  Utils.log(TAG, (onLine / 1000 / 3600 / 24) + Utils.getLineNumber(new Exception()));
                    if (System.currentTimeMillis() / 1000 / 3600 / 24 - onLine / 1000 / 3600 / 24 > 0) {
                        MyApp.timeCount = 0;
                    }

            }
        }
    };


    FragmentManager fragmentManager;
    Fragment newsFragment, funPicFragment, girlPicFragment, jokeFragment, videoFragment;

    @Override
    public void initFragment(Bundle savedInstanceState) {
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

    @Override
    public void setSelect(int checkedId) {
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
                if (funPicFragment == null) {
                    funPicFragment = FlowFragment.newInstance(Config.FUN_PIC);
                    fragmentTransaction.add(R.id.container, funPicFragment, Config.FUN_PIC);
                } else {
                    fragmentTransaction.show(funPicFragment);
                }
                break;
            case 2:
                if (girlPicFragment == null) {
                    girlPicFragment = FlowFragment.newInstance(Config.GIRL_PIC);
                    fragmentTransaction.add(R.id.container, girlPicFragment, Config.GIRL_PIC);
                } else {
                    fragmentTransaction.show(girlPicFragment);
                }
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
                if (videoFragment == null) {
                    videoFragment = FlowFragment.newInstance(Config.VIDEO);
                    fragmentTransaction.add(R.id.container, videoFragment, Config.VIDEO);
                } else {
                    fragmentTransaction.show(videoFragment);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);

        progressBarTop.setColorSchemeResources(R.color.red, R.color.blue, R.color.yellow);
        progressBarTop.setVisibility(View.INVISIBLE);
        progressBar.setColorSchemeResources(R.color.red, R.color.blue, R.color.yellow);
        progressBar.setVisibility(View.INVISIBLE);

        mDrawerToggle = Dali.create(this).constructNavToggle(this, mDrawerLayout,
                null, R.string.drawer_open, R.string.drawer_close, new NavigationDrawerListener() {
                    @Override
                    public void onDrawerClosed(View view) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    /** Called when a drawer has settled in a completely open state. */
                    public void onDrawerOpened(View drawerView) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                });
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    long keyDown = System.currentTimeMillis();

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - keyDown) > 1500) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                    Utils.log(TAG, Utils.getLineNumber(new Exception()));
                    return true;
                }
                Utils.showToast(this, "再次点击退出");
                keyDown = System.currentTimeMillis();
                Utils.log(TAG, Utils.getLineNumber(new Exception()));
                return true;
            } else {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
                Utils.log(TAG, Utils.getLineNumber(new Exception()));
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        Prefs.save(Config.COOKIE, "");
    }

    @Override
    public void finish() {
        super.finish();
        System.exit(0);
    }

    @Subscriber(tag = EventConfig.CLOASE_DRAWER)
    void closeDrawer(int i) {
        if (i < 5) {
            mMainPresenter.setSelect(i);
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Subscriber(tag = EventConfig.SHOW_GLOBAL_PRO_TOP)
    void showGlobalProTop(int visual) {
        progressBarTop.setVisibility(visual);
    }

    @Subscriber(tag = EventConfig.SHOW_GLOBAL_PRO)
    void showGlobalPro(int visual) {
        progressBar.setVisibility(visual);
    }
}
