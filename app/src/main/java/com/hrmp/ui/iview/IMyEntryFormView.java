package com.hrmp.ui.iview;

import com.hrmp.bean.RspMsg;
import com.hrmp.bean.Work;

import java.util.List;

/**
 * Created by Ly on 2017/6/2.
 */

public interface IMyEntryFormView {
    public void getMyEntryListSuccess(List<Work> list);
    public void getMyEntryListFail(String desc);
    public void cancelEnrollSuccess(RspMsg rspMsg);
    public void cancelEnrollFail(RspMsg rspMsg);
    public void showProgress();
    public void hideProgress();
    public void getWXpayInfoSuccess(RspMsg rspMsg);
    public void getWXpayInfoFail(RspMsg rspMsg);

}
