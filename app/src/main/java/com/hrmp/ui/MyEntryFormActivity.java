package com.hrmp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.hrmp.Constant;
import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.adapter.MyEntryAdapter;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.WXPay;
import com.hrmp.bean.Work;
import com.hrmp.presenter.MyEntryFormPresenter;
import com.hrmp.presenter.WorkDetailPresenter;
import com.hrmp.ui.iview.IMyEntryFormView;
import com.hrmp.ui.iview.IWorkDetailView;
import com.hrmp.util.DialogBuilder;
import com.hrmp.util.LogUtils;
import com.hrmp.util.ToastUtils;
import com.hrmp.view.Titlebar;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的报名
 */
public class MyEntryFormActivity extends MyBaseActivity implements IMyEntryFormView,MyEntryAdapter
        .CusOnItemOnClickListener,IWorkDetailView{

    private static final String TAG = "MyEntryFormActivity";
    private List<Work> workMsgs = new ArrayList<Work>();
    private MyEntryFormPresenter myEntryFormPresenter;
    private MyEntryAdapter myEntryAdapter;
    private Titlebar titlebar;
    private PtrClassicFrameLayout ptr;
    private RecyclerView rcl;
    private int pageNum = 1;
    private int pageSize = 20;
    private WorkDetailPresenter workDetailPresenter;
    private Work clickWork;
    private int currentHandlerWorkStatus = -1;
    private static final int MASK_PREPARE_PAY = 2;   //继续支付
    private static final int MASK_CANCEL_PAY = 1;    //支付取消
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_entry_form);
        try {
            initTitlebar();
            init();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addListener() {
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG,"onLoadMoreBegin");
                myEntryFormPresenter.getMyEntryForm(++pageNum,pageSize);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG,"onRefreshBegin");
                pageNum = 1;
                myEntryFormPresenter.getMyEntryForm(pageNum,pageSize);
            }
        });
    }


    private void init() {
        ptr = (PtrClassicFrameLayout) findViewById(R.id.ptr_my_entry);
        rcl = (RecyclerView) findViewById(R.id.rcl_my_entry);
        myEntryFormPresenter = new MyEntryFormPresenter(MyEntryFormActivity.this, this);
        myEntryAdapter = new MyEntryAdapter(MyEntryFormActivity.this, workMsgs);
        rcl.setLayoutManager(new LinearLayoutManager(MyEntryFormActivity.this));
        rcl.setAdapter(myEntryAdapter);
        myEntryAdapter.setListener(this);
        myEntryFormPresenter.getMyEntryForm(pageNum,pageSize);
        workDetailPresenter = new WorkDetailPresenter(this,this);
    }


    private void initTitlebar() {
        titlebar = (Titlebar) findViewById(R.id.my_entry_title_bar);
        titlebar.setTitle(getResources().getString(R.string.my_entry_form));
        titlebar.setTitleBarClickListener(new Titlebar.TitleBarClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {

            }
        });
    }

    @Override
    public void getMyEntryListSuccess(List<Work> list) {
        ptr.refreshComplete();
        List<Work> canCancelList = deleteCannotCancelWork(list);
        if (pageNum == 1) {
            if (canCancelList!=null && canCancelList.size()>0) {
                workMsgs.clear();
                workMsgs.addAll(canCancelList);
                myEntryAdapter.updateData(workMsgs);
            }
        }else if (pageNum > 1) {
            if (canCancelList!=null && canCancelList.size()>0) {
                workMsgs.addAll(canCancelList);
                myEntryAdapter.updateData(workMsgs);
            }else{
                --pageNum;
            }
        }
    }

    @Override
    public void getMyEntryListFail(String desc) {
        ptr.refreshComplete();
        ToastUtils.toastShort(desc);
    }
    private List<Work> deleteCannotCancelWork(List<Work> list){
        List<Work> newList = new ArrayList<Work>();
        if (list!=null && list.size()>0) {
            for(Work work :list){
                /**
                 * 2：待支付
                 * 1：可以取消报名
                 * 0：不可以取消报名
                 */
                String cancelsign = work.getCanCancelSign();
                if ("1".equals(cancelsign) || "2".equals(cancelsign)) {
                    newList.add(work);
                }
            }
        }
        return newList;
    }

    @Override
    public void cancelEnrollSuccess(RspMsg rspMsg) {
        ToastUtils.toastShort(Tools.getStringByResouceId(R.string.cancel_sign_success));
        finish();
    }

    @Override
    public void cancelEnrollFail(RspMsg rspMsg) {
        if (!TextUtils.isEmpty(rspMsg.getRspDesc())) {
            ToastUtils.toastShort(rspMsg.getRspDesc());
        }else{
            ToastUtils.toastShort(Tools.getStringByResouceId(R.string.cancel_sign_fail));
        }
    }

    @Override
    public void showProgress() {
        if (currentHandlerWorkStatus==-1) {
            return;
        }
        if (currentHandlerWorkStatus == MASK_CANCEL_PAY) {
            showDialog(Tools.getStringByResouceId(R.string.cancel_sign_ing));
        } else if (currentHandlerWorkStatus == MASK_PREPARE_PAY) {
            showDialog(Tools.getStringByResouceId(R.string.request_sign_ing));
        }
    }

    @Override
    public void showProgress(String desc) {

    }

    @Override
    public void hideProgress() {
        hideDialog();
    }

    @Override
    public void getWXpayInfoSuccess(RspMsg rspMsg) {
        WXPay mWXPay = rspMsg.getRspDetail().getWxPay();
        Work work = rspMsg.getRspDetail().getWork();
        if (mWXPay!=null && work !=null) {
            mWXPay.setUnitPrice(work.getUnitPrice());
            mWXPay.setNum(work.getNum());
            mWXPay.setWorkId(work.getId());
            Intent intent = new Intent(MyEntryFormActivity.this, PayMainActivity.class);
            intent.putExtra(Constant.KEY_WXPAY_ENTITY, mWXPay);
            startActivity(intent);
            finish();
        }else{
            LogUtils.e(TAG,"getWXpayInfoSuccess WXPay is null or Work is null");
        }
    }

    @Override
    public void getWXpayInfoFail(RspMsg rspMsg) {
        if (rspMsg!=null && !TextUtils.isEmpty(rspMsg.getRspDesc())) {
            LogUtils.i(TAG,"rspMsg.getWXpayInfoFail() ... "+rspMsg.getRspDesc());
            ToastUtils.toastShort(rspMsg.getRspDesc());
        }
    }

    @Override
    public void getSignSuccess(Work workMsg, RspMsg rspMsg) {
        if (rspMsg!=null) {
            WXPay mWXPay = rspMsg.getRspDetail().getWxPay();
            if (clickWork!=null) {
                mWXPay.setUnitPrice(clickWork.getUnitPrice());
                mWXPay.setNum(clickWork.getNum());
                mWXPay.setWorkId(clickWork.getId());
            }
            Intent intent = new Intent(MyEntryFormActivity.this, PayMainActivity.class);
            intent.putExtra(Constant.KEY_WXPAY_ENTITY,mWXPay);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void getSignFail(Work workMsg, RspMsg rspMsg) {
        if (rspMsg!=null && !TextUtils.isEmpty(rspMsg.getRspDesc())) {
            LogUtils.i(TAG,"rspMsg.getRspDesc() ... "+rspMsg.getRspDesc());
            ToastUtils.toastShort(rspMsg.getRspDesc());
        }
    }

    @Override
    public void getDetailSuccess(List<Work> works) {

    }

    @Override
    public void getDetailFail(String desc) {

    }

    @Override
    public void OnItemClick(View v, int position) {
        clickWork = workMsgs.get(position);
        if (clickWork==null) {
            return;
        }
        String sign = clickWork.getCanCancelSign();
        if ("2".equals(sign)) {
            currentHandlerWorkStatus = MASK_PREPARE_PAY;
            DialogBuilder builder = new DialogBuilder();
            builder.createCustomDialogCannotEdit(MyEntryFormActivity.this, clickWork.getUnitPrice(),clickWork
                    .getNum(), new DialogBuilder.DialogSingleButtonClickListener() {
                @Override
                public void onClick(int choiceNum,float totalPrice) {
                    //点击待支付
                    if (clickWork!=null) {
                        String id = clickWork.getId();
                        myEntryFormPresenter.getWXpayInfo(id);
                        builder.dismissDialog();;
                    }
                }
            });
        }else if ("1".equals(sign)) {
            currentHandlerWorkStatus = MASK_CANCEL_PAY;
            //取消报名
            //        ToastUtils.toastShort("取消报名");
            final DialogBuilder db = new DialogBuilder();
            db.createNormalDialog(MyEntryFormActivity.this, "",
                    getString(R.string.is_cancel_sign),
                    new DialogBuilder.DialogButtonClickListener() {
                        @Override
                        public void onClickOk() {
                            if (myEntryFormPresenter!=null) {
                                LogUtils.i(TAG,"workID ... "+clickWork.getId());
                                myEntryFormPresenter.cancelEroll(clickWork.getId());
                                db.getCurrentDialog().dismiss();
                            }
                        }
                        @Override
                        public void onClickCancel() {
                            db.getCurrentDialog().dismiss();
                        }
                    });
        }
    }
}