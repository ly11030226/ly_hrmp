package com.hrmp;

import android.content.Context;

/**
 * 状态管理者
 */

public class StateManager {
    private StateManager(){}
    private static final String TAG = "StateManager";
    //regist中断之后的重连次数
    private  int reconnectNum =0;	//当前登陆次数，以0做为一次
    private int autoLoginMaxNum=3;  //自动登录最大次数
    public Context context;
    //初始未定义状态
    LOGIN_STATE state = LOGIN_STATE.LS_UNDEFINED;
    public static StateManager stateManager;
    public static StateManager getStateManager(){
        return stateManager;
    }


    public enum LOGIN_STATE{
        LS_UNDEFINED,        //初始化未定义状态
        LS_UNLOGIN,          //未登录状态
        LS_LOGINING,         //正在登录
        LS_LOGINSUC,         //登录成功
        LS_LOGINING_OUT,     //正在注销
        LS_PUBLISHED,        //发布在线状态成功
        LS_PSEUDO_LOGIN,     //界面处于登录后状态，但是后台并没有登录（情况1.程序刚启动，还没有后台登录。 情况2.登录后断网
        LS_PSEUDO_LOGINING,  //从 LS_PSEUDO_LOGIN 发起的登录 (正在登录中）
        LS_PSEUDO_LOGINGSUC, //从 LS_PSEUDO_LOGIN 发起的登录 (登录成功，正在PUBLISH）

    }
    public void initStateManager(Context mContext){
        if (stateManager == null){
            stateManager = new StateManager();
        }
        this.context = mContext;
    }
    public int getReconnectNum(){
        return reconnectNum;
    }
    /**
     * 设置当前登录状态
     */
    public void setState(LOGIN_STATE newState){
        if (state == newState){
            return;
        }
        LOGIN_STATE oldState = state;
        state = newState;

    }
}
