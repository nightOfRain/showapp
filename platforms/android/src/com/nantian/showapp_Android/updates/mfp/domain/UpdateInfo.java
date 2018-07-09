package com.nantian.showapp_Android.updates.mfp.domain;

import java.io.Serializable;

public class UpdateInfo implements Serializable {
	private static final long serialVersionUID = -7245873467064129798L;
	
	private boolean hasUpdate;
	/** 应用包版本 */
	private String app_version;
	/** 是否静默下载安装 */
	private boolean isstatic;
	/** 是否强制更新 */
	private boolean forceupdate;
	/** 启动页的相对路径 */
	private String startpage;
	/** 是否包含基线版本 */
	private boolean hasBaseVerson;
	/** 更新简介 */
	private String updateMsg;
	
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public boolean isIsstatic() {
		return isstatic;
	}
	public void setIsstatic(boolean isstatic) {
		this.isstatic = isstatic;
	}
	public boolean isForceupdate() {
		return forceupdate;
	}
	public void setForceupdate(boolean forceupdate) {
		this.forceupdate = forceupdate;
	}
	public String getStartpage() {
		return startpage;
	}
	public void setStartpage(String startpage) {
		this.startpage = startpage;
	}

	public boolean isHasBaseVerson() {
		return hasBaseVerson;
	}
	public void setHasBaseVerson(boolean hasBaseVerson) {
		this.hasBaseVerson = hasBaseVerson;
	}
	public String getUpdateMsg() {
		return updateMsg;
	}
	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}
	public boolean isHasUpdate() {
		return hasUpdate;
	}
	public void setHasUpdate(boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}
	
}
