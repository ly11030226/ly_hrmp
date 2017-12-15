package com.hrmp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hrmp.Constant;
import com.hrmp.HRMPApplication;
import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.ReqDetail;
import com.hrmp.bean.RspMsg;
import com.hrmp.bean.WXPay;
import com.hrmp.model.IThreadListener;
import com.hrmp.util.LogUtils;
import com.hrmp.util.RequestBaseUtil;
import com.hrmp.util.ToastUtils;
import com.hrmp.view.Titlebar;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 支付界面
 * (默认只有微信支付)
 */

public class PayMainActivity extends MyBaseActivity{
    private static final String TAG = "PayMainActivity";
    private Titlebar titlebar;
    private Button btnPay;
    private WXPay wxPay;
    private IWXAPI msgApi;
    private TextView tvOrderDesc;
    private TextView tvOrderMoney;
    private WXPayResultReceiver wxPayResultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_main);
        try {
            initTitleBar();
            initView();
            initIntent();
            registReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registReceiver() {
        wxPayResultReceiver = new WXPayResultReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_WX_PAY_RESULT);
        registerReceiver(wxPayResultReceiver,intentFilter);
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
        titlebar = (Titlebar) findViewById(R.id.pay_main_title_bar);
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
        tvOrderDesc = (TextView) findViewById(R.id.tv_pay_main_order_desc);
        tvOrderMoney = (TextView) findViewById(R.id.tv_pay_main_order_money);
        btnPay = (Button) findViewById(R.id.btn_do_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wxPay!=null) {
                    doPay();
                }
            }
        });
        msgApi = WXAPIFactory.createWXAPI(PayMainActivity.this, null);
        msgApi.registerApp(Constant.WEIXIN_REPAY_APPID);
    }
    private void doPay(){
        showDialog(Tools.getStringByResouceId(R.string.dialog_send_pay_request));
        if (msgApi!=null) {
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
            boolean result = msgApi.sendReq(request);
            LogUtils.i(TAG,"after msgApi.sendReq result ... "+result);

        }else{
            LogUtils.e(TAG,"doPay IWXAPI msgApi is null");
        }
    }
    /**
     * 查询微信结果
     */
    private void queryWXPayResult(String workId){
//        StringBuilder sb = new StringBuilder();
//        sb.append("<reqMsg>")
//                .append("<operater>")
//                .append(HRMPApplication.getInstance().currentUser.getLoginName())
//                .append("</operater>")
//                .append("<reqDetail>")
//                .append("<workId>")
//                .append(workId)
//                .append("</workId>")
//                .append("</reqDetail>")
//                .append("</reqMsg>");
//        SoapUtil soap = new SoapUtil(HttpConstant.NAME_SPACE_2, "queryWXPayResult", "queryWXPayResult", sb.toString(), "", new IThreadListener() {
//            @Override
//            public void success(RspMsg rspMsg) {
//                hideDialog();
////                ToastUtils.toastLong(rspMsg.getRspDetail().getPayDescri());
//                Intent intent = new Intent(PayMainActivity.this,PayResultActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void fail(RspMsg rspMsg) {
//                hideDialog();
//                if (!TextUtils.isEmpty(rspMsg.getRspDesc())) {
//                    ToastUtils.toastLong(rspMsg.getRspDesc());
//                }
//            }
//        });
//        soap.sendRequest();
        new RequestBaseUtil() {
            @Override
            public ReqDetail setReqDetail() {
                ReqDetail reqDetail = new ReqDetail();
                reqDetail.setWorkId(workId);
                return reqDetail;
            }
        }.sendRequest(HRMPApplication.getInstance().currentUser.getLoginName(), "queryWXPayResult", "queryWXPayResult", new IThreadListener() {
            @Override
            public void success(RspMsg rspMsg) {
                hideDialog();
                //ToastUtils.toastLong(rspMsg.getRspDetail().getPayDescri());
                Intent intent = new Intent(PayMainActivity.this,PayResultActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void fail(RspMsg rspMsg) {
                hideDialog();
                if (!TextUtils.isEmpty(rspMsg.getRspDesc())) {
                    ToastUtils.toastLong(rspMsg.getRspDesc());
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        LogUtils.i(TAG,"onDestroy");
        super.onDestroy();
        if (wxPayResultReceiver!=null) {
            unregisterReceiver(wxPayResultReceiver);
        }
    }

    class WXPayResultReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null && Constant.ACTION_WX_PAY_RESULT.equals(intent.getAction())) {
                int code = intent.getIntExtra(
                        Constant.KEY_WX_PAY_RESULT_CODE,
                        BaseResp.ErrCode.ERR_SENT_FAILED);
                int result = 0;
                switch (code) {
                    case BaseResp.ErrCode.ERR_OK:
                        result = R.string.weixin_pay_errcode_success;
                        //查询结果
                        if (wxPay!=null) {
                            queryWXPayResult(wxPay.getWorkId());
                        }
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        result = R.string.weixin_pay_errcode_cancel;
                        hideDialog();
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        result = R.string.weixin_pay_errcode_deny;
                        hideDialog();
                        break;
                    case BaseResp.ErrCode.ERR_UNSUPPORT:
                        result = R.string.weixin_pay_errcode_unsupported;
                        hideDialog();
                        break;
                    default:
                        result = R.string.weixin_pay_errcode_unknown;
                        hideDialog();
                        break;
                }
                ToastUtils.toastLong(result);
            }
        }
    }
}
