package com.hrmp.util;

import android.app.Activity;

/**
 * Created by Ly on 2017/5/14.
 */

public class UITools {
    /**
     * 获取屏幕宽度
     * @param owner
     * @return
     */
    public static  int getWindowWidth(Activity owner)
    {
        return owner.getWindowManager().getDefaultDisplay().getWidth();
    }
}
