package com.hrmp.presenter;

import android.content.Context;

import com.hrmp.HRMPApplication;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.model.IThreadListener;
import com.hrmp.ui.iview.IMessageDetailView;
import com.hrmp.util.RequestBaseUtil;

/**
 * Created by Ly on 2017/9/27.
 */

public class MessageDetailPresenter implements IThreadListener{
    private Context context;
    private IMessageDetailView messageDetailView;
    public MessageDetailPresenter(Context cotext, IMessageDetailView messageDetailView) {
        this.context = context;
        this.messageDetailView = messageDetailView;
    }

    /**
     * 获取消息详情
     */
    public void getMessageDetail(String msgId){
        messageDetailView.showProgress();
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setMsgId(msgId);
                return reqDetail;
            }
        }.sendRequest(
                HRMPApplication.getInstance().currentUser.getLoginName(),
                "getMessageInfo",
                "getMessageInfo",
                this
        );

    }

    @Override
    public void success(RspMsg rspMsg) {
        messageDetailView.hideProgress();
        messageDetailView.getMessageDetailSuccess(rspMsg);

    }

    @Override
    public void fail(RspMsg rspMsg) {
        messageDetailView.hideProgress();
        messageDetailView.getMessageDetailFail(rspMsg);
    }
}
