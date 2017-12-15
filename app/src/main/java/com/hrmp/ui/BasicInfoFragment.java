package com.hrmp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hrmp.R;
import com.hrmp.bean.Work;

/**
 * Created by Ly on 2017/5/10.
 */

public class BasicInfoFragment extends Fragment implements EnrollDetailActivity.WorkMsgContentListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_info,container,false);

        return view;
    }

    @Override
    public void fillLayout(Work workMsg) {

    }
}
