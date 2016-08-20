package com.reikyz.jandan.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.annotation.JSONField;
import com.reikyz.jandan.presenter.main.MainActivity;
import com.reikyz.jandan.MyApp;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Prefs;
import com.reikyz.jandan.widget.CircleProgressBar;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final double EARTH_RADIUS = 6378137;
    private static final int THUMB_SIZE = DentistyConvert.dp2px(100);

    /**
     * 微信分享
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    public static void wechatShare(int flag, String imagePath, float scale) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(imagePath);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.title = "饭团分享测试";
        msg.description = "好好次~~";
        //这里替换一张自己工程里的图片资源
        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, (int) (THUMB_SIZE / scale), THUMB_SIZE, true);
        bmp.recycle();
        msg.setThumbImage(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        MyApp.iwxapi.sendReq(req);
    }

    /**
     * 微信分享
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    public static void wechatShareWeb(int flag, Bitmap image, String url, String title, String describ) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        // title description 不接受空字符串，不会报错，不能发送会话
        msg.title = title.equals("") ? "吃啥" : title;
        msg.description = describ.equals("") ? "吃啥" : describ;
        msg.setThumbImage(image);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        MyApp.iwxapi.sendReq(req);
    }

    /**
     * 获取屏幕DisplayMetrics，然后得到相应的长宽
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics;
    }

    /**
     * 获取actionbar的高度
     *
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight;
        /*TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        actionBarHeight = context.getResources().getDimensionPixelSize(tv.resourceId);*/
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarHeight;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 显示Toast
     */
    public static void showToast(Context context, String str) {
        if (context != null) {
            View toastView = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
            TextView tvToast = (TextView) toastView.findViewById(R.id.tv_toast);
            tvToast.setText(str);
            Toast toast = new Toast(context);
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 创建加载弹框
     */
    public static ProgressDialog showSpinnerDialog(Activity activity) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage(MyApp.getContext().getString(R.string.loading));
        if (!activity.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }


    public static Dialog createProgressDialog(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.view_progress_dialog, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll);
        CircleProgressBar progressBar = (CircleProgressBar) view.findViewById(R.id.proBar);
        progressBar.setColorSchemeResources(R.color.blue, R.color.red, R.color.yellow);
        Dialog dialog = new Dialog(activity, R.style.circle_progress_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return dialog;
    }


    public static void hideSoftInput(View view, Context context) {
        // 先隐藏键盘
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInput(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 复制到剪切板
     */
    public static void copToClipBoard(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content);
    }

    /**
     * 根据Uri获取真实的文件地址
     */
    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String path;
        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    /**
     * 初始化Indicator
     */
    public static CirclePageIndicator initCirclePagerIndicator(CirclePageIndicator indicator) {
        Context context = MyApp.getContext();
        final float density = context.getResources().getDisplayMetrics().density;
        indicator.setRadius(density * 3);
        indicator.setStrokeWidth(0);
        indicator.setPageColor(context.getResources().getColor(R.color.alpha_grey));
        indicator.setStrokeColor(context.getResources().getColor(R.color.alpha_grey));
        indicator.setFillColor(context.getResources().getColor(R.color.white));
        return indicator;
    }

    /**
     * 验证是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成长度为30的随机字符串，防止重复
     */
    public static String getRandomString() { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 30; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机产生一个color
     */
    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    /**
     * 发送notification
     */
    public static void setNotifyType(int iconId, String message, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = "有一条饭团消息";
        Intent notifyIntent;
        if (null == MyApp.getCurrentActivity()) {
            notifyIntent = new Intent(context, MainActivity.class);
        } else {
            notifyIntent = new Intent(context, MyApp.getCurrentActivity().getClass());
        }
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setTicker(message)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setContentText(message).build();
        notificationManager.notify(0, notification);
    }

    public static String attachString(String word, int value) {
        return word + " （" + value + "）";
    }

    /**
     * 去除TextView下划线
     */
    public static void stripUnderlines(TextView textView) {
        SpannableString s = (SpannableString) textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public static void log(String tag, String result) {
        if (MyApp.getContext().getString(R.string.isDeveloping).equals("true"))
            Log.e(tag, "==sssss==" + result + "==" + getLineNumber(new Exception()));
    }

    public static String getLineNumber(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) return "==" + -1 + "=="; //
        return "==" + trace[0].getLineNumber() + "==";
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    public static float convertToDegree(String stringDMS) {
        float result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = Double.valueOf(stringD[0]);
        Double D1 = Double.valueOf(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = Double.valueOf(stringM[0]);
        Double M1 = Double.valueOf(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = Double.valueOf(stringS[0]);
        Double S1 = Double.valueOf(stringS[1]);
        Double FloatS = S0 / S1;

        result = (float) (FloatD + (FloatM / 60) + (FloatS / 3600));
        return result;
    }

    public static String attachCoordinate(double longitude, double latitude) {
        return longitude + "," + latitude;
    }

    public static String attachSpan(double span_long, double span_lati) {
        return span_long + "," + span_lati;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * bytes转化为Bytes
     */
    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing
        return bytes;
    }

    /**
     * Bytes转化为bytes
     */
    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }


    /**
     * 获取四分之一屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getQuarterWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    /**
     * 根据size均分屏幕宽度
     *
     * @param context
     * @param size
     * @return
     */
    public static int getAverageWidth(Context context, int size) {
        return context.getResources().getDisplayMetrics().widthPixels / size;
    }

    /**
     * 判断当前是否是wifi连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 设置字体
     *
     * @param context
     * @param textView
     * @param font     首3个字母
     */

    public static void setFont(Context context, TextView textView, String font) {
        Typeface face;
        switch (font) {
            case "hel":
                face = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue.ttf");
                break;
            default:
                face = Typeface.DEFAULT;
                break;
        }
        textView.setTypeface(face);
    }


    public static LayoutAnimationController getListViewControl() {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }

    public static String getIpAddress(Context context) {
        //获取wifi服务

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        //判断wifi是否开启

        if (!wifiManager.isWifiEnabled()) {

            wifiManager.setWifiEnabled(true);

        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        int ipAddress = wifiInfo.getIpAddress();

        String ip = (ipAddress & 0xFF) + "." +

                ((ipAddress >> 8) & 0xFF) + "." +

                ((ipAddress >> 16) & 0xFF) + "." +

                (ipAddress >> 24 & 0xFF);

        return ip;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getUUID() {

        if (Prefs.getString("UUID") != null) return Prefs.getString("UUID");

        final TelephonyManager tm = (TelephonyManager) MyApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;


        tmDevice = "" + tm.getDeviceId();

        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(MyApp.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        String uniqueId = deviceUuid.toString();

        StringBuffer sb = new StringBuffer();
        if (uniqueId != null) {
            for (int i = 0; i < uniqueId.length(); i++) {
                char c = uniqueId.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(c);
                } else if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        Prefs.save("UUID", sb.toString());
        return sb.toString();
    }

    public static String toUpperCase(String str) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(c);
                } else if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static boolean canScrollDown(View view) {
        return ViewCompat.canScrollVertically(view, 1);
    }

    public static boolean canScrollUp(View view) {
        return ViewCompat.canScrollVertically(view, -1);
    }

    public static String getSign(Object obj, String key) throws Exception {
        TreeMap<String, String> map = new TreeMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            Object object = field.get(obj);

            if (field.getAnnotation(JSONField.class) != null
                    && field.getAnnotation(JSONField.class).name() != null) {
                if (object != null) {
                    Log.e("sssss", "===" + field.getAnnotation(JSONField.class).name() + "===" + field.get(obj).toString());
                    map.put(field.getAnnotation(JSONField.class).name(), object.toString());
                }
            } else {
                if (object != null) {
                    Log.e("sssss", "===" + field.getName() + "===" + field.get(obj).toString());
                    map.put(field.getName(), object.toString());
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        Iterator titer = map.entrySet().iterator();
        while (titer.hasNext()) {
            Map.Entry ent = (Map.Entry) titer.next();
            String keyt = ent.getKey().toString();
            String valuet = ent.getValue().toString();
            sb.append("&");
            sb.append(keyt);
            sb.append("=");
            sb.append(valuet);
        }
        sb.append("&");
        sb.append("key=");
        sb.append(key);
        return MD5.GetMD5Code(sb.toString().substring(1)).toUpperCase();
    }

    public static String beanToXml(Object object) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Field field : object.getClass().getDeclaredFields()) {
            Object attr = field.get(object);
            if (attr != null) {
                sb.append("<");
                sb.append(field.getName());
                sb.append(">");
                sb.append(field.get(object));
                sb.append("</");
                sb.append(field.getName());
                sb.append(">");
            }
        }
        sb.append("</xml>");
        Log.e("sssss", "XML===" + sb.toString());
        return sb.toString();
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


}
