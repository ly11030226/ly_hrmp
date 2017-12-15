package com.hrmp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.bean.Work;
import com.hrmp.view.Titlebar;

import java.util.ArrayList;
import java.util.List;

/**
 * 招工详情(未用 请看 WorkDetailActivity)
 *
 */

public class EnrollDetailActivity extends MyBaseActivity implements View.OnClickListener{
    private Work workMsg;
    private Titlebar titlebar;
    private List<WorkMsgContentListener> listeners = new ArrayList<WorkMsgContentListener>();
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private BasicInfoFragment basicInfoFragment;
    private EntryFormFragment entryFormFragment;
    private HandleHistoryFragment handleHistoryFragment;
    private Button btnEnroll;
    private TextView tvBasicInfo,tvEntryForm,tvHandleHistory;
    private FrameLayout flMain;
    private Fragment currentFragment;
    private List<TextView> tvs = new ArrayList<TextView>();
    private int fragmentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enroll_detail);
        try {
            initView();
            initTitleBar();
            initData();
            addListener();
            workMsg = (Work) getIntent().getSerializableExtra(EnrollFragment.KEY_ENROLL_WROK_MESSAGE);
            if (workMsg!=null) {
                for (WorkMsgContentListener listener:listeners){
                    listener.fillLayout(workMsg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        btnEnroll = (Button) findViewById(R.id.btn_enroll);
        tvBasicInfo = (TextView) findViewById(R.id.tv_basic_info);
        tvEntryForm = (TextView) findViewById(R.id.tv_entry_form);
        tvHandleHistory = (TextView) findViewById(R.id.tv_handle_history);
        flMain = (FrameLayout) findViewById(R.id.fl_main);
    }

    private void addListener() {
        btnEnroll.setOnClickListener(this);
        tvBasicInfo.setOnClickListener(this);
        tvEntryForm.setOnClickListener(this);
        tvHandleHistory.setOnClickListener(this);
    }

    private void initData() {
        basicInfoFragment = new BasicInfoFragment();
        entryFormFragment = new EntryFormFragment();
        handleHistoryFragment = new HandleHistoryFragment();
        fragments.add(basicInfoFragment);
        fragments.add(entryFormFragment);
        fragments.add(handleHistoryFragment);
        listeners.add(basicInfoFragment);
//        listeners.add(entryFormFragment);
//        listeners.add(handleHistoryFragment);
        tvs.add(tvBasicInfo);
        tvs.add(tvEntryForm);
        tvs.add(tvHandleHistory);
        fragmentIndex = 0;
        showFragmentByIndex(0);
    }

    private void initTitleBar() {
        titlebar = (Titlebar) findViewById(R.id.enroll_title_bar);
        titlebar.setTitle(getResources().getString(R.string.enroll_detail));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enroll:
                break;
            case R.id.tv_basic_info:
                showFragmentByIndex(0);
                break;
            case R.id.tv_entry_form:
                showFragmentByIndex(1);
                break;
            case R.id.tv_handle_history:
                showFragmentByIndex(2);
                break;
            default:
                break;
        }
    }

    public interface WorkMsgContentListener{
        public void fillLayout(Work workMsg);
    }
    public void showFragmentByIndex(int index){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //判断当前的Fragment是否为空，不为空则隐藏
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        //先根据Tag从FragmentTransaction事物获取之前添加的Fragment
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragments.get(index).getClass().getName());
        if (fragment==null) {
            //如fragment为空，则之前未添加此Fragment。便从集合中取出
            fragment = fragments.get(index);
        }
        currentFragment = fragment;
        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (!fragment.isAdded()) {
            ft.add(R.id.fl_main,fragment,fragment.getClass().getName());
        }else{
            ft.show(fragment);
        }
        ft.commit();
        setTextViewGrayOrBlueColor(index);
    }
    private void setTextViewGrayOrBlueColor(int index){
        for (TextView tv : tvs){
            tv.setTextColor(getResources().getColor(R.color.edittext_hint_color));
        }
        TextView one = tvs.get(index);
        one.setTextColor(getResources().getColor(R.color.blue_main));
    }
}
