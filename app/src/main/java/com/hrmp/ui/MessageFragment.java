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
import com.hrmp.adapter.MsgAdapter;
import com.hrmp.bean.Msg;
import com.hrmp.presenter.MessageListPresenter;
import com.hrmp.ui.iview.IMsgView;
import com.hrmp.util.LogUtils;
import com.hrmp.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 消息列表 TAB
 */

public class MessageFragment extends Fragment implements IMsgView,MsgAdapter
        .CusOnItemOnClickListener{

    public static final String TAG = "MessageFragment";
    public static final String KEY_MESSAGE_BEAN = "KEY_MESSAGE_BEAN";
    private List<Msg> messages = new ArrayList<Msg>();
    private MessageListPresenter mlp;
    private PtrClassicFrameLayout ptr;
    private RecyclerView rcl;
    private MsgAdapter msgAdapter;
    private int pageNum = 1;
    private int pageSize = 20;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        initPtr(view);
        addListener();
        return view;
    }

    private void addListener() {
        ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG,"onLoadMoreBegin");
                mlp.getMessageList(++pageNum,pageSize);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                LogUtils.i(TAG,"onRefreshBegin");
                pageNum = 1;
                mlp.getMessageList(pageNum,pageSize);
            }
        });
    }

    private void initPtr(View view) {
        mlp = new MessageListPresenter(getActivity(),this);
        ptr = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_msg);
        rcl = (RecyclerView) view.findViewById(R.id.rcl_msg);
        msgAdapter = new MsgAdapter(getActivity(),messages);
        rcl.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcl.setAdapter(msgAdapter);
        msgAdapter.setListener(this);
        mlp.getMessageList(pageNum,pageSize);
    }

    @Override
    public void OnItemClick(View v, int position) {
        Msg msg = messages.get(position);
        Intent intent = new Intent(getActivity(),MessageDetailActivity.class);
        intent.putExtra(KEY_MESSAGE_BEAN,msg);
        startActivity(intent);
    }

    @Override
    public void getMsgSuccess(List<Msg> msg) {
        ptr.refreshComplete();
        if (pageNum == 1) {
            if (msg!=null && msg.size()>0) {
                messages.clear();
                messages.addAll(msg);
                msgAdapter.updateData(messages);
            }
        }else if (pageNum > 1) {
            if (msg!=null && msg.size()>0) {
                messages.addAll(msg);
                msgAdapter.updateData(messages);
            }else{
                --pageNum;
            }
        }
    }

    @Override
    public void getMsgFail(String desc) {
        ptr.refreshComplete();
        if (!TextUtils.isEmpty(desc)) {
            ToastUtils.toastLong(desc);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG,"onResume");
        refresh();
    }
    public void refresh(){
        pageNum = 1;
        if (mlp!=null) {
            mlp.getMessageList(pageNum,pageSize);
        }
    }
}
