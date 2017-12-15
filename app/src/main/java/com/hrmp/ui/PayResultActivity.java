package com.hrmp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.view.Titlebar;

/**
 * 付款结果界面
 */
public class PayResultActivity extends MyBaseActivity {
    private static final String TAG = "PayResultActivity";
    private Titlebar titlebar;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_result);
        try {
            initTitleBar();
            btnBack = (Button) findViewById(R.id.btn_pay_result_back);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initTitleBar() {
        titlebar = (Titlebar) findViewById(R.id.pay_result_title_bar);
        titlebar.setTitle(getString(R.string.pay_result_title_bar));
        titlebar.showLeftButton();
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
}
