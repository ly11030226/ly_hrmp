package com.hrmp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hrmp.HRMPApplication;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.model.IThreadListener;
import com.hrmp.ui.iview.IModifyPsdView;
import com.hrmp.util.RequestBaseUtil;
import com.hrmp.util.ToastUtils;

/**
 * Created by Ly on 2017/5/30.
 */

public class ModifyPsdPresenter implements IThreadListener{
    private Context context;
    private IModifyPsdView iModifyPsdView;
    public ModifyPsdPresenter(Context context, IModifyPsdView iModifyPsdView) {
        this.context = context;
        this.iModifyPsdView = iModifyPsdView;
    }
    public void sendRequest(String old,String n1,String n2){
        iModifyPsdView.showProgressBar();
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setOriginalPwd(old);
                reqDetail.setNewPwd(n1);
                reqDetail.setConfirmPwd(n2);
                return reqDetail;
            }
        }.sendRequest(
                HRMPApplication.getInstance().currentUser.getLoginName(),
                "modifyPassword",
                "modifyPassword",
                this
        );
    }

    @Override
    public void success(RspMsg rspMsg) {
        iModifyPsdView.hideProgressBar();
        ToastUtils.toastLong(R.string.modify_password_success);
        Tools.logout(context);
    }

    @Override
    public void fail(RspMsg rspMsg) {
        iModifyPsdView.hideProgressBar();
        iModifyPsdView.showReminder(rspMsg.getRspDesc());
    }
    private void checkInputContent(String o,String n1,String n2){
        if (TextUtils.isEmpty(o)) {
            iModifyPsdView.showReminder(context.getResources().getString(R.string.remind_old_is_empty));
            return;
        }
        if (TextUtils.isEmpty(n1)) {
            iModifyPsdView.showReminder(context.getResources().getString(R.string.remind_new_is_empty));
            return;
        }
        if (TextUtils.isEmpty(n2)) {
            iModifyPsdView.showReminder(context.getResources().getString(R.string.remind_new_2_is_empty));
            return;
        }
        if (o.equals(n1)) {
            iModifyPsdView.showReminder(context.getResources().getString(R.string.remind_new_is_equals_old));
            return;
        }
        if (n1.equals(n2)) {
            iModifyPsdView.showReminder(context.getResources().getString(R.string.remind_new_is_equals_new2));
            return;
        }
    }
}
