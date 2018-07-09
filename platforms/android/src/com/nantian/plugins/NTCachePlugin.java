package com.nantian.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class NTCachePlugin extends CordovaPlugin {
	private static CallbackContext mCallbackContext;

	/**
	 * Constructor.
	 */
	public NTCachePlugin() {
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
		Activity activity = cordova.getActivity();
		SharedPreferences preferences = activity.getSharedPreferences("NTCache", Activity.MODE_PRIVATE);
		try{
			if(action.equals("save")){
				String key = args.getString(0);
				String value = args.getString(1);
				Editor edit = preferences.edit();
				edit.putString(key, value);
				edit.commit();
				mCallbackContext.success();
			}else if(action.equals("get")){
				String key = args.getString(0);
				mCallbackContext.success(preferences.getString(key, ""));
			}
		}catch(Exception e){
			e.printStackTrace();
			mCallbackContext.error("程序异常");
		}
		return true;
	}
}
