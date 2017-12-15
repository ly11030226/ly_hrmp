package com.hrmp.ui.iview;

import com.hrmp.bean.Work;

import java.util.List;

/**
 * Created by Ly on 2017/5/10.
 */

public interface IEnrollView {
    public void getWorkMsgSuccess(List<Work> work);
    public void getWorkMsgFail(String desc);
}
