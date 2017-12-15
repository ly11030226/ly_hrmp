package com.hrmp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrmp.R;
import com.hrmp.bean.Work;

import java.util.ArrayList;
import java.util.List;

/**
 * 报名信息适配器
 */
public class WorkMsgAdapter extends RecyclerView.Adapter<WorkMsgAdapter.MsgViewHolder> {
    private static final String TAG = "WorkMsgAdapter";
    private Context context;
    private List<Work> data;
    private CusOnItemOnClickListener listener;

    public WorkMsgAdapter(Context mContext, List<Work> mData) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        this.context = mContext;
        this.data = mData;
    }

    public void updateData(List<Work> newList){
        this.data = newList;
        notifyDataSetChanged();
    }

    public CusOnItemOnClickListener getListener() {
        return listener;
    }

    public void setListener(CusOnItemOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MsgViewHolder holder = new MsgViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_work_list_item, parent,false),listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position) {
        Work work = data.get(position);
        holder.bindData(work);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvWorkKind;
        TextView tvWorkNum;
        CusOnItemOnClickListener mlistener;
        public MsgViewHolder(View view,CusOnItemOnClickListener listener) {
            super(view);
            tvWorkKind = (TextView) view.findViewById(R.id.tv_workkind);
            tvWorkNum = (TextView) view.findViewById(R.id.tv_worknum);
            this.mlistener = listener;
            view.setOnClickListener(this);
        }
        private  void bindData(Work work){
            String kind = work.getWorkKind();
            String num = work.getHireNum()+"人";
            tvWorkKind.setText(kind);
            tvWorkNum.setText(num);
        }

        @Override
        public void onClick(View v) {
            if (listener!=null) {
                listener.OnItemClick(v,getAdapterPosition());
            }
        }
    }
    public interface CusOnItemOnClickListener {
        public void OnItemClick(View v,int position);
    }
}
