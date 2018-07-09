package com.nantian.config;

public class ConfigurationConstant {
	/**
	 * https双向验证证书密码，两个证书都一样
	 */
	public static final String SSLPWd = "123456";
	
	//===========分享==============start
	/**
	 * 在微信平台注册的appkey
	 */
	public static final String WEIXIN_APPKEY = "wxba328faeab1176d1";
	
	/**
	 * 在新浪微博平台注册的appkey
	 */
	public static final String SINA_APPKEY = "2182506800";
	
	//===========分享===============end
	
	/**
	 * 页面加载策略：local, server
	 * 
	 * 页面加载策略为local时，下面的参数皆可不理会。
	 * 页面加载策略为server时，下面的参数都必须按照正确的格式修改
	 */
	public static final String PAGE_LOAD_STRATEGY = "local";
	
	/**
	 * zip加载策略：unzip（解压出来）, ram（加载到内存）
	 */
	public static final String ZIP_LOAD_STRATEGY = "ram";
	
	/**
	 * 安全沙箱加密密码
	 */
	public static final String SANDBOX_PWD = "123456";
	
	//===========后台接口地址===============
	/**
	 * 页面版本检查更新的地址
	 */
	public static final String UPDATE_SERVER_URL = "http://168.39.5.57:8088";
	
}
