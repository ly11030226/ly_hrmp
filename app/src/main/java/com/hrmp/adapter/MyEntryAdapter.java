package com.hrmp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.Work;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的报名适配器
 */

public class MyEntryAdapter extends RecyclerView.Adapter<MyEntryAdapter.MsgViewHolder> {
    private static final String TAG = "MyEntryAdapter";
    private Context context;
    private List<Work> data;
    private CusOnItemOnClickListener listener;

    public MyEntryAdapter(Context mContext, List<Work> mData) {
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
        MsgViewHolder holder = new MsgViewHolder(LayoutInflater.from(
                context).inflate(R.layout.adapter_my_entry_form, parent,
                false),listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position) {
        Work work= data.get(position);
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

    class MsgViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        TextView tvDesc;
        Button btnCancel;
        CusOnItemOnClickListener mlistener;
        public MsgViewHolder(View view,CusOnItemOnClickListener listener) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tv_my_entry_time);
            tvDesc = (TextView) view.findViewById(R.id.tv_my_entry_desc);
            btnCancel = (Button) view.findViewById(R.id.btn_my_entry);
            this.mlistener = listener;
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener!=null) {
                        mlistener.OnItemClick(v,getAdapterPosition());
                    }
                }
            });
        }
        private  void bindData(Work work){
            String desc = work.getWorkDescri();
            String date = work.getSignTime();
            String sign = work.getCanCancelSign();
            tvDesc.setText(desc);
            tvDate.setText(date);
            if ("2".equals(sign)) {
                btnCancel.setText(Tools.getStringByResouceId(R.string.wait_for_do_repay));
            }else if ("1".equals(sign)) {
                btnCancel.setText(Tools.getStringByResouceId(R.string.do_not_enroll));
            }
        }
    }
    public interface CusOnItemOnClickListener {
        public void OnItemClick(View v, int position);
    }
}
