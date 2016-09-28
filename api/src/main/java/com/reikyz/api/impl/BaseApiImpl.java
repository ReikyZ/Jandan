package com.reikyz.api.impl;


public class BaseApiImpl {

    public static String TAG = "BaseApiImpl";

    protected static String HOST_JANDAN = "http://i.jandan.net";
    protected static String HOST_YWZ = "http://yanwenzi.net/jd.php";
    protected static String HOST_DUOSHUO = "http://jandan.duoshuo.com/api/threads/counts.json";


    protected static String GERNERAL_API = HOST_JANDAN + "/";
    protected static String VOTE = HOST_JANDAN + "/index.php";
    protected static String GET_COMMENTS = "http://jandan.duoshuo.com/api/threads/listPosts.json";
}
