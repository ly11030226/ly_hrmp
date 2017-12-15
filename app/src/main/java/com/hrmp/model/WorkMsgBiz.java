package com.hrmp.model;

import com.hrmp.HRMPApplication;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.Work;
import com.hrmp.util.RequestBaseUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ly on 2017/5/10.
 */

public class WorkMsgBiz implements IWorkMsgBiz,Serializable{
    List<Work> currentWorkMsg;
    @Override
    public void getOnePageWorkMsg(int position) {

    }

    public List<Work> getCurrentWorkMsg() {
        return currentWorkMsg;
    }

    public void setCurrentWorkMsg(List<Work> currentWorkMsg) {
        this.currentWorkMsg = currentWorkMsg;
    }


    /**
     * 获取招工信息
     * @param pageNo
     * @param pageSize
     * @param iThreadListener
     */
    public void getWorkMsg(int pageNo,int pageSize,IThreadListener iThreadListener){
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setPageNo(String.valueOf(pageNo));
                reqDetail.setPageSize(String.valueOf(pageSize));
                return reqDetail;
            }
        }.sendRequest(
                HRMPApplication.getInstance().currentUser.getLoginName(),
                "getWorkList",
                "getWorkList",
                iThreadListener
        );

    }

    public void getWorkListByWorkKind(int pageNo,int pageSize,String workkind,IThreadListener
            iThreadListener){
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setPageNo(String.valueOf(pageNo));
                reqDetail.setPageSize(String.valueOf(pageSize));
                reqDetail.setWorkKind(workkind);
                return reqDetail;
            }
        }.sendRequest(HRMPApplication.getInstance().currentUser.getLoginName
                (),"getWorkDetailList","getWorkDetailList",iThreadListener);
    }
}
