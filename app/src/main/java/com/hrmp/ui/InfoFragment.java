package com.hrmp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrmp.HRMPApplication;
import com.hrmp.R;
import com.hrmp.presenter.InfoPresenter;
import com.hrmp.ui.iview.IInfoView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Ly on 2017/5/9.
 */

public class InfoFragment extends Fragment implements View.OnClickListener,IInfoView{

    public static final String TAG = "InfoFragment";
    private TextView tvNickname,tvUsername;
    private RelativeLayout rlModifyPsd,rlMyEntry,rlChangeUsername,rlExit;
    private InfoPresenter infoPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info,container,false);
        initView(view);
        addListener();
        infoPresenter = new InfoPresenter(getActivity(),this);
        if (HRMPApplication.getInstance().currentUser!=null) {
            tvNickname.setText(HRMPApplication.getInstance().currentUser.getUserName());
            tvUsername.setText(HRMPApplication.getInstance().currentUser.getLoginName());
        }
        return view;
    }

    private void addListener() {
        rlModifyPsd.setOnClickListener(this);
        rlMyEntry.setOnClickListener(this);
        rlChangeUsername.setOnClickListener(this);
        rlExit.setOnClickListener(this);
    }

    private void initView(View view) {
        tvNickname = (TextView) view.findViewById(R.id.tv_info_nickname);
        tvUsername = (TextView) view.findViewById(R.id.tv_username);
        rlModifyPsd = (RelativeLayout) view.findViewById(R.id.rl_modyfy_password);
        rlMyEntry = (RelativeLayout) view.findViewById(R.id.rl_my_entry);
        rlChangeUsername = (RelativeLayout) view.findViewById(R.id.rl_change_username);
        rlExit = (RelativeLayout) view.findViewById(R.id.rl_exit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_modyfy_password:    //修改密码
                infoPresenter.modifyPassword();
                break;
            case R.id.rl_my_entry:           //我的报名
                infoPresenter.showMyEntryForm();
                break;
            case R.id.rl_change_username:    //切换账号
                infoPresenter.changeUsername();
                break;
            case R.id.rl_exit:               //退出登录
                infoPresenter.exit();
                break;
            default:
                break;
        }
    }

    @Override
    public void modifyPsd() {

    }

    @Override
    public void showMyEntryForm() {

    }

    @Override
    public void changeUsername() {

    }

    @Override
    public void exit() {

    }
}
