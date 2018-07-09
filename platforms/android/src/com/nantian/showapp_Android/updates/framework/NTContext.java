package com.nantian.showapp_Android.updates.framework;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.nantian.showapp_Android.updates.framework.NTConstants.Spec;

/**
 * $Id: NTContext.java 2932 2014-06-27 09:10:14Z Genty $
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTContext {

	/** 系统上下文 */
	private Context applicationContext;

	private View topView;

	/** 当前的Activity */
	private Activity currentActivity;

	/** 应用程序版本 */
	private String applicationVersion;

	/** 根据系统生成的用户代理 */
	private String userAgen;

	/** 屏幕每英寸点数 */
	private int densityDpi = Spec.DEFAULT_DENSITY_DPI;

	/** 设备信息 */
	private String device;

	/** 平台信息 */
	private String platform;

	/** 地图标注距被标注点的垂直距离 */
	private int overlay_marginTop;

	/** 从手机银行客户端传过来的皮肤图片id */
	private int skinId;

	private final Stack<Exception> errors = new Stack<Exception>();

	public void addError(Exception e) {
		errors.add(e);
	}

	/**
	 * @return the applicationContext
	 */
	public Context getApplicationContext() {
		return applicationContext;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public synchronized Activity getCurrentActivity() {
		return currentActivity;
	}

	public int getDensityDpi() {
		return densityDpi;
	}

	public String getDevice() {
		return device;
	}

	public int getOverlay_marginTop() {
		return overlay_marginTop;
	}

	public String getPlatform() {
		return platform;
	}

	public int getSkinId() {
		return skinId;
	}

	public synchronized View getTopView() {
		return topView;
	}

	public String getUserAgen() {
		return userAgen;
	}

	/**
	 * @param applicationContext the applicationContext to set
	 */
	public void setApplicationContext(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public synchronized void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

	public void setDensityDpi(int densityDpi) {
		if (densityDpi != 0) {
			this.densityDpi = densityDpi;
		}
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public void setOverlay_marginTop(int overlay_marginTop) {
		this.overlay_marginTop = overlay_marginTop;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setSkinId(int skinId) {
		this.skinId = skinId;
	}

	public synchronized void setTopView(View topView) {
		this.topView = topView;
	}

	public void setUserAgen(String userAgen) {
		this.userAgen = userAgen;
	}

}
