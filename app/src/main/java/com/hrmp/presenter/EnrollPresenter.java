package com.hrmp.presenter;

import android.content.Context;

import com.hrmp.bean.RspMsg;
import com.hrmp.model.IThreadListener;
import com.hrmp.model.WorkMsgBiz;
import com.hrmp.ui.iview.IEnrollView;
import com.hrmp.util.LogUtils;

import java.io.Serializable;

/**
 * Created by Ly on 2017/5/10.
 */

public class EnrollPresenter implements IThreadListener,Serializable{
    private static final String TAG = "EnrollPresenter";
    Context context;
    IEnrollView iEnrollView;
    WorkMsgBiz workMsgBiz;
    public EnrollPresenter(Context context, IEnrollView iEnrollView) {
        this.context = context;
        this.iEnrollView = iEnrollView;
        workMsgBiz = new WorkMsgBiz();
    }
    public void refresh(){
        workMsgBiz.getOnePageWorkMsg(1);
    }
    public void clickWorkMsg(){

    }


    @Override
    public void success(RspMsg rspMsg) {
        if (rspMsg!=null) {
            iEnrollView.getWorkMsgSuccess(rspMsg.getRspDetail().getWorkList());
        }else{
            iEnrollView.getWorkMsgFail("");
        }
    }

    @Override
    public void fail(RspMsg rspMsg) {
        if (rspMsg!=null) {
            iEnrollView.getWorkMsgFail(rspMsg.getRspDesc());
        }else{
            iEnrollView.getWorkMsgFail("");
        }
    }

    public WorkMsgBiz getWorkMsgBiz() {
        return workMsgBiz;
    }

    public void getWorkMsg(int pageNo,int pageSize){
        LogUtils.i(TAG,"getWorkMsg pageNo ... "+pageNo);
        workMsgBiz.getWorkMsg(pageNo,pageSize,this);
    }
}
