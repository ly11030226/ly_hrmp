package com.hrmp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.List;

@XStreamAlias("rspDetail")
public class RspDetail implements Serializable{

	/**
	 * 用户id
	 */
	@XStreamAlias("userId")
	private String userId;
	
	/**
	 * 用户登录名
	 */
	@XStreamAlias("userLoginName")
	private String userLoginName;
	
	/**
	 * 用户姓名
	 */
	@XStreamAlias("userName")
	private String userName;
	
	/**
	 * 安卓客户端版本
	 */
	private AndroidVersion androidVersion;
	
	/**
	 * 招工列表
	 */
	private List<Work> workList;
	
	private Work work;
	
	private List<SignEmp> signEmpList;
	
	private List<Msg> msgList;
	
	private Msg msg;

	/**
	 * 微信支付实体
	 * @return
	 */
	private WXPay wxPay;

	private String payStatus;
	private String payDescri;

	public AndroidVersion getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(AndroidVersion androidVersion) {
		this.androidVersion = androidVersion;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Work> getWorkList() {
		return workList;
	}

	public void setWorkList(List<Work> workList) {
		this.workList = workList;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public List<SignEmp> getSignEmpList() {
		return signEmpList;
	}

	public void setSignEmpList(List<SignEmp> signEmpList) {
		this.signEmpList = signEmpList;
	}

	public List<Msg> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<Msg> msgList) {
		this.msgList = msgList;
	}

	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

	public WXPay getWxPay() {
		return wxPay;
	}

	public void setWxPay(WXPay wxPay) {
		this.wxPay = wxPay;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayDescri() {
		return payDescri;
	}

	public void setPayDescri(String payDescri) {
		this.payDescri = payDescri;
	}

	@Override
	public String toString() {
		return "RspDetail{" +
				"workList=" + workList +
				", userLoginName='" + userLoginName + '\'' +
				'}';
	}
}
