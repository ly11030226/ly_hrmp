package com.hrmp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hrmp.Constant;
import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.User;
import com.hrmp.presenter.LoginPresenter;
import com.hrmp.ui.iview.ILoginView;
import com.hrmp.util.DialogBuilder;
import com.hrmp.util.ToastUtils;


public class LoginActivity extends MyBaseActivity implements View.OnClickListener,ILoginView{
    private static final String TAG = "LoginActivity";
    private Button ivClearUserName,ivClearPsd;
    private EditText etUserName,etPsd;
    private Button btnLogin;
    private ProgressBar pbLogin;
    private TextView tvReminder;
    LoginPresenter loginPresenter;
    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        try {
            initView();
            addListener();
            loginPresenter = new LoginPresenter(this);
            loginPresenter.checkVersion();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addListener() {
        ivClearUserName.setOnClickListener(this);
        ivClearPsd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        etUserName.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @SuppressLint("NewApi")
            @Override
            public void afterTextChanged(android.text.Editable s){
                if (s.length() > 0) {
                    ivClearUserName.setVisibility(View.VISIBLE);
                    if (etPsd.getText().length()>0){
                        btnLogin.setBackground(null);
                        ColorDrawable mColorDrawable = new ColorDrawable(getResources().getColor(R.color.blue_main));
                        btnLogin.setBackgroundDrawable(mColorDrawable);
                        btnLogin.setTextColor(Color.WHITE);
                    }else{
                        btnLogin.setBackgroundResource(R.mipmap.anniu);
//                        btnLogin.setTextColor(Color.parseColor("#A9A9A9"));
                    }
                } else {
                    ivClearUserName.setVisibility(View.INVISIBLE);
                    btnLogin.setBackgroundResource(R.mipmap.anniu);
//                    btnLogin.setTextColor(Color.parseColor("#A9A9A9"));
                }
            }
        });

        etPsd.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            }

            @SuppressLint("NewApi")
            @Override
            public void afterTextChanged(android.text.Editable s){
                if (s.length() > 0) {
                    ivClearPsd.setVisibility(View.VISIBLE);
                    if (etUserName.getText().length()>0){
                        btnLogin.setBackground(null);
                        ColorDrawable mColorDrawable = new ColorDrawable(getResources().getColor(R.color.blue_main));
                        btnLogin.setBackgroundDrawable(mColorDrawable);
//                        btnLogin.setBackgroundColor(getResources().getColor(R.color.blue_main,null));
                        btnLogin.setTextColor(Color.WHITE);
                    }else{
                        btnLogin.setBackgroundResource(R.mipmap.anniu);
                        btnLogin.setTextColor(getResources().getColor(R.color.edittext_gray_color));
                    }
                } else {
                    ivClearPsd.setVisibility(View.INVISIBLE);
                    btnLogin.setBackgroundResource(R.mipmap.anniu);
                    btnLogin.setTextColor(getResources().getColor(R.color.edittext_gray_color));
                }
            }
        });
    }

    private void initView() {
        ivClearUserName = (Button) findViewById(R.id.iv_clear_username);
        ivClearPsd = (Button) findViewById(R.id.iv_clear_psd);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPsd = (EditText) findViewById(R.id.et_psd);
        pbLogin = (ProgressBar) findViewById(R.id.pb_login);
        tvReminder = (TextView) findViewById(R.id.tv_show_reminder);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_clear_username:
                clearUserName();
                break;
            case R.id.iv_clear_psd:
                clearPsd();
                break;
            case R.id.btn_login:
                String username = etUserName.getText().toString();
                String psd = etPsd.getText().toString();
                login(username,psd);
                break;
        }
    }

    @Override
    public void login(String username, String psd) {
        loginPresenter.login(username,psd);
    }

    @Override
    public void clearUserName() {
        etUserName.setText("");
    }

    @Override
    public void clearPsd() {
        etPsd.setText("");
    }

    @Override
    public void showProgressBar(String desc) {
        showDialog(desc);
    }

    @Override
    public void hideProgressBar() {
        hideDialog();
    }

    @Override
    public void usernameOrPsdIsEmpty() {
        tvReminder.setVisibility(View.VISIBLE);
        tvReminder.setText(getResources().getText(R.string.username_or_psd_is_empty));
    }

    @Override
    public void loginSuccess(User user) {
        Tools.saveUserToFile(user);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra(LOGIN_USER_KEY,user);
        intent.putExtra(Constant.KEY_LOGIN_STYLE,Constant.FOREGROUND_LOGIN);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFail(String content) {
        tvReminder.setText(content);
    }

    @Override
    public void checkVersionSuccess(RspMsg rspMsg) {
        DialogBuilder dialogBuilder = new DialogBuilder();
        dialogBuilder.createUpdateVersionDialog(
                LoginActivity.this,
                false,
                "HRMP "+rspMsg.getRspDetail().getAndroidVersion().getVersion(),
                rspMsg.getRspDetail().getAndroidVersion().getVersionDesc(),
                rspMsg.getRspDetail().getAndroidVersion().getVersionPath()
        );

    }

    @Override
    public void checkVersionFail(RspMsg rspMsg) {
        ToastUtils.toastShort(rspMsg.getRspDesc());
    }
}
