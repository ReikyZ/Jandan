package com.reikyz.jandan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.reikyz.api.impl.ApiImpl;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.SessionData;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.utils.BitmapUtils;
import com.reikyz.jandan.utils.Utils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.Stack;

import io.fabric.sdk.android.Fabric;


public class MyApp extends Application {

    final static String TAG = "===MyApp===";

    static MyApp instance;
    static Context context;
    static Stack<Activity> activityStack;
    static Activity currentActivity;
    public static IWXAPI iwxapi;
    protected ApiImpl api;

    public synchronized static MyApp getInstance() {
        if (instance == null) {
            instance = new MyApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initImageLoader(this);
        context = getApplicationContext();
        api = new ApiImpl();
        //初始化api相关数据
        SessionData.initApi(context);
        initImageLoader(this);
        regToWX();

        MultiDex.install(context);

        testApi();
    }

    private void testApi() {
        new ResponseSimpleNetTask(this, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.entry("get_recent_posts",
                        "url,date,tags,author,title,comment_count,custom_fields",
                        1,
                        "thumb_c,views",
                        1);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                Utils.log(TAG, "onSucceed");
            }

            @Override
            protected void onFailure() {
                Utils.log(TAG, "onFailure");
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
