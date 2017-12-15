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
import com.hrmp.adapter.WorkDetailAdapter;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.WXPay;
import com.hrmp.bean.Work;
import com.hrmp.model.WorkMsgBiz;
import com.hrmp.presenter.WorkDetailPresenter;
import com.hrmp.ui.iview.IWorkDetailView;
import com.hrmp.util.DialogBuilder;
import com.hrmp.util.LogUtils;
import com.hrmp.util.ToastUtils;
import com.hrmp.view.Titlebar;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 招工详情
 */
public class WorkDetailActivity extends MyBaseActivity implements IWorkDetailView,
        WorkDetailAdapter.CusOnItemOnClickListener{
    private static final String TAG = "WorkDetailActivity";
    private Titlebar titlebar;
    private WorkDetailAdapter workDetail2Adapter;
    private List<Work> works = new ArrayList<Work>();
    private Work work;
    private WorkDetailPresenter workDetailPresenter;
    private WorkMsgBiz workMsgBiz;
    private PtrClassicFrameLayout ptr;
    private RecyclerView rcl;
    private WXPay currentPay;
    private Work clickWork;  //当前点击支付的work
    private int pageNo = 1;
    private int pageSize = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_work_detail);
        try {
            initTitleBar();
            initIntentData();
            initListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initIntentData() {
        work = (Work) getIntent().getSerializableExtra(EnrollFragment.KEY_ENROLL_WROK_MESSAGE);
        workMsgBiz = (WorkMsgBiz) getIntent().getSerializableExtra(EnrollFragment.KEY_ENROLL_WORKMSGBIZ);
        if (work == null) {
            LogUtils.e("","****** work is null ******");
            return;
        }else if (workMsgBiz == null) {
            LogUtils.e("","****** workMsgBiz is null ******");
            return;
        }else{
            workDetailPresenter = new WorkDetailPresenter(WorkDetailActivity.this,this);
            workDetailPresenter.requestData(this.pageNo,this.pageSize,work.getWorkKind(),
                    workMsgBiz);
        }
    }

    private void initListView() {
        ptr = (PtrClassicFrameLayout) findViewById(R.id.ptr_work_detail);
        rcl = (RecyclerView) findViewById(R.id.rcl_work_detail);
        rcl.setLayoutManager(new LinearLayoutManager(WorkDetailActivity.this));
        workDetail2Adapter = new WorkDetailAdapter(WorkDetailActivity.this,works);
        workDetail2Adapter.setListener(this);
        rcl.setAdapter(workDetail2Adapter);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG, "onRefreshBegin");
                workDetailPresenter.requestData(pageNo,pageSize,work.getWorkKind(),workMsgBiz);
            }
        });
    }

    private void initTitleBar() {
        titlebar = (Titlebar) findViewById(R.id.work_detail_title_bar);
        titlebar.setTitle(getString(R.string.enroll_detail));
        titlebar.showLeftButton();
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
    public void showProgress(String desc) {
        showDialog(desc);
    }

    @Override
    public void hideProgress() {
        hideDialog();
    }

    @Override
    public void getSignSuccess(Work workMsg,RspMsg rspMsg) {
        if (rspMsg!=null) {
            WXPay mWXPay = rspMsg.getRspDetail().getWxPay();
            if (clickWork!=null) {
                mWXPay.setUnitPrice(clickWork.getUnitPrice());
                mWXPay.setNum(clickWork.getNum());
                mWXPay.setWorkId(clickWork.getId());
            }
            Intent intent = new Intent(WorkDetailActivity.this, PayMainActivity.class);
            intent.putExtra(Constant.KEY_WXPAY_ENTITY,mWXPay);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void getSignFail(Work workMsg,RspMsg rspMsg) {
        if (rspMsg!=null && !TextUtils.isEmpty(rspMsg.getRspDesc())) {
            LogUtils.i(TAG,"rspMsg.getRspDesc() ... "+rspMsg.getRspDesc());
            ToastUtils.toastShort(rspMsg.getRspDesc());
        }
    }

    @Override
    public void getDetailSuccess(List<Work> work) {
        ptr.refreshComplete();
        if (work!=null && work.size()>0) {
            works.clear();
            works.addAll(work);
            workDetail2Adapter.updateData(works);
        }
    }

    @Override
    public void getDetailFail(String desc) {
        ptr.refreshComplete();
        if (!TextUtils.isEmpty(desc)) {
            ToastUtils.toastLong(desc);
        }

    }

    @Override
    public void OnItemClick(View v, int position) {
        LogUtils.i(TAG,"OnItemClick");
        clickWork = works.get(position);
        DialogBuilder builder = new DialogBuilder();
        builder.createCustomDialog(WorkDetailActivity.this, clickWork.getUnitPrice(),clickWork
                .getHireNum(), new DialogBuilder.DialogSingleButtonClickListener() {
            @Override
            public void onClick(int choiceNum,float totalPrice) {
                //点击支付
                if (clickWork!=null) {
                    String id = clickWork.getId();
                    clickWork.setNum(choiceNum);
                    String unitPrice = clickWork.getUnitPrice()+"";
                    //测试设置金额为0.001
                    clickWork.setUnitPrice(Float.valueOf(unitPrice));
                    LogUtils.i(TAG,"id ... "+id+"  choiceNum ... "+choiceNum+"  unitPrice ... "+unitPrice);
                    workDetailPresenter.getSign(clickWork);
                }
            }
        });
    }
}
