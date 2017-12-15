package com.hrmp.model;

/**
 * Created by Ly on 2017/4/29.
 */

public interface IUserBiz {
    public boolean checkIsFirstLogin();
    public void doLogin(String username, String psd,IThreadListener threadListener);
    public void doBgLogin();
}
