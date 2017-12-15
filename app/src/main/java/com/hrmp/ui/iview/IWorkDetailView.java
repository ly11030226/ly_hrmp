package com.hrmp.ui.iview;

import com.hrmp.bean.RspMsg;
import com.hrmp.bean.Work;

import java.util.List;

/**
 *
 */

public interface IWorkDetailView {
    public void showProgress(String desc);
    public void hideProgress();
    public void getSignSuccess(Work workMsg,RspMsg rspMsg);
    public void getSignFail(Work workMsg,RspMsg rspMsg);
    public void getDetailSuccess(List<Work> works);
    public void getDetailFail(String desc);
}
