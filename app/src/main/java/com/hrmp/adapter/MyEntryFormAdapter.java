package com.hrmp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.hrmp.R;
import com.hrmp.bean.Work;
import com.hrmp.util.DialogBuilder;
import com.hrmp.util.LogUtils;

import java.util.List;

/**
 * Created by Ly on 2017/6/2.
 */

public class MyEntryFormAdapter extends CommonAdapter<Work> {
    private Context context;
    public MyEntryFormAdapter(Context context, List list, int layoutId) {
        super(context, list, layoutId);
        this.context = context;
    }

    @Override
    public void updateListData(List<Work> newList) {
        super.updateListData(newList);
    }

    @Override
    public void setViewContent(CommonViewHolder viewHolder, final Work work) {
        viewHolder.setText(R.id.tv_my_entry_time,work.getSignTime())
                .setText(R.id.tv_my_entry_desc,work.getWorkDescri())
                .setOnClickListener(R.id.btn_my_entry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder builder = new DialogBuilder();
                        builder.createNormalDialog((Activity) context, "", context.getResources().getString(R.string.my_entry_cancel), new DialogBuilder.DialogButtonClickListener() {
                            @Override
                            public void onClickOk() {
                                LogUtils.i("取消报名操作 work id ... "+work.getId());
                            }

                            @Override
                            public void onClickCancel() {

                            }
                        });
                    }
                });

    }
}
