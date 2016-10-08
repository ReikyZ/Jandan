package com.reikyz.jandan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.view.Gravity;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.reikyz.api.impl.ApiImpl;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.SessionData;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.db.VoteDAOImpl;
import com.reikyz.jandan.model.GeneralPostModel;
import com.reikyz.jandan.model.NewsModel;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.fabric.sdk.android.Fabric;


public class MyApp extends Application {

    final String TAG = "===MyApp===";

    static MyApp instance;
    static Context context;
    static Stack<Activity> activityStack;
    static Activity currentActivity;
    public static IWXAPI iwxapi;
    public static ApiImpl api;

    // DB
    public static VoteDAOImpl voteDAO;

    //DATA
    public static long timeCount = 0;
    public static boolean updateFunCilently = false;
    public static boolean updateGirlCilently = false;
    public static Integer currentNewsIndex, currentFunPicIndex, currentGirlPicIndex, currentJokeIndex, currentVideoIndex;
    public static List<NewsModel> newsList = new ArrayList<>();
    public static List<GeneralPostModel> funPicList = new ArrayList<>();
    public static List<GeneralPostModel> girlPicLIst = new ArrayList<>();
    public static List<GeneralPostModel> jokeList = new ArrayList<>();
    public static List<GeneralPostModel> videoLIst = new ArrayList<>();

    public synchronized static MyApp getInstance() {
        if (instance == null) {
            instance = new MyApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Fabric
//        Fabric.with(this, new Crashlytics());
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);


        initImageLoader(this);
        context = getApplicationContext();
        api = new ApiImpl();
        //初始化api相关数据
        SessionData.initApi(context);
        initImageLoader(this);
        regToWX();

        MultiDex.install(context);

        //内存泄露检测
//        LeakCanary.install(this);
        getInfo();

        intiDB();

//        testApi();
    }

    private void intiDB() {
        voteDAO = VoteDAOImpl.getInstance(context);
    }

    private void testApi() {
        new ResponseSimpleNetTask(this, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.testAPi();
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                Utils.log(TAG, result + Utils.getLineNumber(new Exception()));
            }

            @Override
            protected void onFailure() {
                Utils.log(TAG, "onFailure" + Utils.getLineNumber(new Exception()));
            }
        }.execute();
    }

    private void getInfo() {
        new ResponseSimpleNetTask(this, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.getInfo();
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                if (result.length() > 0) {
                    Prefs.save(Config.YWZ_INFO, result);
                }
            }

            @Override
            protected void onFailure() {
                Utils.log(TAG, "onFailure" + Utils.getLineNumber(new Exception()));
            }
        }.execute();

    }

    /*将该App注册到微信*/
    private void regToWX() {
        iwxapi = WXAPIFactory.createWXAPI(this, getString(R.string.wxapp_id), true);
        iwxapi.registerApp(getString(R.string.wxapp_id));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                context.getString(R.string.img_cahce));
        ImageLoaderConfiguration config = BitmapUtils.getImageLoaderConfig(context, cacheDir);
        ImageLoader.getInstance().init(config);
    }

    /**
     * 添加activity到activity栈中
     *
     * @param activity
     */

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityStack == null) return;
        activityStack.remove(activity);
    }

    /**
     * 退出应用结束所有activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    handler.removeMessages(1);
                    timeCount += 5000;
                    handler.sendEmptyMessageDelayed(1, 5000);
            }
        }
    };

    synchronized public static void startCountTime() {
        handler.sendEmptyMessageDelayed(1, 5000);
    }

    public static void stopCountTime() {
        handler.removeMessages(1);
    }


    public static ApiImpl getApi() {
        return api;
    }

    public static Context getContext() {
        return context;
    }

    public static Stack<Activity> getActivities() {
        return activityStack;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

}
