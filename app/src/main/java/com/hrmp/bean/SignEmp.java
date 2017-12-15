package com.hrmp.bean;

import java.io.Serializable;
import java.util.Date;


public class SignEmp implements Serializable{
	
	private String workId;
	
	private String empId;

	/**
	 * 报名时间
	 */
	private Date signTime;

	/**
	 * 工人名称
	 */
	private String empName;
	
	/**
	 * 报名人数
	 */
	private String num;
	
	/**
	 * 报名费单价，单位：元/人
	 */
	private String unitPrice;
	
	/**
	 * 总金额
	 */
	private String totalMoney;

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

}
