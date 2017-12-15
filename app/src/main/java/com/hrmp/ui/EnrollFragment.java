package com.hrmp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hrmp.R;
import com.hrmp.adapter.WorkMsgAdapter;
import com.hrmp.bean.Work;
import com.hrmp.presenter.EnrollPresenter;
import com.hrmp.ui.iview.IEnrollView;
import com.hrmp.util.LogUtils;
import com.hrmp.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 报名TAB
 */
public class EnrollFragment extends Fragment implements IEnrollView,WorkMsgAdapter.CusOnItemOnClickListener{
    public static final String TAG = "EnrollFragment";
    private List<Work> workMsgs = new ArrayList<Work>();
    private EnrollPresenter enrollPresenter;
    public static final String KEY_ENROLL_WROK_MESSAGE = "KEY_ENROLL_WROK_MESSAGE";
    public static final String KEY_ENROLL_WORKMSGBIZ = "KEY_ENROLL_WORKMSGBIZ";
    private PtrClassicFrameLayout ptr;
    private RecyclerView rcl;
    private WorkMsgAdapter workMsgAdapter;
    private int pageNum = 1;
    private int pageSize = 20;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enroll,container,false);
        initView(view);
        initData();
        initPtr();
        return view;
    }
    private void initPtr() {
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG,"onLoadMoreBegin");
                enrollPresenter.getWorkMsg(++pageNum,pageSize);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG,"onRefreshBegin");
                pageNum = 1;
                enrollPresenter.getWorkMsg(pageNum,pageSize);
            }
        });
    }


    private void initData() {
        workMsgAdapter = new WorkMsgAdapter(getActivity(),workMsgs);
        rcl.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcl.setAdapter(workMsgAdapter);
        workMsgAdapter.setListener(this);
        enrollPresenter = new EnrollPresenter(getActivity(),this);
        enrollPresenter.getWorkMsg(pageNum,pageSize);
    }

    private void initView(View view) {
        ptr = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_enroll);
        rcl = (RecyclerView) view.findViewById(R.id.rcl_enroll);
    }

    @Override
    public void getWorkMsgSuccess(List<Work> work) {
        ptr.refreshComplete();
        if (pageNum == 1) {
            if (work!=null && work.size()>0) {
                workMsgs.clear();
                workMsgs.addAll(work);
                workMsgAdapter.updateData(workMsgs);
            }
        }else if (pageNum > 1) {
            if (work!=null && work.size()>0) {
                workMsgs.addAll(work);
                workMsgAdapter.updateData(workMsgs);
            }else{
                --pageNum;
            }
        }
    }

    @Override
    public void getWorkMsgFail(String desc) {
        ptr.refreshComplete();
        if (!TextUtils.isEmpty(desc)) {
            ToastUtils.toastLong(desc);
        }
    }

    @Override
    public void OnItemClick(View v, int position) {
        Work workMsg = workMsgs.get(position);
        Intent intent = new Intent(getActivity(),WorkDetailActivity.class);
        intent.putExtra(KEY_ENROLL_WROK_MESSAGE,workMsg);
        intent.putExtra(KEY_ENROLL_WORKMSGBIZ,enrollPresenter.getWorkMsgBiz());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNum = 1;
        enrollPresenter.getWorkMsg(pageNum,pageSize);
    }
}
