package com.nantian.showapp_Android.updates.framework.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;

import com.nantian.showapp_Android.updates.framework.NTContext;

/**
 * $Id: NTContextUtils.java 2472 2014-04-15 01:35:40Z Genty $
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTContextUtils {

	private static final String TAG = "ContextUtils";

	private static final String LEFT_SLASH = "/";

	private static final NTContext CONTEXT = new NTContext();

	public static NTContext getContext() {
		return CONTEXT;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static Point getScreenSize() {
		Point size = new Point();
		Display display = CONTEXT.getCurrentActivity().getWindowManager().getDefaultDisplay();
		if (Build.VERSION.SDK_INT > 12) {
			display.getSize(size);
		} else {
			size.x = display.getWidth();
			size.y = display.getHeight();
		}
		return size;
	}

	public static boolean isOnline() {
		return NTNetworkUtils.isOnline(CONTEXT.getApplicationContext());
	}

	/**
	 * 方法功能说明：组装MBC-User-Agent请求头参数的值
	 * 
	 * @param context
	 * @return String 请求头信息MBC-User-Agent的值
	 */
	public static String getUserAgent() {
		// 组装版本信息MBCCCB/Android/[手机系统版本]/[程序版本号]/[页面版本号]/[设备id]
		StringBuffer sb = new StringBuffer();
		// sb.append("MBCMAP/Android/");
		sb.append("MBCCCB/Android/");
		// sb.append("CCBMAP/Android/");
		String systemversion = "";
		int sdk_level = android.os.Build.VERSION.SDK_INT;
		switch (sdk_level) {
		case 2:
			systemversion = "Android 1.1";
			break;
		case 3:
			systemversion = "Android 1.5";
			break;
		case 4:
			systemversion = "Android 1.6";
			break;
		case 5:
			systemversion = "Android 2.0";
			break;
		case 6:
			systemversion = "Android 2.01";
			break;
		case 7:
			systemversion = "Android 2.1";
			break;
		case 8:
			systemversion = "Android 2.2";
			break;
		case 9:
			systemversion = "Android 2.3.1";
			break;
		case 10:
			systemversion = "Android 2.3.3";
			break;
		case 11:
			systemversion = "Android 3.0";
			break;
		case 12:
			systemversion = "Android 3.1";
			break;
		case 13:
			systemversion = "Android 3.2";
			break;
		case 14:
			systemversion = "Android 4.0";
			break;
		case 15:
			systemversion = "Android 4.0.3";
			break;
		case 16:
			systemversion = "Android 4.1";
			break;
		case 17:
			systemversion = "Android 4.2";
			break;
		default:
			systemversion = "Android 2.0";
			break;
		}
		sb.append(systemversion).append(LEFT_SLASH);
		sb.append(CONTEXT.getApplicationVersion()).append(LEFT_SLASH);
		// 页面版本号，获取通讯中银行域名为key，获取不到则以文件名pversion位key。
		// CmpBTCMySharedPreferences sf_pversion = new CmpBTCMySharedPreferences(context, CmpBTCGlobal.PVERSION_FILE, Context.MODE_PRIVATE);
		// CmpBTCMySharedPreferences sf_host = new CmpBTCMySharedPreferences(context, CmpBTCGlobal.DEFAULT_FILE, Context.MODE_PRIVATE);
		// String pversion_key = sf_host.getString(CmpBTCGlobal.MBSURL_BANK_HOST, CmpBTCGlobal.PVERSION_FILE);
		// String pversion = sf_pversion.getString(pversion_key, CmpBTCGlobal.INIT_VERSION);
		sb.append("9.04");
		sb.append(LEFT_SLASH);
		// 获取设备id
		TelephonyManager telMgr = (TelephonyManager) CONTEXT.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceID = telMgr.getDeviceId();
		sb.append(deviceID);
		return sb.toString();
	}

	public static void initialize(Application application) {
		NTContextUtils.getContext().setApplicationContext(application);
		// Logger.debug(TAG, String.format("config authorizations->baidumap->key[%s]", ConfigUtils.getConfig("authorizations", "baidumap", "key")));
		// Logger.debug(TAG, String.format("config servers->product->domain[%s]", ConfigUtils.getConfig("servers", "product", "domain")));
		// Logger.debug(TAG, String.format("config authorizations->tencent->openid[%s]", ConfigUtils.getConfig("authorizations", "tencent", "openid")));
		// Logger.debug(TAG, String.format("Build->MODEL[%s],PRODUCT[%s],VERSION.CODENAME[%s], MANUFACTURER[%s], ID[%s], VERSION.RELEASE[%s],FINGERPRINT[%s]", Build.MODEL, Build.PRODUCT, Build.ID,
		// Build.VERSION.CODENAME, Build.MANUFACTURER, Build.VERSION.RELEASE, Build.FINGERPRINT));
		// EbsSafeManager.init(application);
		try {
			String version = CONTEXT.getApplicationContext().getPackageManager().getPackageInfo(CONTEXT.getApplicationContext().getPackageName(), 0).versionName;
			NTLogger.debug(TAG, version);
			NTContextUtils.getContext().setApplicationVersion(version);
			NTContextUtils.getContext().setUserAgen(NTContextUtils.getUserAgent());
			NTLogger.debug(TAG, NTContextUtils.getContext().getUserAgen());

		} catch (NameNotFoundException e) {
			String message = "Failed to get application version";
			NTLogger.warn(TAG, message);
			NTContextUtils.getContext().addError(new Exception(message));
		}
		NTContextUtils.getContext().setDensityDpi(CONTEXT.getApplicationContext().getResources().getDisplayMetrics().densityDpi);
		NTContextUtils.getContext().setDevice(String.format("%s,%s,%s", Build.MANUFACTURER, Build.PRODUCT, Build.MODEL));
		NTContextUtils.getContext().setPlatform(String.format("%s,Android,%s", Build.MANUFACTURER, Build.VERSION.RELEASE));

		NTLogger.debug(TAG, "Created.");
	}

	public static void showMessage(String message) {
		NTLogger.debug("CCBBranchMapApplication", message);
	}

}
