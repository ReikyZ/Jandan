package com.reikyz.api.impl;

import com.reikyz.api.model.ApiResponse;
import com.reikyz.api.net.ApiParams;
import com.reikyz.api.net.HttpClient;
import com.reikyz.api.utils.ResponseParse;

import java.io.IOException;


public class ApiImpl extends BaseApiImpl {

    ResponseParse paser = new ResponseParse();

    public ApiResponse generalApi(String intent, String include, Integer page, String fields, Integer dev,Integer id) throws IOException {
        return paser.parse(HttpClient.get(
                HttpClient.attachHttpGetParams(
                        GERNERAL_API,
                        ApiParams.gerneralParams(intent, include, page, fields, dev, id))
                , false));
    }

    public ApiResponse tryBD() throws IOException {
        return paser.parse(HttpClient.get(
                "http://baidu.com",
                false
        ));
    }

}
