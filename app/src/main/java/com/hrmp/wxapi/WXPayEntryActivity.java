package com.hrmp.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hrmp.Constant;
import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.WXPay;
import com.hrmp.util.LogUtils;
import com.hrmp.view.Titlebar;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付回调界面（必须设置）
 */

public class WXPayEntryActivity extends MyBaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    private Titlebar titlebar;
    private IWXAPI api;
    private WXPay wxPay;
    private Button btnPay;
    private TextView tvOrderDesc;
    private TextView tvOrderMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wx_pay_entry);
        try {
            initWXPay();
            initTitleBar();
            initView();
            initIntent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initIntent() {
        if (getIntent()!=null && getIntent().hasExtra(Constant.KEY_WXPAY_ENTITY)) {
            wxPay = (WXPay) getIntent().getSerializableExtra(Constant.KEY_WXPAY_ENTITY);
            if (wxPay!=null) {
                tvOrderDesc.setText(wxPay.getPrepayId());
                tvOrderMoney.setText("¥"+wxPay.getUnitPrice()*wxPay.getNum());
            }
        }
    }

    private void initTitleBar() {
        titlebar = (Titlebar) findViewById(R.id.wx_pay_title_bar);
        titlebar.setTitle(Tools.getStringByResouceId(R.string.weixin_pay_order));
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

    private void initView() {
        tvOrderDesc = (TextView) findViewById(R.id.tv_order_desc);
        tvOrderMoney = (TextView) findViewById(R.id.tv_order_money);
        btnPay = (Button) findViewById(R.id.btn_do_wx_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPay();
            }
        });
    }

    private void initWXPay() {
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_REPAY_APPID);
        api.registerApp(Constant.WEIXIN_REPAY_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG,"onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.i(TAG, "onPayFinish, errCode = " + baseResp.errCode);// 支付结果码
        Intent intent = new Intent(Constant.ACTION_WX_PAY_RESULT);
        intent.putExtra(Constant.KEY_WX_PAY_RESULT_CODE,baseResp.errCode);
        sendBroadcast(intent);
        finish();
    }

    private void doPay(){
        showDialog(Tools.getStringByResouceId(R.string.dialog_send_pay_request));
        if (api!=null && wxPay!=null) {
            PayReq request = new PayReq();
            request.appId = wxPay.getAppId();
            request.partnerId = wxPay.getPartnerId();
            request.prepayId= wxPay.getPrepayId();
            request.packageValue = "Sign=WXPay";
            request.nonceStr= wxPay.getNonceStr();
            request.timeStamp= wxPay.getTimestamp();
            request.sign= wxPay.getSign();
            LogUtils.i(TAG,
                    "appId "+wxPay.getAppId()+"||" +
                            "partnerId "+wxPay.getPartnerId()+"||" +
                            "prepayId "+wxPay.getPrepayId()+"||" +
                            "nonceStr "+wxPay.getNonceStr()+"||" +
                            "timeStamp "+wxPay.getTimestamp()+"||" +
                            "sign "+wxPay.getSign());
            boolean result = api.sendReq(request);
            LogUtils.i(TAG,"after api.sendReq result ... "+result);

        }else{
            LogUtils.e(TAG,"doPay IWXAPI api is null");
        }
    }

}
