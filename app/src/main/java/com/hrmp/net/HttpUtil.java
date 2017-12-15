package com.hrmp.net;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Ly on 2017/5/19.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";
    public String MethodName = "appLogin";
    Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(final Subscriber<? super String> subscriber) {
            final HttpTransportSE ht = new HttpTransportSE(HttpConstant.URL_TEST);
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SoapObject soapObject = new SoapObject(HttpConstant.NAME_SPACE,MethodName);
            StringBuilder sb = new StringBuilder();
            sb.append("<reqMsg>")
                    .append("<operater>admin</operater>")
                    .append("<identification></identification>")
                    .append("<reqDetail>")
                    .append("<password>111</password>")
                    .append("</reqDetail>")
                    .append("</reqMsg>");
            soapObject.addProperty("appLogin",sb.toString());
            envelope.bodyOut = soapObject;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ht.call("",envelope);
                        if (envelope.getResponse()!=null) {
                            Log.i("result","response != null");
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            Log.i("result",result.toString());
                            subscriber.onNext(result.toString());
//                            Log.i("result","result.getPropertyCount()..."+result.getPropertyCount());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }).observeOn(AndroidSchedulers.mainThread());

}
