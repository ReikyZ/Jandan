package com.reikyz.api.net;

import android.support.v4.util.ArrayMap;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by clownqiang on 15/7/21.
 */
public class ApiParams {

    private static final MediaType TYPE_JSON =
            MediaType.parse("application/json; charset=utf-8");
    private static final MediaType TYPE_FORM =
            MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType TYPE_XML =
            MediaType.parse("application/xml; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JPEG =
            MediaType.parse("image/jpeg");
    private static final MediaType MULTIPART_FORM_DATA =
            MediaType.parse("multipart/form-data");
    private static final MediaType MEDIA_TYPE_JPEG_PNG =
            MediaType.parse("jpeg/png");

    public static final int LIMIT = 20;


    /**
     * 生成 URL 参数
     * For GET
     */

    private static String joinParams(ArrayMap paramsMap) {
        if (paramsMap.size() == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (Object key : paramsMap.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            try {
                stringBuilder.append(URLEncoder.encode(String.valueOf(paramsMap.get(key)), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 根据json生成RequestBody
     * For POST
     */
    private static RequestBody jsonToBody(String json) {
        return RequestBody.create(TYPE_JSON, json);
    }

    /**
     * 生成 Form 格式 RequestBody
     * For POST
     */

    private static RequestBody paramsToFormBody(String params) {
        return RequestBody.create(TYPE_FORM, params);
    }

    /**
     * 空白RequestBody
     * FOr POST
     */
    public static RequestBody postBlankParams() {
        return jsonToBody("{}");
    }


}
