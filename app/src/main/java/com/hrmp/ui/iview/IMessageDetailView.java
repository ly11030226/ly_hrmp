package com.hrmp.ui.iview;

import com.hrmp.bean.RspMsg;

/**
 * Created by Ly on 2017/9/27.
 */

public interface IMessageDetailView {
    public void showProgress();
    public void hideProgress();
    public void getMessageDetailSuccess(RspMsg rspMsg);
    public void getMessageDetailFail(RspMsg rspMsg);

}
