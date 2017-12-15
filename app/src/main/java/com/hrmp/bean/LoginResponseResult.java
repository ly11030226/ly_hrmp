package com.hrmp.bean;

import java.io.Serializable;

/**
 * Created by Ly on 2017/5/20.
 */

public class LoginResponseResult implements Serializable{
    private String inputUsername;  //用户输入的用户名
    private String identification;  //验证信息
    private int resultCode;       //返回码
    private String resultDesc;   //返回内容
    private User user;  //返回用户信息，当没有成功登陆的时候，user为null
    public LoginResponseResult(){}
    public LoginResponseResult(int resultCode,String resultDesc){
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    public String getInputUsername() {
        return inputUsername;
    }

    public void setInputUsername(String inputUsername) {
        this.inputUsername = inputUsername;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "LoginResponseResult{" +
                "resultDesc='" + resultDesc + '\'' +
                ", resultCode=" + resultCode +
                ", inputUsername='" + inputUsername + '\'' +
                '}';
    }
}
