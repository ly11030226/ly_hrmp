package com.hrmp.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrmp.R;

/**
 * Created by Ly on 2017/5/6.
 */

public class BottomButtonBar extends LinearLayout implements View.OnClickListener{

    RelativeLayout rlEnroll,rlMessage,rlInfo;
    CheckedTextView ctvEnroll,ctvMessage,ctvInfo;
    TextView tvEnrol,tvMessage,tvInfo;
    BottomButtonClickListener bottomBottonButtonClickListener;
    public BottomButtonBar(Context context) {
        this(context, null);
    }

    public BottomButtonBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomButtonBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_bottom_button,this,true);
    }
    public void createView(){
        rlEnroll = (RelativeLayout) findViewById(R.id.rl_enroll);
        rlMessage = (RelativeLayout) findViewById(R.id.rl_message);
        rlInfo = (RelativeLayout) findViewById(R.id.rl_info);
        ctvEnroll = (CheckedTextView) findViewById(R.id.ctv_enroll);
        ctvMessage = (CheckedTextView) findViewById(R.id.ctv_message);
        ctvInfo = (CheckedTextView) findViewById(R.id.ctv_info);
        tvEnrol = (TextView) findViewById(R.id.tv_enrol);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        rlEnroll.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
    }
    public void setLightByPosition(int position){
        switch (position) {
            case 0:
                setEnrollLight();
                break;
            case 1:
                setMessageLight();
                break;
            case 2:
                setInfoLight();
                break;
            default:
                setEnrollLight();
                break;
        }
    }
    public void setEnrollLight(){
        ctvEnroll.setChecked(true);
        ctvMessage.setChecked(false);
        ctvInfo.setChecked(false);
        tvEnrol.setTextColor(Color.parseColor("#168edb"));
        tvMessage.setTextColor(Color.parseColor("#999999"));
        tvInfo.setTextColor(Color.parseColor("#999999"));
    }
    public void setMessageLight(){
        ctvEnroll.setChecked(false);
        ctvMessage.setChecked(true);
        ctvInfo.setChecked(false);
        tvEnrol.setTextColor(Color.parseColor("#999999"));
        tvMessage.setTextColor(Color.parseColor("#168edb"));
        tvInfo.setTextColor(Color.parseColor("#999999"));
    }
    public void setInfoLight(){
        ctvEnroll.setChecked(false);
        ctvMessage.setChecked(false);
        ctvInfo.setChecked(true);
        tvEnrol.setTextColor(Color.parseColor("#999999"));
        tvMessage.setTextColor(Color.parseColor("#999999"));
        tvInfo.setTextColor(Color.parseColor("#168edb"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_enroll:
                if (bottomBottonButtonClickListener!=null) {
                    bottomBottonButtonClickListener.clickEnroll();
                }
                break;
            case R.id.rl_message:
                if (bottomBottonButtonClickListener!=null) {
                    bottomBottonButtonClickListener.clickMessage();
                }
                break;
            case R.id.rl_info:
                if (bottomBottonButtonClickListener!=null) {
                    bottomBottonButtonClickListener.clickInfo();
                }
                break;
            default:
                break;
        }
    }
    public void setBottomButonClickListener(BottomButtonClickListener mListener){
        this.bottomBottonButtonClickListener = mListener;
    }
    public interface BottomButtonClickListener{
        public void clickEnroll();
        public void clickMessage();
        public void clickInfo();
    }
}
