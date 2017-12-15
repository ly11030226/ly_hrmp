package com.hrmp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.presenter.ModifyPsdPresenter;
import com.hrmp.ui.iview.IModifyPsdView;
import com.hrmp.util.ToastUtils;
import com.hrmp.view.Titlebar;

public class ModifyPsdActivity extends MyBaseActivity implements IModifyPsdView{

    private TextView tvReminder;
    private Titlebar titlebar;
    private EditText etOld,etNew,etNew2;
    private Button btnCommit;
    private ModifyPsdPresenter modifyPsdPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_psd);
        try {
            initTitlebar();
            initView();
            modifyPsdPresenter = new ModifyPsdPresenter(ModifyPsdActivity.this,this);
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String old = etOld.getText().toString();
                    String n1 = etNew.getText().toString();
                    String n2 = etNew2.getText().toString();
                    if (TextUtils.isEmpty(old)) {
                        ToastUtils.toastLong(R.string.remind_old_is_empty);
                        return;
                    }else if (TextUtils.isEmpty(n1)) {
                        ToastUtils.toastLong(R.string.remind_new_is_empty);
                        return;
                    }else if (TextUtils.isEmpty(n2)) {
                        ToastUtils.toastLong(R.string.remind_new_2_is_empty);
                        return;
                    }else if (!n1.equals(n2)) {
                        ToastUtils.toastLong(R.string.remind_new_is_equals_new2);
                        return;
                    }else if (old.equals(n1)) {
                        ToastUtils.toastLong(R.string.remind_new_is_equals_old);
                        return;
                    }
                    modifyPsdPresenter.sendRequest(old,n1,n2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        tvReminder = (TextView) findViewById(R.id.tv_modify_psd_reminder);
        etOld = (EditText) findViewById(R.id.et_oldpassword);
        etNew = (EditText) findViewById(R.id.et_newpassword);
        etNew2 = (EditText) findViewById(R.id.et_comfir_password);
        btnCommit = (Button) findViewById(R.id.btn_enroll);
    }

    private void initTitlebar() {
        titlebar = (Titlebar) findViewById(R.id.modify_psd_title_bar);
        titlebar.setTitle(getResources().getString(R.string.modify_password));
        titlebar.setTitleBarClickListener(new Titlebar.TitleBarClickListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {

            }
        });
    }

    @Override
    public void showProgressBar() {
        showDialog(Tools.getStringByResouceId(R.string.resetting_pwd));
    }

    @Override
    public void hideProgressBar() {
        hideDialog();
    }

    @Override
    public void showReminder(String reminder) {
        tvReminder.setVisibility(View.VISIBLE);
        tvReminder.setText(reminder);
    }

}
