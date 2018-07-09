package com.nantian.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.nantian.config.ConfigurationConstant;
import com.nantian.showapp_Android.LoadingActivity;
import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.mfp.NTBusinessController;

public class NTUpdatePlugin extends CordovaPlugin {
	private static CallbackContext mCallbackContext;
	private Activity activity;
	private ProgressDialog dialog;

	// zip包是否有更新
	public static boolean zipUpdate;
	/**
	 * Constructor.
	 */
	public NTUpdatePlugin() {
	}

	/**
	 * Sets the context of the Command. This can then be used to do things like
	 * get file paths associated with the Activity.
	 * 
	 * @param cordova
	 *            The context of the main Activity.
	 * @param webView
	 *            The CordovaWebView Cordova is running in.
	 */
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}

	/**
	 * Executes the request and returns PluginResult.
	 * 
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArry of arguments for the plugin.
	 * @param callbackContext
	 *            The callback id used when calling back into JavaScript.
	 * @return True if the action was valid, false if not.
	 */
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		mCallbackContext = callbackContext;
		activity = cordova.getActivity();
		try{
			if(action.equals("update")){
				dialog = new ProgressDialog(webView.getContext());
				dialog.setMessage("正在检查，请稍后...");
				dialog.setCancelable(false);
				dialog.show();
				NTBusinessController.getInstance().checkApkVersion(activity, new ResultListener() {
					@Override
					public void onProgress(long currentLen, long totalLen, boolean downloaded) {
						//进入这里表明apk有更新
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(dialog != null){
									dialog.cancel();
									dialog = null;
								}
							}
						});
					}
					
					@Override
					public void onExecuted(Object result, NTServiceException se) {
						if(se != null){
							packageDownloadListener.onExecuted(null, se);
							return;
						}
						//进入这里表明apk没有更新，接着检查版本的更新
						if(ConfigurationConstant.PAGE_LOAD_STRATEGY.equals("server")){
							NTBusinessController.getInstance().loadZipInfo(activity, packageDownloadListener, false);
						}else{
							packageDownloadListener.onExecuted(null, null);
						}
					}
				});
				
			}else if(action.equals("getVersion")){
				SharedPreferences pf = activity.getSharedPreferences(NTConstants.ZIP_INFO, Activity.MODE_PRIVATE);
				String zipVersion = pf.getString(NTConstants.ZIP_VERSION, "");
				PackageManager manager = activity.getPackageManager();
				PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
				String appVersion = info.versionName;
				mCallbackContext.success("{\"zip_version\":\"" + zipVersion + "\", \"app_version\":\"" + appVersion + "\"}");
			}
		}catch(Exception e){
			e.printStackTrace();
			mCallbackContext.error("网络异常");
		}
		return true;
	}
	
	/** 业务包下载监听器 */
	private final ResultListener packageDownloadListener = new ResultListener() {

		@Override
		public void onExecuted(Object result, final NTServiceException se) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(dialog != null){
						dialog.cancel();
						dialog = null;
					}
					if (se != null) {
						Toast.makeText(activity, se.getMessage(), Toast.LENGTH_LONG).show();
						return;
					}
					
					if(zipUpdate){
						Toast.makeText(activity, "更新完成，请重启应用！", Toast.LENGTH_LONG).show();
						activity.getApplication().onTerminate();
						// 需要退出应用，然后重启
					}else{
						Toast.makeText(activity, "当前已是最新版本", Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		@Override
		public void onProgress(long currentLen, long totalLen, boolean loaded) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(dialog != null){
						dialog.cancel();
						dialog = null;
					}
				}
			});
			LoadingActivity.updateProgress(currentLen, totalLen, false);
		}
	};
}
