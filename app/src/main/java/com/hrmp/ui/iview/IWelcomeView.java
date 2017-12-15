package com.hrmp.ui.iview;

/**
 * Created by Ly on 2017/4/29.
 */

public interface IWelcomeView {
    boolean checkIsFirstLogin();
    void toJumpMainActivity();
    void toJumpLoginActivity();
}
