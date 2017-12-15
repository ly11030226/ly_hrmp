package com.hrmp.presenter;

import android.content.Context;

import com.hrmp.HRMPApplication;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.SignEmp;
import com.hrmp.bean.Work;
import com.hrmp.model.IThreadListener;
import com.hrmp.model.WorkMsgBiz;
import com.hrmp.ui.iview.IWorkDetailView;
import com.hrmp.util.LogUtils;
import com.hrmp.util.RequestBaseUtil;

/**
 * Created by Ly on 2017/5/30.
 */

public class WorkDetailPresenter implements IThreadListener{
    private static final String TAG = "WorkDetailPresenter";
    private Context context;
    private IWorkDetailView iCommonView;
    public WorkDetailPresenter(Context context, IWorkDetailView iCommonView) {
        this.context = context;
        this.iCommonView = iCommonView;
    }

    public void requestData(int pageNo ,int pageSize,String workkind,WorkMsgBiz workMsgBiz){
        workMsgBiz.getWorkListByWorkKind(pageNo,pageSize,workkind,this);
    }

    /**
     * 获取微信支付必要的sign
     */
    public void getSign(Work workMsg){
        iCommonView.showProgress(Tools.getStringByResouceId(R.string.dialog_please_wait));
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                SignEmp signEmp = new SignEmp();
                signEmp.setWorkId(workMsg.getId());
                signEmp.setNum(workMsg.getNum()+"");
                signEmp.setUnitPrice(String.valueOf(workMsg.getUnitPrice()));
                signEmp.setTotalMoney(workMsg.getUnitPrice()*workMsg.getNum()+"");
                reqDetail.setSignEmp(signEmp);
                return reqDetail;
            }
        }.sendRequest(HRMPApplication.getInstance().currentUser.getLoginName(), "sign", "sign", new IThreadListener() {
            @Override
            public void success(RspMsg rspMsg) {
                iCommonView.hideProgress();
                iCommonView.getSignSuccess(workMsg,rspMsg);
            }

            @Override
            public void fail(RspMsg rspMsg) {
                iCommonView.hideProgress();
                iCommonView.getSignFail(workMsg,rspMsg);
            }
        });
    }


    @Override
    public void success(RspMsg rspMsg) {
        LogUtils.i(TAG,"success ... "+Thread.currentThread().getName());
        if (rspMsg!=null) {
            iCommonView.getDetailSuccess(rspMsg.getRspDetail().getWorkList());
        }else{
            iCommonView.getDetailFail("");
        }
    }

    @Override
    public void fail(RspMsg rspMsg) {
        if (rspMsg!=null) {
            iCommonView.getDetailFail(rspMsg.getRspDesc());
        }else{
            iCommonView.getDetailFail("");
        }
    }
}
