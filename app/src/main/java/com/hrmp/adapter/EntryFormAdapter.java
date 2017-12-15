package com.hrmp.adapter;

import android.content.Context;

import com.hrmp.R;
import com.hrmp.bean.EntryFormBean;

import java.util.List;

/**
 * Created by Ly on 2017/5/12.
 */

public class EntryFormAdapter extends CommonAdapter<EntryFormBean> {

    public EntryFormAdapter(Context context, List list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    public void setViewContent(CommonViewHolder viewHolder, EntryFormBean bean) {
        viewHolder.setText(R.id.tv_entry_time,bean.getDate())
                .setText(R.id.tv_entry_name,bean.getName())
                .setText(R.id.tv_entry_num,bean.getNum());
    }
}
