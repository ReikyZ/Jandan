package com.reikyz.jandan.async;

import android.os.AsyncTask;

public class TaskUtils {

    public static void cancelTask(ResponseSimpleNetTask task) {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true); // 如果Task还在运行，则先取消它
        }
    }
}
