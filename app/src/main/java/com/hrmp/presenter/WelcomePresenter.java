package com.hrmp.presenter;


import com.hrmp.Tools;
import com.hrmp.model.IUserBiz;
import com.hrmp.ui.iview.IWelcomeView;

/**
 * Created by Ly on 2017/4/29.
 */

public class WelcomePresenter {
    IWelcomeView iWelcome;
    IUserBiz userBiz;
    public WelcomePresenter(IWelcomeView mIWelcome) {
        this.iWelcome = mIWelcome;
    }

    public boolean checkIsOrNotFirstLogin(){
        return Tools.isFirstLogin();
    }
}
