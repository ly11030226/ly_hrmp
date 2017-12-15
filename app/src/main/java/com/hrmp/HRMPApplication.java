package com.hrmp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.hrmp.bean.User;
import com.hrmp.service.LogService;
import com.hrmp.util.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.LinkedList;
import java.util.List;


public class HRMPApplication extends Application {
    private static final String TAG = "HRMPApplication";
    private List<Activity> mList = new LinkedList<Activity>();
    public static HRMPApplication applicationInstance;
    public static HRMPApplication getInstance(){
        return applicationInstance;
    }
    private RefWatcher refWatcher;
    public User currentUser;
    public static final String TENCENT_APP_ID= "f3414aa8fb";
    public static final String TENCENT_APP_KEY= "7218811a-c852-41d0-bbe2-29a95241bc81";
    public static IWXAPI msgApi = null;
    //友盟推送注册
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        initBugly();
        initWeixinPay();
        initUmeng();
        refWatcher = LeakCanary.install(this);
        LogUtils.i(TAG,"********HRMPApplication onCreate*******");
        try {
//            StateManager.getStateManager().initStateManager(this);
            Intent logService = new Intent (this, LogService.class);
            startService(logService);
            String path = HRMPApplication.getInstance().getApplicationInfo().dataDir;
            LogUtils.i(TAG,path);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initUmeng() {
        //友盟统计
        MobclickAgent.setDebugMode(BuildConfig.OPEN_UMENG_DEBUG);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        // MobclickAgent.setAutoLocation(true);
        // MobclickAgent.setSessionContinueMillis(1000);
        // MobclickAgent.startWithConfigure(
        // new UMAnalyticsConfig(mContext, "4f83c5d852701564c0000011", "Umeng",
        // EScenarioType.E_UM_NORMAL));
        MobclickAgent.setScenarioType(applicationInstance, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //友盟推送
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(BuildConfig.OPEN_UMENG_DEBUG);
    }

    private void initWeixinPay() {
        msgApi = WXAPIFactory.createWXAPI(applicationInstance, null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.WEIXIN_REPAY_APPID);
    }

    private void initBugly() {
        //true表示打开debug模式，false表示关闭调试模式
        Bugly.init(getApplicationContext(), "注册时申请的APPID", false);
    }

    /**
     *  exit Activity
     *  @param code code=1:清除activity，不关闭程序，code=0：完全退出程序
     */

    public void exit(int code) {
        try {
            for (Activity activity : mList) {
                if (activity != null && !activity.isFinishing()) {
//					Log.e(TAG, "activityname = " + activity.getLocalClassName());
                    activity.finish();
                }
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(code==0){
                System.exit(code);
            }
        }
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mList != null) {

            if (mList.contains(activity)) {
                mList.remove(activity);
            }
        }

    }
    public static RefWatcher getRefWatcher(Context context){
        HRMPApplication application = (HRMPApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    public void setCurrentUser(User user){
        this.currentUser = user;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
