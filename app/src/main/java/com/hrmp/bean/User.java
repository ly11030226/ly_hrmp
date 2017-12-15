package com.hrmp.bean;

import java.io.Serializable;

/**
 * Created by Ly on 2017/4/29.
 * 用户实体类
 */

public class User implements Serializable{
    private int id;
    private String password;
    private String loginName;
    private String userName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", loginName='" + loginName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}