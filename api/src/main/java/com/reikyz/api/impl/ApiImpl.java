package com.reikyz.api.impl;

import android.text.TextUtils;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.net.ApiParams;
import com.reikyz.api.net.HttpClient;
import com.reikyz.api.utils.ResponseParse;

import java.io.IOException;


public class ApiImpl extends BaseApiImpl {

    ResponseParse paser = new ResponseParse();


    // TEST


    public ApiResponse testAPi() throws IOException {
        return paser.parse(HttpClient.get(
                "http://jandan.duoshuo.com/api/threads/listPosts.json?thread_key=comment-3237809",
                false
        ));
    }

    // GET mothed

    public ApiResponse generalApi(String intent, String include, Integer page, String fields, Integer dev, Integer id) throws IOException {
        return paser.parse(HttpClient.get(
                HttpClient.attachHttpGetParams(
                        GERNERAL_API,
                        ApiParams.gerneralParams(intent, include, page, fields, dev, id))
                , false));
    }

    public ApiResponse getInfo() throws IOException {
        return paser.parse(HttpClient.get(HOST_YWZ, false));
    }

    public ApiResponse getDuoshuo(String threads) throws IOException {
        return paser.parse(HttpClient.get(
                HttpClient.attachHttpGetParam(HOST_DUOSHUO, "threads", threads),
                false
        ));
    }

    public ApiResponse getDuoshuoComments(String thread_key) throws IOException {
        return paser.parse(HttpClient.get(
                HttpClient.attachHttpGetParam(GET_COMMENTS, "thread_key", thread_key),
                false
        ));
    }

    // POST method

    public ApiResponse postVote(String acv_ajax, Integer option, String ID, String cookie) throws IOException {
        return paser.parse(HttpClient.post(
                VOTE + "?acv_ajax=" + acv_ajax + "&option=" + option,
                ApiParams.voteBody(ID),
                TextUtils.isEmpty(cookie) ? false : true,
                cookie));
    }

}
