package com.reikyz.api.impl;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.net.ApiParams;
import com.reikyz.api.net.HttpClient;
import com.reikyz.api.utils.ResponseParse;

import java.io.IOException;


public class ApiImpl extends BaseApiImpl {

    ResponseParse paser = new ResponseParse();

    public ApiResponse getNews(String intent, String content, int page, String fields, int dev) throws IOException {
        return paser.parse(HttpClient.get(
                HttpClient.attachHttpGetParams(
                        GENERAL_API,
                        ApiParams.gerneralParams(intent, content, page, fields, dev))
                , false));
    }

    public ApiResponse tryBD() throws IOException {
        return paser.parse(HttpClient.get(
                "http://baidu.com",
                false
        ));
    }

}
