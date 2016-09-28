package com.reikyz.api.net;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.reikyz.api.utils.HttpHelper;
import com.reikyz.api.utils.SessionData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpClient {

    final static String TAG = "==HttpClient==";

    private final static String ENCODE_TYPE = "UTF-8";
    private final static OkHttpClient client = getUnsafeOkHttpClient();
    private static final OkHttpClient htmlClient = getUnsafeOkHttpClient();

    static {
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        htmlClient.setConnectTimeout(15, TimeUnit.SECONDS);
    }


    public static void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (null, trustManagerFactory.getTrustManagers(), new SecureRandom()
                    );
            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Request Get
     */
    public static Request getMethodRequest(String url, boolean isHeader) {
        Context context = SessionData.getContext();
        PackageManager packageManager = context.getPackageManager();
        if (isHeader && !SessionData.getAccessToken().isEmpty())
            return BaseBuilder(url)
                    .build();
        return BaseBuilder(url).build();
    }

    /**
     * 获取Request Post
     */
    public static Request postMethodRequest(String url, RequestBody body, boolean isHeader) {
        Context context = SessionData.getContext();
        PackageManager packageManager = context.getPackageManager();
        if (isHeader)
            return BaseBuilder(url).post(body)
                    .addHeader("Cookie", SessionData.getCookie())
                    .addHeader("Cookie2", "$Version=1")
                    .build();
        return BaseBuilder(url).post(body).build();
    }

    /**
     * Post with Cookie
     */

    public static Request postMethodWithCookieRequest(String url, RequestBody body, String cookie) {
        return BaseBuilder(url).post(body)
                .addHeader("Cookie", cookie)
                .addHeader("Cookie2", "$Version=1")
                .build();
    }


    private static Request.Builder BaseBuilder(String url) {
        return new Request.Builder().url(url).header("User-Agent", HttpHelper.userAgent());
    }

    /**
     * 获得Request Download
     */
    public static Request downloadMethodRequest(String url) {
        Context context = SessionData.getContext();
        PackageManager packageManager = context.getPackageManager();
        return BaseBuilder(url).build();
    }

    /**
     * 实际使用的方法是下面几个
     */
    public static Response get(String url, boolean isHeader) throws IOException {
        return execute(getMethodRequest(url, isHeader), isHeader);
    }

    public static Response post(String url, RequestBody body, boolean isHeader) throws IOException {
        return execute(postMethodRequest(url, body, isHeader), isHeader);
    }

    public static Response post(String url, RequestBody body, boolean isHeader, String cookie) throws IOException {
        return execute(postMethodWithCookieRequest(url, body, cookie), isHeader);
    }

    public static Response getHtml(String url, boolean isHeader) throws IOException {
        return executeHtml(getMethodRequest(url, isHeader), isHeader);
    }

    public static Response download(String url) throws IOException {
        return execute(downloadMethodRequest(url), false);
    }


    /**
     * 异步获取
     */
    public static void getAsync(String url, boolean isHeader, Callback callback) {
        enqueue(getMethodRequest(url, isHeader), callback);
    }

    public static void postAsync(String url, RequestBody requestBody, boolean isHeader, Callback callback) {
        enqueue(postMethodRequest(url, requestBody, isHeader), callback);
    }

    /**
     * 下面是基础方法，需要特殊使用时自己组装
     * <p>
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request, boolean isHeader) throws IOException {
        if (!isHeader) {
            try {
                return client.newCall(request).execute();
            } catch (Exception e) {
                return null;
            }
        } else {
            return client.newCall(request).execute();
        }
    }

    public static Response executeHtml(Request request, boolean isHeader) throws IOException {
        if (!isHeader) {
            return htmlClient.newCall(request).execute();
        } else {
            return client.newCall(request).execute();
        }
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    /**
     * 这里使用了HttpClinet的API。只是为了方便
     *
     * @param paramsMap
     * @return
     */
    // 拼接参数列表
    private static String joinParams(ArrayMap paramsMap) {
        if (paramsMap.size() == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (Object key : paramsMap.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            try {
                stringBuilder.append(URLEncoder.encode(String.valueOf(paramsMap.get(key)), ENCODE_TYPE));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    private static String joinParamsWithoutNull(ArrayMap paramsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object key : paramsMap.keySet()) {
            if (paramsMap.get(key) != null) {
                stringBuilder.append(key);
                stringBuilder.append("=");
                try {
                    stringBuilder.append(URLEncoder.encode(String.valueOf(paramsMap.get(key)), ENCODE_TYPE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                stringBuilder.append("&");
            }
        }
        Log.i("params", stringBuilder.substring(0, stringBuilder.length() - 1));
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, ArrayMap params) {
        return url + "?" + joinParams(params);
    }

    public static String attachHttpGetParamsWithoutNull(String url, ArrayMap params) {
        return url + "?" + joinParamsWithoutNull(params);
    }


    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     *
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }


    static class TrustEveryoneManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null);
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });


            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLineNumber(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) return -1; //
        return trace[0].getLineNumber();
    }

}
