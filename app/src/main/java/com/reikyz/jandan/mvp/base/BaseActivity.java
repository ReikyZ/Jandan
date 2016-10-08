package com.reikyz.jandan.mvp.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.reikyz.api.impl.ApiImpl;
import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.utils.JsonUtils;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.async.ResponseSimpleNetTask;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.data.EventConfig;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.model.GeneralPostModel;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected static final String TAG = "===BaseActivity===";
    private static final String FOREGROUND = "isCurrentRunningForeground";
    protected MyApp myApp;
    protected ApiImpl api;
    protected ActionBar actionBar;

    protected UpdateCountTime countTime = new UpdateCountTime(60 * 1000, 1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    private void init() {
        myApp = MyApp.getInstance();
        api = new ApiImpl();
        myApp.setCurrentActivity(this);
        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean isCurrentRunningForeground = isRunningForeground();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putBoolean(FOREGROUND, isCurrentRunningForeground).apply();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    public boolean isRunningForeground() {
        String packageName = getPackageName(this);
        String topActivityClassName = getTopActivityName(this);
        if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AVAnalytics.onPause(this);
        MyApp.stopCountTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AVAnalytics.onResume(this);
        MyApp.startCountTime();
        Prefs.save(Config.ONLINE_TIME, System.currentTimeMillis());
    }

    private String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        //即最多取得的运行中的任务信息(RunningTaskInfo)数量
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        //按下Home键盘后 topActivityClassName=com.android.launcher2.Launcher
        return topActivityClassName;
    }

    private String getPackageName(Context context) {
        return context.getPackageName();
    }


    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(ketboardContext);

            if (heightDiff - 100 <= contentViewTop) {
                onHideKeyboard();

                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);

                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);
            }
        }
    };

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private Context ketboardContext;

    protected void onShowKeyboard(int keyboardHeight) {
    }

    protected void onHideKeyboard() {
    }

    protected void attachKeyboardListeners(Context context, int root_id) {
        if (keyboardListenersAttached) {
            return;
        }

        ketboardContext = context;
        rootLayout = (ViewGroup) findViewById(root_id);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        myApp.setCurrentActivity(null);
        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }


    protected class UpdateCountTime extends CountDownTimer {

        public UpdateCountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            countTime.start();
            if (MyApp.updateFunCilently || MyApp.updateGirlCilently) {
                if (preIndex >= 10) preIndex = 0;
                if (preIndex % 2 == 0) {
                    getData(Config.API_GET_FUN_PICS, preIndex / 2 + 1);
                } else {
                    getData(Config.API_GET_GIRL_PIS, (preIndex + 1) / 2);
                }
            }
        }
    }

   protected int preIndex = 0;

    private void getData(final String type, final Integer page) {

        new ResponseSimpleNetTask(this, false) {
            @Override
            protected ApiResponse doInBack() throws Exception {
                return api.generalApi(
                        type, null, page, null, null, null);
            }

            @Override
            protected void onSucceed(String result) throws Exception {
                String results = JsonUtils.getString(new JSONObject(result), "comments");
                List<GeneralPostModel> tmpList = JSON.parseArray(results, GeneralPostModel.class);

                ImageView iv = new ImageView(BaseActivity.this);

                for (int i = 0; i < tmpList.size(); i++) {
                    final GeneralPostModel picModel = tmpList.get(i);
                    if (picModel.getPics() != null &&
                            picModel.getPics().size() > 0 &&
                            picModel.getPics().get(0) != null)
                        iv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(picModel.getPics().get(0), EventConfig.PRELOAD_IMG);

                            }
                        }, 1000 * i);
                }

                preIndex++;
            }

        }.execute();
    }

}
