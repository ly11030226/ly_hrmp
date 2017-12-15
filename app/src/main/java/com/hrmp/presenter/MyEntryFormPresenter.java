package com.hrmp.presenter;

import android.content.Context;

import com.hrmp.HRMPApplication;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.Work;
import com.hrmp.model.IThreadListener;
import com.hrmp.ui.iview.IMyEntryFormView;
import com.hrmp.util.RequestBaseUtil;

import java.util.List;

/**
 * Created by Ly on 2017/6/2.
 */

public class MyEntryFormPresenter implements IThreadListener {
    private Context context;
    private IMyEntryFormView iMyEntryFormView;
    public MyEntryFormPresenter(Context context, IMyEntryFormView iMyEntryFormView) {
        this.context = context;
        this.iMyEntryFormView = iMyEntryFormView;
    }

    public void getMyEntryForm(int pageNum,int pageSize){
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setPageNo(String.valueOf(pageNum));
                reqDetail.setPageSize(String.valueOf(pageSize));
                return reqDetail;
            }
        }.sendRequest(HRMPApplication.getInstance().currentUser.getLoginName(),"getMySignList","getMySignList",this);

    }
    public void cancelEroll(String workID){
        iMyEntryFormView.showProgress();
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setWorkId(workID);
                return reqDetail;
            }
        }.sendRequest(HRMPApplication.getInstance().currentUser.getLoginName(), "cancelSign", "cancelSign", new IThreadListener() {
            @Override
            public void success(RspMsg rspMsg) {
                iMyEntryFormView.hideProgress();
                iMyEntryFormView.cancelEnrollSuccess(rspMsg);
            }

            @Override
            public void fail(RspMsg rspMsg) {
                iMyEntryFormView.hideProgress();
                iMyEntryFormView.cancelEnrollFail(rspMsg);
            }
        });
    }

    @Override
    public void success(RspMsg rspMsg) {
        List<Work> result = rspMsg.getRspDetail().getWorkList();
        if (result!=null) {
            iMyEntryFormView.getMyEntryListSuccess(result);
        }else{
            fail(null);
        }
    }


    @Override
    public void fail(RspMsg rspMsg) {
        if (rspMsg!=null) {
            iMyEntryFormView.getMyEntryListFail(rspMsg.getRspDesc());
        }else{
            iMyEntryFormView.getMyEntryListFail("");
        }
    }

    /**
     * 获取之前待付款订单的信息
     * @param workID
     */
    public void getWXpayInfo(String workID){
        iMyEntryFormView.showProgress();
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setWorkId(workID);
                return reqDetail;
            }
        }.sendRequest(HRMPApplication.getInstance().currentUser.getLoginName(), "wxPay", "wxPay", new IThreadListener() {
            @Override
            public void success(RspMsg rspMsg) {
                iMyEntryFormView.hideProgress();
                iMyEntryFormView.getWXpayInfoSuccess(rspMsg);
            }

            @Override
            public void fail(RspMsg rspMsg) {
                iMyEntryFormView.hideProgress();
                iMyEntryFormView.getWXpayInfoFail(rspMsg);
            }
        });
    }
}
