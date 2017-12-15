package com.hrmp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("androidVersion")
public class AndroidVersion implements Serializable{

	private String version;
	private String versionPath;
	private String versionDesc;
	private String isForceUpdate;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersionPath() {
		return versionPath;
	}
	public void setVersionPath(String versionPath) {
		this.versionPath = versionPath;
	}
	public String getVersionDesc() {
		return versionDesc;
	}
	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}
	public String getIsForceUpdate() {
		return isForceUpdate;
	}
	public void setIsForceUpdate(String isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}
}
