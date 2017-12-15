package com.hrmp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Window;

import com.hrmp.Constant;
import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.presenter.WelcomePresenter;
import com.hrmp.ui.iview.IWelcomeView;
import com.hrmp.util.LogUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class WelcomeActivity extends MyBaseActivity implements IWelcomeView {
    private static final String TAG = "WelcomeActivity";
    private WelcomePresenter welcomePresenter;
    private static final int LOGIN_WHAT = 0X123;
    private static final int NO_LOGIN_WHAT = 0X122;
    private Handler delayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case NO_LOGIN_WHAT:
                        toJumpLoginActivity();
                        break;
                    case LOGIN_WHAT:
                        toJumpMainActivity();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        try {
            LogUtils.i(TAG,"WelcomeActivity onCreate");
            WelcomeActivityPermissionsDispatcher.checkPermissionsWithCheck(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据状态跳转
     */
    private void doJump() {
        welcomePresenter = new WelcomePresenter(this);
        if(checkIsFirstLogin()){
            delayHandler.sendEmptyMessageDelayed(NO_LOGIN_WHAT,2000);
        }else{
            delayHandler.sendEmptyMessageDelayed(LOGIN_WHAT,2000);
        }
    }

    /***************************验证权限start********************************/
    private AlertDialog mAlertDialog;
    private AlertDialog notAskDialog;
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void checkPermissions(){
        doJump();
    }
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForPermissons(final PermissionRequest request) {
        if (mAlertDialog==null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage(Tools.getStringByResouceId(R.string.permission_reminder))
                    .setPositiveButton(R.string.permission_open, (dialog, button) -> request.proceed())
                    .setNegativeButton(R.string.permission_cancel, (dialog, button) -> request.cancel())
                    .create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
        }
        mAlertDialog.show();
    }
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForPermissions() {
        LogUtils.i(TAG,"Permissions Denied");
        doJump();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForPermissions() {
        LogUtils.i(TAG,"Permissions NeverAskFor");
        if (notAskDialog==null) {
            notAskDialog = new AlertDialog.Builder(this)
                    .setMessage(Tools.getStringByResouceId(R.string.permission_reminder))
                    .setPositiveButton(R.string.permission_do_open, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Tools.jumpSystemSet(WelcomeActivity.this);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.permission_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notAskDialog.dismiss();
                            doJump();
                        }
                    })
                    .create();
            notAskDialog.setCancelable(false);
            notAskDialog.setCanceledOnTouchOutside(false);
        }
        notAskDialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /***************************验证权限end********************************/

    @Override
    public boolean checkIsFirstLogin() {
        return welcomePresenter.checkIsOrNotFirstLogin();
    }

    @Override
    public void toJumpMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        intent.putExtra(Constant.KEY_LOGIN_STYLE,Constant.BACKGROUND_LOGIN);
        startActivity(intent);
        finish();
    }

    @Override
    public void toJumpLoginActivity() {
        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
