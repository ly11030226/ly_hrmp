package com.hrmp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("reqMsg")
public class ReqMsg implements Serializable{

	/**
	 * 操作人登录名
	 */
	private String operater;
	
	/**
	 * 身份鉴定
	 */
	private String identification;
	
	/**
	 * 请求报文体
	 */
	private ReqDetail reqDetail;

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public ReqDetail getReqDetail() {
		return reqDetail;
	}

	public void setReqDetail(ReqDetail reqDetail) {
		this.reqDetail = reqDetail;
	}

	@Override
	public String toString() {
		return "ReqMsg{" +
				"operater='" + operater + '\'' +
				", identification='" + identification + '\'' +
				", reqDetail=" + reqDetail +
				'}';
	}
}
