package com.hrmp.net;

import android.text.TextUtils;

import com.hrmp.util.LogUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Ly on 2017/5/20.
 */

public class BaseHttpRequester {

    private static final String TAG = "BaseHttpRequester";
    private String methodName;
    private String nameSpace = HttpConstant.NAME_SPACE;
    private String propertyName;
    private String propertyContent;
    private String soapAction;
    public BaseHttpRequester(String methodName,String property,String propertContent,String soapAction){
        this("",methodName,property,propertContent,soapAction);
    }
    public BaseHttpRequester(String nameSpace , String methodName,String property,String propertContent,String soapAction){
        if (!TextUtils.isEmpty(nameSpace)) {
            this.nameSpace = nameSpace;
        }
        this.methodName = methodName;
        this.propertyName = property;
        this.propertyContent = propertContent;
        this.soapAction = soapAction;
    }

    public Observable<String> getResponseValue(){
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final HttpTransportSE ht = new HttpTransportSE(HttpConstant.URL_RELEASE,5000);
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject soapObject = new SoapObject(nameSpace,methodName);
                soapObject.addProperty(propertyName,propertyContent);
                LogUtils.i(TAG,methodName+": request xml ... "+propertyContent);
                envelope.bodyOut = soapObject;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ht.call(soapAction,envelope);
                            if (envelope.getResponse()!=null) {
                                SoapObject result = (SoapObject) envelope.bodyIn;
                                String xml = (String) result.getProperty(0);
                                LogUtils.i(TAG,methodName+": response xml ... "+xml);
                                subscriber.onNext(xml);
                            }
                        } catch (Exception e) {
                            if (e instanceof SocketTimeoutException) {
                                LogUtils.i(TAG,"SocketTimeoutException");
                            }else if (e instanceof UnknownHostException) {
                                LogUtils.i(TAG,"UnknownHostException");
                            }else if (e instanceof ConnectException) {
                                LogUtils.i(TAG,"ConnectException");
                            }else{
                                e.printStackTrace();
                                LogUtils.i(TAG,"exception ... "+e.toString());
                            }
                            subscriber.onError(new Throwable("网络连接有误"));
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
