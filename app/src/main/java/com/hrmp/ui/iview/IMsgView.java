package com.hrmp.ui.iview;

import com.hrmp.bean.Msg;

import java.util.List;

/**
 * Created by Ly on 2017/5/10.
 */

public interface IMsgView {
    public void getMsgSuccess(List<Msg> msg);
    public void getMsgFail(String desc);
}
