package com.hrmp.ui.iview;

import com.hrmp.bean.RspMsg;
import com.hrmp.bean.User;

/**
 * Created by Ly on 2017/4/30.
 */

public interface ILoginView {
    public void login(String username, String psd);
    public void clearUserName();
    public void clearPsd();
    public void showProgressBar(String desc);
    public void hideProgressBar();
    public void usernameOrPsdIsEmpty();
    public void loginSuccess(User user);
    public void loginFail(String content);
    public void checkVersionSuccess(RspMsg rspMsg);
    public void checkVersionFail(RspMsg rspMsg);
}
