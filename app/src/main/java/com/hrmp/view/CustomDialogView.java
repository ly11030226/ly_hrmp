package com.hrmp.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hrmp.R;
import com.hrmp.util.LogUtils;

/**
 * Created by Ly on 2017/5/30.
 */

public class CustomDialogView extends FrameLayout{

    private EditText etNum;
    private TextView tvSum,tvPrice;
    private int price,num,sum;
    private Button btnCommit;
    public CustomDialogView(Context context) {
        this(context,null);
    }


    public CustomDialogView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        addListener();
    }

    private void addListener() {
        etNum.addTextChangedListener(new TextWatcher() {
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
                    sum = num * price;
                    tvSum.setText("" + num * price);
                } else {
                    sum = 0;
                    tvSum.setText("0");
                }
            }
        });
        btnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("result","总共花费"+sum+"元");
            }
        });
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.dialog_custom_one_button,this,true);
        etNum = (EditText) findViewById(R.id.et_number);
        tvSum = (TextView) findViewById(R.id.tv_sum);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        btnCommit = (Button) findViewById(R.id.btn_dialog_one_button_ok);
    }

    public void initData(int mPrice){
        this.price = mPrice;
    }
}
