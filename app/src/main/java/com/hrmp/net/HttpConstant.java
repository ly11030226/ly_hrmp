package com.hrmp.net;

/**
 * Created by Ly on 2017/5/19.
 */

public class HttpConstant {
    //
    public static final String IP = "192.168.1.105:8080";
    //WebService URL(测试)
    public static final String URL_TEST = "http://"+IP+"/hrmp/ws/appServicePublish?wsdl";
    //WebService URL(正式)
    public static final String URL_RELEASE = "http://www.rhlaowu.com.cn/ws/appServicePublish?wsdl";
    //WebService NAME_SPACE
    public static final String NAME_SPACE = "http://service.app.platform.com";
    //WebService NAME_SPACE
    public static final String NAME_SPACE_2 = "http://"+IP+"/hrmp/ws/appServicePublish";
    public static final String NAME_SPACE_RELEASE = "http://www.rhlaowu.com.cn/ws/appServicePublish";
    //测试更新app的地址
    public static final String TEST_DOWNLOAD_APP = "http://"+IP+"/hrmp/release/hrmp.apk";
}
