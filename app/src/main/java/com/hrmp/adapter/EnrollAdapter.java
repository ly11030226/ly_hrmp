package com.hrmp.adapter;

import android.content.Context;

import com.hrmp.R;
import com.hrmp.bean.Work;

import java.util.List;

/**
 * Created by Ly on 2017/5/10.
 */

public class EnrollAdapter extends CommonAdapter<Work> {

    public EnrollAdapter(Context context, List list, int layoutId) {
        super(context, list, layoutId);
    }
    public void updateData(List<Work> newListData){
        super.updateListData(newListData);
    }

    @Override
    public void setViewContent(CommonViewHolder viewHolder, Work workMsg) {
        viewHolder.setText(R.id.tv_workkind,workMsg.getWorkKind())
                .setText(R.id.tv_worknum,workMsg.getHireNum()+"人");
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
