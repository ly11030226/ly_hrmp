package com.hrmp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.Date;

@XStreamAlias("work")
public class Work implements Serializable{
	
	private String id;
	
	/**
	 * 单号
	 */
	private String businessNumber;
	
	/**
	 * 发布人
	 */
	private String publisherName;
	
	/**
	 * 发布人所在公司
	 */
	private String publisherCompanyName;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 用工单位
	 */
	private String workCompany;
	
	/**
	 * 发布时间
	 */
	private Date publishTime;
	
	/**
	 * 关闭时间
	 */
	private Date closeTime;
	
	/**
	 * 工作地点
	 */
	private String workArea;
	
	/**
	 * 工作描述
	 */
	private String workDescri;
	
	/**
	 * 需要工种
	 */
	private String workKind;
	
	/**
	 * 预计用工开始时间
	 */
	private Date planStartTime;
	
	/**
	 * 预计用工结束时间
	 */
	private Date planEndTime;
	
	/**
	 * 招工数量
	 */
	private int hireNum;
	/**
	 * 招工状态
	 * noPublish：草稿 publishing：正在招工 closed：已关闭 delete：删除
	 */
	private String status;
	
	/**
	 * 报名费单价，单位：元/人，用于“我要报名”弹出页面
	 */
	private float unitPrice;


	/**
	 * 报名时间
	 */
	private String signTime;

	/**
	 * 是否能取消报名
	 * 2：待支付      1：可以取消报名；   0：不可以取消报名
	 * @return
     */
	private String canCancelSign;


	private String payStatus;

	private String payDescri;

	public String getPayDescri() {
		return payDescri;
	}

	public void setPayDescri(String payDescri) {
		this.payDescri = payDescri;
	}

	private String prepayId;

	private int num;

	private float payFee;


	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public float getPayFee() {
		return payFee;
	}

	public void setPayFee(float payFee) {
		this.payFee = payFee;
	}

	public String getCanCancelSign() {
		return canCancelSign;
	}

	public void setCanCancelSign(String canCancelSign) {
		this.canCancelSign = canCancelSign;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisherCompanyName() {
		return publisherCompanyName;
	}

	public void setPublisherCompanyName(String publisherCompanyName) {
		this.publisherCompanyName = publisherCompanyName;
	}

	public String getWorkCompany() {
		return workCompany;
	}

	public void setWorkCompany(String workCompany) {
		this.workCompany = workCompany;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getWorkDescri() {
		return workDescri;
	}

	public void setWorkDescri(String workDescri) {
		this.workDescri = workDescri;
	}

	public String getWorkKind() {
		return workKind;
	}

	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	public int getHireNum() {
		return hireNum;
	}

	public void setHireNum(int hireNum) {
		this.hireNum = hireNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
	public String toString() {
		return "Work{" +
				"workDescri='" + workDescri + '\'' +
				", signTime='" + signTime + '\'' +
				", id='" + id + '\'' +
				", title='" + title + '\'' +
				'}';
	}
}
