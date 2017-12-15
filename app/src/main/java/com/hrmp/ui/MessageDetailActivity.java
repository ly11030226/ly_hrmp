package com.hrmp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import com.hrmp.MyBaseActivity;
import com.hrmp.R;
import com.hrmp.Tools;
import com.hrmp.bean.Msg;
import com.hrmp.bean.RspMsg;
import com.hrmp.presenter.MessageDetailPresenter;
import com.hrmp.ui.iview.IMessageDetailView;
import com.hrmp.util.ToastUtils;
import com.hrmp.view.Titlebar;

/**
 * 消息详情
 */
public class MessageDetailActivity extends MyBaseActivity implements IMessageDetailView {

    private Titlebar titlebar;
    private Msg msg;
    private TextView tvTitle,tvSendUser,tvSendTime,tvContent;
    private MessageDetailPresenter messageDetailPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message_detail);
        try {
            initView();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        msg = (Msg) getIntent().getSerializableExtra(MessageFragment.KEY_MESSAGE_BEAN);
        if (msg!=null) {
            tvSendUser.setText(msg.getSendUserName());
            tvSendTime.setText(msg.getCreateTime());
            tvTitle.setText(msg.getMessageTitle());
            tvContent.setText(msg.getMessageContent());
            if (messageDetailPresenter==null) {
                messageDetailPresenter = new MessageDetailPresenter(MessageDetailActivity.this,
                        this);
            }
            messageDetailPresenter.getMessageDetail(msg.getId());
        }
    }

    private void initView() {
        tvSendUser = (TextView) findViewById(R.id.msg_detail_senduser);
        tvSendTime = (TextView) findViewById(R.id.msg_detail_sendtime);
        tvTitle = (TextView) findViewById(R.id.msg_detail_title);
        tvContent = (TextView) findViewById(R.id.msg_detail_content);
        titlebar = (Titlebar) findViewById(R.id.message_titlebar);
        titlebar.setTitle(getResources().getString(R.string.msg_detail_title));
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
    public void showProgress() {
        showDialog(Tools.getStringByResouceId(R.string.msg_detail_msg_detail_remind));
    }

    @Override
    public void hideProgress() {
        hideDialog();
    }

    @Override
    public void getMessageDetailSuccess(RspMsg rspMsg) {
        Msg m = rspMsg.getRspDetail().getMsg();
        String id = m.getId();
        if (msg.getId().equals(id)) {
            updateUI(m);
        }else{
            //传过来的id和新获取的id不同
            ToastUtils.toastShort(R.string.msg_detail_msg_error);
            finish();
        }
    }

    @Override
    public void getMessageDetailFail(RspMsg rspMsg) {
        String desc = rspMsg.getRspDesc();
        if (!TextUtils.isEmpty(desc)) {
            ToastUtils.toastShort(desc);
        }
    }
    private void updateUI(Msg m){
        tvSendUser.setText(m.getSendUserName());
        tvSendTime.setText(m.getCreateTime());
        tvTitle.setText(m.getMessageTitle());
        tvContent.setText(m.getMessageContent());
    }
}
