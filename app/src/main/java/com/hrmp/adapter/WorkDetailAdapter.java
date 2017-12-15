package com.hrmp.adapter;

/**
 * Created by Ly on 2017/9/6.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hrmp.R;
import com.hrmp.bean.Work;

import java.util.ArrayList;
import java.util.List;

/**
 * 招工详情适配器
 */
public class WorkDetailAdapter extends RecyclerView.Adapter<WorkDetailAdapter.MsgViewHolder> {
    private static final String TAG = "WorkDetailAdapter";
    private Context context;
    private List<Work> data;
    private CusOnItemOnClickListener listener;

    public WorkDetailAdapter(Context mContext, List<Work> mData) {
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
                context).inflate(R.layout.adapter_work_detail_sun, parent,
                false),listener);
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

        TextView tvWorkDesc;
        Button btnWork;
        CusOnItemOnClickListener mlistener;
        public MsgViewHolder(View view,CusOnItemOnClickListener listener) {
            super(view);
            tvWorkDesc = (TextView) view.findViewById(R.id.tv_worklist_desc);
            btnWork = (Button) view.findViewById(R.id.btn_worklist);
            this.mlistener = listener;
            btnWork.setOnClickListener(this);
        }
        private  void bindData(Work workMsg){
            String desc = workMsg.getWorkDescri();
            tvWorkDesc.setText(desc);
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
