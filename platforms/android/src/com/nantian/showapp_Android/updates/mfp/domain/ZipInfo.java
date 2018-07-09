package com.nantian.showapp_Android.updates.mfp.domain;

import java.io.Serializable;

public class ZipInfo implements Serializable {
	private static final long serialVersionUID = 6727402711775573479L;
	
	/** 包的id */
	private String zip_id;
	/** 应用的id */
	private String app_id;
	/** 包的版本 */
	private String zip_version;
	/** 包的类型 */
	private String zip_type;
	/** 包的大小 */
	private String zip_size;
	/** 包的md5 */
	private String zip_md5;
	/** 资源包的存放路径 */
	private String path;
	/** 资源包的解压路径 */
	private String targetDir;
	
	public String getZip_id() {
		return zip_id;
	}
	public void setZip_id(String zip_id) {
		this.zip_id = zip_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getZip_version() {
		return zip_version;
	}
	public void setZip_version(String zip_version) {
		this.zip_version = zip_version;
	}
	public String getZip_type() {
		return zip_type;
	}
	public void setZip_type(String zip_type) {
		this.zip_type = zip_type;
	}
	public String getZip_size() {
		return zip_size;
	}
	public void setZip_size(String zip_size) {
		this.zip_size = zip_size;
	}
	public String getZip_md5() {
		return zip_md5;
	}
	public void setZip_md5(String zip_md5) {
		this.zip_md5 = zip_md5;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTargetDir() {
		return targetDir;
	}
	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}
	
}
