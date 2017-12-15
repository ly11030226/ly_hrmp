package com.hrmp.presenter;

import android.content.Context;

import com.hrmp.HRMPApplication;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.model.IThreadListener;
import com.hrmp.ui.iview.IMsgView;
import com.hrmp.util.RequestBaseUtil;

import java.io.Serializable;

/**
 * Created by Ly on 2017/5/26.
 */

public class MessageListPresenter implements Serializable,IThreadListener{
    private Context context;
    private IMsgView iCommonView;
    //每页最多获取20条记录
    private static final int maxNumPerPage = 20;
    public MessageListPresenter(Context context, IMsgView iCommonView){
        this.context = context;
        this.iCommonView = iCommonView;
    }
    public void getMessageList(int page,int maxNumperPage){
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setPageNo(String.valueOf(page));
                reqDetail.setPageSize(String.valueOf(maxNumperPage));
                return reqDetail;
            }
        }.sendRequest(
                HRMPApplication.getInstance().currentUser.getLoginName(),
                "getMessageList",
                "getMessageList",
                this);

    }
    public void getMessageList(int page){
        this.getMessageList(page,maxNumPerPage);
    }

    @Override
    public void success(RspMsg rspMsg) {
        if (rspMsg!=null) {
            iCommonView.getMsgSuccess(rspMsg.getRspDetail().getMsgList());
        }else{
            iCommonView.getMsgFail("");
        }
    }

    @Override
    public void fail(RspMsg rspMsg) {
        if (rspMsg!=null) {
            iCommonView.getMsgFail(rspMsg.getRspDesc());
        }else{
            iCommonView.getMsgFail("");
        }

    }
}
