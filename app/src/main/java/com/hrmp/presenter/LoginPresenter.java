package com.hrmp.presenter;

import android.text.TextUtils;

import com.hrmp.Constant;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.User;
import com.hrmp.model.IThreadListener;
import com.hrmp.model.UserBiz;
import com.hrmp.ui.iview.ILoginView;
import com.hrmp.util.LogUtils;
import com.hrmp.util.SpUtils;


/**
 * Created by Ly on 2017/4/30.
 */

public class LoginPresenter implements IThreadListener{
    ILoginView iLoginView;
    public LoginPresenter(ILoginView mILoginView) {
        this.iLoginView = mILoginView;

    }

    public void login(String username,String psd){
        if (TextUtils.isEmpty(username)){
            iLoginView.usernameOrPsdIsEmpty();
            return;
        }
        if (TextUtils.isEmpty(psd)){
            iLoginView.usernameOrPsdIsEmpty();
            return;
        }
        iLoginView.showProgressBar(Tools.getStringByResouceId(R.string.do_login_ing));
        //因为服务器没有返回密码信息，因此这里暂时保存,为了后台登录的时候用
        SpUtils.putString(Constant.KEY_USER_PSD,psd);
        UserBiz.getInstance().doLogin(username,psd,this);
    }

    @Override
    public void success(RspMsg r) {
        iLoginView.hideProgressBar();
        User user = new User();
        String userId = r.getRspDetail().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            user.setId(Integer.valueOf(userId));
        }else{
            LogUtils.e("hrmp","****** login success but userId is null ******");
        }
        user.setLoginName(r.getRspDetail().getUserLoginName());
        user.setUserName(r.getRspDetail().getUserName());
        iLoginView.loginSuccess(user);
    }

    @Override
    public void fail(RspMsg r) {
        iLoginView.hideProgressBar();
        SpUtils.putString(Constant.KEY_USER_PSD,"");
        iLoginView.loginFail(r.getRspDesc());
    }
    public void checkVersion(){
        Tools.checkVersion(new IThreadListener() {
            @Override
            public void success(RspMsg rspMsg) {
                iLoginView.checkVersionSuccess(rspMsg);
            }

            @Override
            public void fail(RspMsg rspMsg) {
                iLoginView.checkVersionFail(rspMsg);
            }
        });
    }
}
