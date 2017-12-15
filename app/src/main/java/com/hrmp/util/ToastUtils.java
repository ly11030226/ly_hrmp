package com.hrmp.util;

/**
 * Created by Ly on 2017/4/30.
 */

import android.widget.Toast;

import com.hrmp.HRMPApplication;

/**
 * Toast工具类
 * 针对系统Toast连续弹出多个的时候会一直显示问题的改造
 * @author huangxiaohu
 *
 */
public class ToastUtils {

    private static Toast toast;

    public static void toastShort(String text){
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(HRMPApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void toastLong(String text){
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(HRMPApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }
    public static void toastShort(int txtRes){
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(HRMPApplication.getInstance().getApplicationContext(), txtRes, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void toastLong(int txtRes){
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(HRMPApplication.getInstance().getApplicationContext(), txtRes, Toast.LENGTH_LONG);
        toast.show();
    }
}