package com.hrmp.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.UpdateVersionAsyncTask;
import com.hrmp.net.HttpConstant;

import java.io.File;

/**
 * Created by Ly on 2017/5/14.
 */

public class DialogBuilder {
    private static final String TAG = "DialogBuilder";

    public Dialog currentDialog;

    public void setClickOKText() {


    }

    public void setClickCancelText() {

    }

    /**
     * 创建一个普通的dialog，包括一个确定按钮和一个取消按钮
     */
    public void createNormalDialog(Activity activity, String title, String content, final DialogButtonClickListener dialogButtonClickListener) {

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_normal, null);
        View ok = view.findViewById(R.id.btn_dialog_normal_ok);
        View cancel = view.findViewById(R.id.btn_dialog_normal_cancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_dialog_normal_tips);
        tvContent.setText(content);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_normal_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText(activity.getResources().getString(R.string.dialog_prompt));
        } else {
            tvTitle.setText(title);
        }
        Dialog dlg = new Dialog(activity, R.style.float_dialog);
        currentDialog = dlg;
        dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.dimAmount = 0.6f;

        dlg.setCanceledOnTouchOutside(false);//设置用户点击其他区域不关闭
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0x00ffffff));
        dlg.show();

        ok.setTag(dlg);
        cancel.setTag(dlg);

        params.width = UITools.getWindowWidth(activity) - 120;
        dlg.getWindow().setAttributes(params);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dlg = (Dialog) v.getTag();
                dlg.cancel();
                if (dialogButtonClickListener != null) {
                    dialogButtonClickListener.onClickOk();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dlg = (Dialog) v.getTag();
                dlg.cancel();
                if (dialogButtonClickListener != null) {
                    dialogButtonClickListener.onClickCancel();
                }
            }
        });
    }

    public Dialog getCurrentDialog() {
        return currentDialog;
    }

    public void setCurrentDialog(Dialog currentDialog) {
        this.currentDialog = currentDialog;
    }

    public interface DialogButtonClickListener {
        public void onClickOk();

        public void onClickCancel();
    }
    public interface DialogSingleButtonClickListener{
        public void onClick(int choiceNum,float totalPrice);
    }

    /**
     * 创建一个自定义弹框
     * 只含有一个自定义名称的按钮
     */
    public void createCustomDialog(final Activity activity, final float mPrice,final int hireNum,
                                   DialogSingleButtonClickListener
                                           mDialogSingleButtonClickListener) {

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_custom_one_button, null);
        TextView price = (TextView) view.findViewById(R.id.tv_price);
        price.setText(String.valueOf(mPrice));
        final EditText number = (EditText) view.findViewById(R.id.et_number);
        final TextView sum = (TextView) view.findViewById(R.id.tv_sum);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    int num = Integer.valueOf(s.toString());
                    if (num>hireNum) {
                        number.setText(String.valueOf(hireNum));
                        return;
                    }
                    sum.setText("" + num * mPrice);
                } else {
                    sum.setText("0");
                }
            }
        });
        number.setText("1");

        View ok = view.findViewById(R.id.btn_dialog_one_button_ok);
        Dialog dlg = new Dialog(activity, R.style.float_dialog);
        currentDialog = dlg;
        dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.dimAmount = 0.6f;
        dlg.setCanceledOnTouchOutside(true);//设置用户点击其他区域不关闭
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0x00ffffff));
        dlg.show();

        ok.setTag(dlg);

        params.width = UITools.getWindowWidth(activity) - 120;
        dlg.getWindow().setAttributes(params);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付
                if ("0".equals(sum.getText().toString())) {
                    ToastUtils.toastShort(R.string.weixin_pay_repay_zero);
                    return;
                }
                if (mDialogSingleButtonClickListener!=null) {
                    mDialogSingleButtonClickListener.onClick(
                            Integer.valueOf(number.getText().toString()),
                            Float.valueOf(sum.getText().toString()));
                    dlg.dismiss();
                }
            }
        });
    }

    /**
     * 创建一个自定义弹框
     * EditText不可编辑
     * 只含有一个自定义名称的按钮
     */
    public void createCustomDialogCannotEdit(
            final Activity activity,
            final float mPrice,
            final int realNum,
            DialogSingleButtonClickListener mDialogSingleButtonClickListener) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_custom_one_button_uneditable, null);
        TextView price = (TextView) view.findViewById(R.id.tv_price);
        price.setText(String.valueOf(mPrice));
        final EditText number = (EditText) view.findViewById(R.id.et_number);
        final TextView sum = (TextView) view.findViewById(R.id.tv_sum);
        number.setEnabled(false);
        number.setText(""+realNum);
        sum.setText(""+mPrice*realNum);
        View ok = view.findViewById(R.id.btn_dialog_one_button_ok);
        Dialog dlg = new Dialog(activity, R.style.float_dialog);
        currentDialog = dlg;
        dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.dimAmount = 0.6f;
        dlg.setCanceledOnTouchOutside(true);//设置用户点击其他区域不关闭
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0x00ffffff));
        dlg.show();

        ok.setTag(dlg);

        params.width = UITools.getWindowWidth(activity) - 120;
        dlg.getWindow().setAttributes(params);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付
                if ("0".equals(sum.getText().toString())) {
                    ToastUtils.toastShort(R.string.weixin_pay_repay_zero);
                    return;
                }
                if (mDialogSingleButtonClickListener!=null) {
                    mDialogSingleButtonClickListener.onClick(
                            realNum,
                            Float.valueOf(sum.getText().toString()));
                    dlg.dismiss();
                }
            }
        });
    }

    public void createUpdateVersionDialog(final Activity activity,boolean isForce,String title,String desc,String url){
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_version,null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_update_version_title);
        TextView tvDesc = (TextView) view.findViewById(R.id.tv_update_version_desc);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_update_version_cancel);
        TextView tvClose = (TextView) view.findViewById(R.id.tv_close_button);
        CircularProgressButton cpb = (CircularProgressButton) view.findViewById(R.id.cpb_update_version);
        tvTitle.setText(title);
        tvDesc.setText(desc);
        if (isForce) {
            tvClose.setVisibility(View.INVISIBLE);
        }else{
            tvClose.setVisibility(View.VISIBLE);
        }
        //立即更新
        cpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminder = cpb.getText().toString();
                LogUtils.i(TAG,"reminder ... "+reminder);
                String updateStart = Tools.getStringByResouceId(R.string.update_version_button_start);
                String updateError = Tools.getStringByResouceId(R.string.update_version_button_error);
                String updateEnd =  Tools.getStringByResouceId(R.string.update_version_button_complete);
                //点击是开始更新
                if (updateStart.equals(reminder)) {
                    UpdateVersionAsyncTask task = new UpdateVersionAsyncTask(cpb);
                    //HttpConstant.TEST_DOWNLOAD_APP 是测试地址
                    task.execute(HttpConstant.TEST_DOWNLOAD_APP);
//                    task.execute(url);
                }//点击是更新错误
                else if( updateError.equals(reminder)){
                    if (currentDialog!=null) {
                        currentDialog.dismiss();
                    }
                }//点击是安装
                else if (updateEnd.equals(reminder)) {
                    if (currentDialog!=null) {
                        currentDialog.dismiss();
                    }
                    String dirName = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator+"hrmp.apk";
                    File file = new File(dirName);
                    if (file.exists()) {
                        Tools.installApk(activity,file);
                    }else{
                        LogUtils.e(TAG,"file path ["+dirName+"] is not exist");
                    }
                }
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDialog!=null) {
                    currentDialog.dismiss();
                }
            }
        });
        Dialog dlg = new Dialog(activity, R.style.float_dialog);
        currentDialog = dlg;
        dlg.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.dimAmount = 0.6f;
        dlg.setCanceledOnTouchOutside(false);//设置用户点击其他区域不关闭
        dlg.setCancelable(false);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0x00ffffff));
        dlg.show();
        params.width = UITools.getWindowWidth(activity) - 120;
        dlg.getWindow().setAttributes(params);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDialog!=null) {
                    currentDialog.dismiss();
                }
            }
        });
    }

    public void dismissDialog(){
        if (currentDialog!=null) {
            currentDialog.dismiss();
        }
    }
}
