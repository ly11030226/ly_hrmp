package com.hrmp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrmp.R;

/**
 *
 */

public class Titlebar extends RelativeLayout implements View.OnClickListener{


    private TextView tvTitle;
    private LinearLayout llBack;
    private TitleBarClickListener titleBarClickListener;
    public Titlebar(Context context) {
        this(context, null);
    }

    public Titlebar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_title_bar,this,true);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(this);
    }
    public void setTitleBarClickListener(TitleBarClickListener listener){
        this.titleBarClickListener = listener;
    }
    public void setTitle(String string){
        tvTitle.setText(string);
    }
    public void hideLeftButton(){
        llBack.setVisibility(View.INVISIBLE);
    }
    public void showLeftButton(){
        llBack.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                if (titleBarClickListener!=null) {
                    titleBarClickListener.clickLeft();
                }
            default:
                break;
        }
    }

    public interface TitleBarClickListener{
        public void clickLeft();
        public void clickRight();
    }
    public void setTitleByPosition(int position){
        switch (position) {
            case 0:
                tvTitle.setText(getResources().getString(R.string.bottom_bar_enroll));
                break;
            case 1:
                tvTitle.setText(getResources().getString(R.string.bottom_bar_message));
                break;
            case 2:
                tvTitle.setText(getResources().getString(R.string.bottom_bar_info));
                break;
            default:
                tvTitle.setText(getResources().getString(R.string.bottom_bar_enroll));
                break;
        }
    }
}
