package com.hrmp.adapter;

import android.content.Context;

import com.hrmp.R;
import com.hrmp.bean.Msg;

import java.util.List;

/**
 * Created by Ly on 2017/5/12.
 */

public class MessgeAdapter extends CommonAdapter<Msg> {
    public MessgeAdapter(Context context, List list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void setViewContent(CommonViewHolder viewHolder, Msg msg) {
        viewHolder.setText(R.id.tv_enroll_title,msg.getMessageTitle())
                .setText(R.id.tv_enroll_businessnum,msg.getSendUserName())
                .setText(R.id.tv_enroll_workarea,"")
                .setText(R.id.tv_enroll_hirenum,"")
                .setText(R.id.tv_enroll_date,
//                        DateUtil.date2Str(msg.getCreateTime(),"yyyy-MM-dd")
                        msg.getCreateTime()
                );
    }

    @Override
    public void updateListData(List<Msg> newList) {
        super.updateListData(newList);
    }
}
