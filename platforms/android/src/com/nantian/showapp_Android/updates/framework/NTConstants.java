package com.nantian.showapp_Android.updates.framework;

/**
 * $Id NTConstants.java 4230 2014-10-15 173727Z Genty $
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTConstants {

	public static class Error {

		public static final String NETWORK_ERROR = "网络异常，请重试！";

		public static final String INVALID_RESPONSE = "服务响应非法！";

		public static final String SYSTEM_ERROR = "系统错误，请联系客服！";

		public static final String MD5_ERROR = "文件校验失败，请联系客服";

		public static final String GET_DATA_ERROR = "获取数据错误";

	}

	/**
	 * 文件存放路径定义
	 */
	public static class Path {

		public static final String HTML5_FOLDER = "html5";

		public static final String HTML5_TEMPLATE_FOLDER = "template";

		public static final String HTML5_UNZIP_FOLDER = "unzip";

	}

	/**
	 * 系统参数规格
	 */
	public static class Spec {

		/** 字符集 */
		public static final String CHARSET = "UTF-8";

		/** 默认屏幕显示点数 */
		public static final int DEFAULT_DENSITY_DPI = 160;

	}
	
	/**===============普通常量=================*/
	/** 固定参数--交易号 */
	public static final String PARAMS_TXCODE_KEY = "txcode";
	public static final String PARAMS_TXCODE_003 = "vem003";
	public static final String PARAMS_TXCODE_002 = "vem002";
	public static final String PARAMS_TXCODE_101 = "app101";
	public static final String PARAMS_TXCODE_102 = "vem102";
	
	
	public static final String ZIP_INFO = "ZipInfo";
	/** 首页相对与zip包的路径 */
	public static final String ZIP_FIRST_PAGE_NAME = "firstPageName";
	/** zip类型--业务包 */
	public static final String ZIP_TYPE_BUSINESS = "business";
	/** zip类型--资源包 */
	public static final String ZIP_TYPE_RESOURCE = "resource";
	
	public static final String ZIP_VERSION = "zip_version";
	/** 解压zip的key */
	public static final String ZIP_KEY = "123456";
	/** 业务包id的key */
	public static final String KEY_BUSINESS_ID = "business_id";
	/** 资源包id的key */
	public static final String KEY_RESOURCE_ID = "resource_id";
	/** 资源包md5的key */
	public static final String KEY_RESOURCE_MD5 = "resource_md5";
	
	public static final String KEY_BUSINESS_MD5 = "business_md5";
	/** 资源包version的key */
	public static final String KEY_RESOURCE_VERSION = "resource_version";
	/** 业务包version的key */
	public static final String KEY_BUSINESS_VERSION = "business_version";
	/** 解压到私有目录 */
	public static final String ZIPLOADSTRATEGY_UNZIP = "unzip";
	
}
