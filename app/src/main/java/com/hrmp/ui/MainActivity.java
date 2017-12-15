package com.hrmp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;

import com.hrmp.Constant;
import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.adapter.PageContentFragementAdapter;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.User;
import com.hrmp.presenter.LoginPresenter;
import com.hrmp.ui.iview.ILoginView;
import com.hrmp.util.LogUtils;
import com.hrmp.util.SpUtils;
import com.hrmp.view.BottomButtonBar;
import com.hrmp.view.MyViewPager;
import com.hrmp.view.Titlebar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import static com.hrmp.Constant.KEY_USER_ID;


public class MainActivity extends MyBaseActivity implements ILoginView{
    private static final String TAG = "MainActivity";
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    private Titlebar titleBar;
    private BottomButtonBar bottomButtonBar;
    private MyViewPager viewPager;
    private PageContentFragementAdapter pagerAdapter;
    private EnrollFragment enrollFragment;
    private MessageFragment messageFragment;
    private InfoFragment infoFragment;
    private PushAgent mPushAgent;
    private LoginPresenter backLoginPresenter;
    private int currentPage = 0;  //当前页面
    private int prePage = 0;   //上一个页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        try {
            initIntent();
            /**
             * 初始化友盟Push
             */
            initUmengPush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initUmengPush() {
        LogUtils.i(TAG,"initUmengPush");
        mPushAgent = PushAgent.getInstance(this);
        //开启推送并设置注册的回调处理
        mPushAgent.enable(mRegisterCallback);
        updateStatus();
    }
    public Handler handler = new Handler();
    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    updateStatus();
                }
            });
        }
    };

    private void updateStatus() {
        String deviceToken = "";
        String pkgName = getApplicationContext().getPackageName();
        if (mPushAgent.isRegistered()) {
            deviceToken = mPushAgent.getRegistrationId();
        }
        LogUtils.i(TAG,"updateStatus packName ... "+pkgName+
                "   isRegisted ... "+mPushAgent.isRegistered()+
                "   deviceToken ... "+deviceToken);
    }
    private void initMain() {
        initData();
        initViewPager();
        addListener();
        //查询系统版本
        backLoginPresenter.checkVersion();
    }

    private void initIntent() {
        backLoginPresenter = new LoginPresenter(this);
        if (getIntent()!=null && getIntent().hasExtra(Constant.KEY_LOGIN_STYLE)) {
            String style = getIntent().getStringExtra(Constant.KEY_LOGIN_STYLE);
            if (Constant.BACKGROUND_LOGIN.equals(style)) {
                String name = SpUtils.getString(Constant.KEY_USER_LOGINNAME,"");
                String psd = SpUtils.getString(Constant.KEY_USER_PSD,"");
                if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(psd)) {
                    backLoginPresenter.login(name,psd);
                }else{
                    Tools.logout(MainActivity.this);
                }
            }else if (Constant.FOREGROUND_LOGIN.equals(style)) {
                initMain();
                if (mPushAgent!=null) {
                    mPushAgent.setAlias(SpUtils.getString(KEY_USER_ID,""),"hrmpId");
                }
                MobclickAgent.onProfileSignIn(SpUtils.getString(KEY_USER_ID,""));

            }
        }
    }

    private void addListener() {
        bottomButtonBar.setBottomButonClickListener(new BottomButtonBar.BottomButtonClickListener() {
            @Override
            public void clickEnroll() {
                viewPager.setCurrentItem(0,false);
                bottomButtonBar.setEnrollLight();
                titleBar.setTitle(getResources().getString(R.string.bottom_bar_enroll));
            }

            @Override
            public void clickMessage() {
                viewPager.setCurrentItem(1,false);
                bottomButtonBar.setMessageLight();
                titleBar.setTitle(getResources().getString(R.string.bottom_bar_message));
            }

            @Override
            public void clickInfo() {
                viewPager.setCurrentItem(2,false);
                bottomButtonBar.setInfoLight();
                titleBar.setTitle(getResources().getString(R.string.bottom_bar_info));
            }
        });
    }


    private void initViewPager() {
        viewPager = (MyViewPager) findViewById(R.id.myviewpager);
        enrollFragment = new EnrollFragment();
        messageFragment = new MessageFragment();
        infoFragment = new InfoFragment();
        fragmentList.add(enrollFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(infoFragment);
        pagerAdapter = new PageContentFragementAdapter(this.getSupportFragmentManager(), this, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        //设置页与页之间的间距
        viewPager.setPageMargin(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                titleBar.setTitleByPosition(position);
                bottomButtonBar.setLightByPosition(position);
            }

            @Override
            public void onPageSelected(int position) {
                recordUmengData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void recordUmengData(int position) {
        prePage = currentPage;
        currentPage = position;
        umengPageEnd(prePage);
        umengPageStart(currentPage);
    }

    private void initData() {
        titleBar = (Titlebar) findViewById(R.id.title_bar);
        titleBar.setTitle(getString(R.string.bottom_bar_enroll));
        titleBar.hideLeftButton();
        bottomButtonBar = (BottomButtonBar) findViewById(R.id.bottom_button_bar);
        bottomButtonBar.createView();
        bottomButtonBar.setEnrollLight();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
//            moveTaskToBack(true);
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void login(String username, String psd) {

    }

    @Override
    public void clearUserName() {

    }

    @Override
    public void clearPsd() {

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

    }

    @Override
    public void loginSuccess(User user) {
        initMain();
        if (mPushAgent!=null) {
            mPushAgent.setAlias(SpUtils.getString(KEY_USER_ID,""),"hrmpId");
        }
        MobclickAgent.onProfileSignIn(SpUtils.getString(KEY_USER_ID,""));
    }

    @Override
    public void loginFail(String content) {
        Tools.logout(MainActivity.this);
    }

    @Override
    public void checkVersionSuccess(RspMsg rspMsg) {

    }

    @Override
    public void checkVersionFail(RspMsg rspMsg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        umengPageStart(currentPage);
        MobclickAgent.onResume(MainActivity.this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        umengPageEnd(currentPage);
        MobclickAgent.onPause(MainActivity.this);
    }
    private void umengPageStart(int position){
        switch (position) {
            case 0:
                MobclickAgent.onPageStart(EnrollFragment.TAG);
                break;
            case 1:
                MobclickAgent.onPageStart(MessageFragment.TAG);
                break;
            case 2:
                MobclickAgent.onPageStart(InfoFragment.TAG);
                break;
            default:
                break;
        }
    }
    private void umengPageEnd(int positon){
        switch (positon) {
            case 0:
                MobclickAgent.onPageEnd(EnrollFragment.TAG);
                break;
            case 1:
                MobclickAgent.onPageEnd(MessageFragment.TAG);
                break;
            case 2:
                MobclickAgent.onPageEnd(InfoFragment.TAG);
                break;
            default:
                break;
        }
    }

}
