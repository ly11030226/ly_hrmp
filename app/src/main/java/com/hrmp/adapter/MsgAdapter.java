package com.hrmp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrmp.R;
import com.hrmp.bean.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表适配器
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder> {
    private static final String TAG = "MsgAdapter";
    private Context context;
    private List<Msg> data;
    private CusOnItemOnClickListener listener;

    public MsgAdapter(Context mContext, List<Msg> mData) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        this.context = mContext;
        this.data = mData;
    }

    public void updateData(List<Msg> newList){
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
                context).inflate(R.layout.adapter_enroll_item, parent,
                false),listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MsgViewHolder holder, int position) {
        Msg msg = data.get(position);
        holder.bindData(msg);
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
        TextView tvTitle;
        TextView tvBusinessNum;
        TextView tvWorkArea;
        TextView tvHireNum;
        TextView tvDate;
        CusOnItemOnClickListener mlistener;
        public MsgViewHolder(View view,CusOnItemOnClickListener listener) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_enroll_title);
            tvBusinessNum = (TextView) view.findViewById(R.id.tv_enroll_businessnum);
            tvWorkArea = (TextView) view.findViewById(R.id.tv_enroll_workarea);
            tvHireNum = (TextView) view.findViewById(R.id.tv_enroll_hirenum);
            tvDate = (TextView) view.findViewById(R.id.tv_enroll_date);
            this.mlistener = listener;
            view.setOnClickListener(this);
        }
        private  void bindData(Msg msg){
            String title = msg.getMessageTitle();
            String businessNum = msg.getSendUserName();
            String date = msg.getCreateTime();
            String isRead = msg.getIsRead();
            //1：已读；0：未读
            if ("0".equals(isRead)) {
                tvTitle.setTextColor(ContextCompat.getColor(context,R.color.edittext_hint_color));
                tvDate.setTextColor(ContextCompat.getColor(context,R.color.edittext_hint_color));
                tvBusinessNum.setTextColor(ContextCompat.getColor(context,R.color.edittext_hint_color));
            }else if ("1".equals(isRead)) {
                tvTitle.setTextColor(ContextCompat.getColor(context,R.color.edittext_hint_color_read));
                tvDate.setTextColor(ContextCompat.getColor(context,R.color.edittext_hint_color_read));
                tvBusinessNum.setTextColor(ContextCompat.getColor(context,R.color.edittext_hint_color_read));
            }
            tvTitle.setText(title);
            tvBusinessNum.setText(businessNum);
            tvWorkArea.setText("");
            tvHireNum.setText("");
            tvDate.setText(date);
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
