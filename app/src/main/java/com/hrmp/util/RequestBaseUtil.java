package com.hrmp.util;

import android.text.TextUtils;

import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.ReqMsg;
import com.hrmp.model.IThreadListener;
import com.hrmp.net.HttpConstant;
import com.thoughtworks.xstream.XStream;

/**
 * Created by Ly on 2017/9/28.
 */

public abstract class RequestBaseUtil {
    private static final String TAG = "RequestBaseUtil";
    private ReqMsg rsqMsg;
    private String operater;
    private String identification;
    private ReqDetail rsqDetail;
    public RequestBaseUtil() {
        rsqMsg = new ReqMsg();
    }
    public void sendRequest(String operater,String method, String property, IThreadListener
            threadListener){
        this.rsqDetail = setReqDetail();
        if (rsqDetail!=null) {
            rsqMsg.setReqDetail(rsqDetail);
            if (!TextUtils.isEmpty(operater)) {
                rsqMsg.setOperater(operater);
            }
            XStream xStream = new XStream();
            xStream.processAnnotations(ReqMsg.class);
            xStream.autodetectAnnotations(true);
            String xml = xStream.toXML(rsqMsg);
            SoapUtil soap = new SoapUtil(HttpConstant.NAME_SPACE_RELEASE,method,property,xml,"",
                    threadListener);
            soap.sendRequest();
        }else{
            throw new IllegalArgumentException("****　set reqDetail is not null　****");
        }
    }
    public abstract ReqDetail setReqDetail();

}
