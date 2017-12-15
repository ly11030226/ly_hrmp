package com.hrmp.model;

import com.hrmp.bean.ReqDetail;
import com.hrmp.util.RequestBaseUtil;

/**
 * Created by Ly on 2017/4/29.
 */

public class UserBiz implements IUserBiz{
    public static UserBiz userBiz = null;
    private UserBiz() {
    }
    public static UserBiz getInstance(){
        if (userBiz==null){
            userBiz = new UserBiz();
        }
        return userBiz;
    }

    @Override
    public void doLogin(String username, String psd,final IThreadListener threadListener) {

//        StringBuilder sb = new StringBuilder();
//        sb.append("<reqMsg>")
//                .append("<operater>")
//                .append(username)
//                .append("</operater>")
//                .append("<identification></identification>")
//                .append("<reqDetail>")
//                .append("<password>")
//                .append(psd)
//                .append("</password>")
//                .append("</reqDetail>")
//                .append("</reqMsg>");
//        SoapUtil soap = new SoapUtil(HttpConstant.NAME_SPACE_2,"appLogin","appLogin",sb.toString(),"",threadListener);
//        soap.sendRequest();
                new RequestBaseUtil() {
                    @Override
                    public ReqDetail setReqDetail() {
                        ReqDetail reqDetail = new ReqDetail();
                        reqDetail.setPassword(psd);
                        return reqDetail;
                    }
                }.sendRequest(username,"appLogin", "appLogin",threadListener);
    }

    @Override
    public boolean checkIsFirstLogin() {
        return false;
    }
    @Override
    public void doBgLogin() {

    }
}
