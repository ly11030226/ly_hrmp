package com.hrmp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hrmp.presenter.BasePresenter;



public abstract class BaseActivity<V, T extends BasePresenter<V>> extends Activity {

    protected T mPresenter;//Presenter对象
    protected Dialog mDialog;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();//创建Presenter
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
    public void showDialog(String str){
        if (mDialog!=null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = ProgressDialog.show(this,"",str,false,false);
    }
    public void hideDialog(){
        if (mDialog!=null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    protected abstract T createPresenter();

}
