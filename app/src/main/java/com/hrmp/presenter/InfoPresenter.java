package com.hrmp.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hrmp.AppManager;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.ui.ModifyPsdActivity;
import com.hrmp.ui.MyEntryFormActivity;
import com.hrmp.ui.iview.IInfoView;
import com.hrmp.util.DialogBuilder;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Ly on 2017/5/14.
 */

public class InfoPresenter {
    private Context context;
    private IInfoView iInfoView;
    public InfoPresenter(Context mContext, IInfoView mIInfoView) {
        this.context = mContext;
        this.iInfoView = mIInfoView;
    }
    public void modifyPassword(){
        Intent intent = new Intent(context, ModifyPsdActivity.class);
        context.startActivity(intent);
    }
    public void showMyEntryForm(){
        Intent intent = new Intent(context, MyEntryFormActivity.class);
        context.startActivity(intent);
    }
    public void changeUsername(){
        final DialogBuilder db = new DialogBuilder();
        db.createNormalDialog((Activity)context, "", context.getString(R.string.dialog_is_change_username), new DialogBuilder.DialogButtonClickListener() {
            @Override
            public void onClickOk() {
                Tools.logout(context);
            }
            @Override
            public void onClickCancel() {
                db.getCurrentDialog().dismiss();
            }
        });

    }
    public void exit(){
        final DialogBuilder db = new DialogBuilder();
        db.createNormalDialog((Activity)context, "", context.getString(R.string.dialog_is_exit), new DialogBuilder.DialogButtonClickListener() {
            @Override
            public void onClickOk() {
                MobclickAgent.onProfileSignOff();
                AppManager.getInstance().finishAllActivity();
                System.exit(0);
            }

            @Override
            public void onClickCancel() {
                db.getCurrentDialog().dismiss();
            }
        });

    }
}
