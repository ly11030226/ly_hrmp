package com.hrmp.util;

import com.hrmp.bean.RspMsg;
import com.hrmp.model.IThreadListener;
import com.hrmp.net.BaseHttpRequester;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang3.StringEscapeUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Ly on 2017/5/24.
 */

public class SoapUtil {
    private static final String TAG = "SoapUtil";
    private BaseHttpRequester bhr;
    private IThreadListener iThreadListener;
    public SoapUtil(String methodName, String property, String propertContent, String soapAction, IThreadListener iThreadListener){
        bhr = new BaseHttpRequester(methodName,property,propertContent,soapAction);
        this.iThreadListener = iThreadListener;
    }
    public SoapUtil(String nameSpace,String methodName, String property, String propertContent, String soapAction, IThreadListener iThreadListener){
        bhr = new BaseHttpRequester(nameSpace,methodName,property,propertContent,soapAction);
        this.iThreadListener = iThreadListener;
    }
    public synchronized void sendRequest(){
        Observable<String> observable = bhr.getResponseValue();
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                RspMsg rspMsg = new RspMsg();
                rspMsg.setRspDesc(throwable.getMessage());
                iThreadListener.fail(rspMsg);
            }

            @Override
            public void onNext(String s) {
                try {
                    XStream xStream = new XStream();
                    String escape = StringEscapeUtils.unescapeHtml3(s);
//                    LogUtils.i(TAG,"escape ... "+escape);
                    xStream.processAnnotations(RspMsg.class);//显示声明使用注解
                    xStream.autodetectAnnotations(true);
                    RspMsg rspMsg_ = (RspMsg) xStream.fromXML(escape);
//                    RspMsg rspMsg = ParserXMLUtil.getResponseResult(s);
                    checkResult(rspMsg_,iThreadListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void checkResult(RspMsg result,IThreadListener threadListener) {
        switch (Integer.valueOf(result.getRspResult())) {
            case 4000:  //非空参数为空
            case 4001:  //用户名、密码不正确
            case 4002:  //用户不存在
            case 4003:  //短信验证码不正确
            case 4004:  //未登陆
            case 4009:  //
                threadListener.fail(result);
                break;
            case 1000:  //用户有效且验证码正确
                threadListener.success(result);
                break;
            default:
                break;
        }
    }
}
