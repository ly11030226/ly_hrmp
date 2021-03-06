package com.hrmp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetUtil {
    private static final String TAG = "NetUtil";

    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        Log.e(TAG, "the net is ok");
                        return true;
                    }
                }
            }
        }
//        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
        return false;
    }
}