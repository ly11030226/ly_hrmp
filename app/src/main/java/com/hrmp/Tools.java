package com.hrmp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.User;
import com.hrmp.model.IThreadListener;
import com.hrmp.ui.LoginActivity;
import com.hrmp.util.LogUtils;
import com.hrmp.util.RequestBaseUtil;
import com.hrmp.util.SpUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;

import static com.hrmp.Constant.KEY_USER_ID;
import static com.hrmp.Constant.KEY_USER_LOGIN;
import static com.hrmp.Constant.KEY_USER_LOGINNAME;
import static com.hrmp.Constant.KEY_USER_PSD;
import static com.hrmp.Constant.KEY_USER_USERNAME;

/**
 * Created by Ly on 2017/5/20.
 */

public class Tools {
    public static boolean isFirstLogin() {
        String userId = SpUtils.getString(KEY_USER_ID,"");
        LogUtils.i(userId);
        String loginName = SpUtils.getString(KEY_USER_LOGINNAME, "");
        LogUtils.i(loginName);
        String userName = SpUtils.getString(KEY_USER_USERNAME, "");
        LogUtils.i(userName);
//        String userpsd = sharedPreferences.getString(KEY_USER_PSD, "");
        //如果用户id为空证明第一次登陆
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(loginName)) {
            return true;
        } else {
            User user = new User();
            user.setId(Integer.valueOf(userId));
            user.setLoginName(loginName);
            user.setUserName(userName);
            HRMPApplication.getInstance().setCurrentUser(user);
            return false;
        }
    }
    public static void saveUserToFile(User user){
        SharedPreferences sharedPreferences = HRMPApplication.getInstance()
                .getSharedPreferences(KEY_USER_LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, String.valueOf(user.getId()));
        editor.putString(KEY_USER_LOGINNAME,user.getLoginName());
        editor.putString(KEY_USER_USERNAME,user.getUserName());
        editor.commit();
        HRMPApplication.getInstance().setCurrentUser(user);
    }
    public static void clearUserInfo(){
        SharedPreferences sharedPreferences = HRMPApplication.getInstance()
                .getSharedPreferences(KEY_USER_LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 通过resouceid获取字符串
     *
     * @param id
     * @return
     */
    public static String getStringByResouceId(int id) {
        return HRMPApplication.getInstance().getResources().getString(id);
    }

    /**
     * 退出
     * @return
     */
    public static void logout(Context context){
        try {
            //解绑友盟PUSH
            PushAgent mPushAgent = PushAgent.getInstance(context);
            mPushAgent.deleteAlias(SpUtils.getString(KEY_USER_ID,""),"hrmpId");
            MobclickAgent.onProfileSignOff();
            SpUtils.putString(KEY_USER_ID,"");
            SpUtils.putString(KEY_USER_LOGINNAME,"");
            SpUtils.putString(KEY_USER_USERNAME,"");
            SpUtils.putString(KEY_USER_PSD,"");
            HRMPApplication.getInstance().setCurrentUser(null);
            AppManager.getInstance().finishAllActivity();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看是否有更新
     */
    public static void checkVersion(IThreadListener threadListener){
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                return reqDetail;
            }
        }.sendRequest("","queryAndroidVersion", "queryAndroidVersion",threadListener);
    }

    /**
     * 安装apk
     * @param filename
     */
    public static void installApk(Context context,String filename){
        File file = new File(filename);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        context.startActivity(intent);
    }
    /**
     * 安装apk
     * @param file
     */
    public static void installApk(Context context,File file){
        Intent intent =new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context,"com.hrmp.fileProvider",file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到系统设置界面
     * @param activity
     */
    public static void jumpSystemSet(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", HRMPApplication.getInstance().getPackageName(), null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        activity.startActivity(intent);
    }
}
