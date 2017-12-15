package com.hrmp.adapter;

import android.content.Context;

import com.hrmp.R;
import com.hrmp.bean.HandleHistoryBean;

import java.util.List;

/**
 * Created by Ly on 2017/5/14.
 */

public class HandleHistoryAdapter extends CommonAdapter<HandleHistoryBean>{

    public HandleHistoryAdapter(Context context, List<HandleHistoryBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void setViewContent(CommonViewHolder viewHolder, HandleHistoryBean handleHistory) {
        viewHolder.setText(R.id.tv_handle_history_time,handleHistory.getHandleDate())
                .setText(R.id.tv_handle_history_name,handleHistory.getHandleName())
                .setText(R.id.tv_handle_history_operate,handleHistory.getOperate())
                .setText(R.id.tv_handle_history_content,handleHistory.getContent());
    }
}
