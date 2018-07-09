package com.nantian.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Base64;
import android.util.Log;
import cn.nt.esafe.xybank.SafeUtil;

public class NTSecretPlugin extends CordovaPlugin {
	
	private static CallbackContext mCallbackContext;
	
	/**
	 * Constructor.
	 */
	public NTSecretPlugin() {
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
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		mCallbackContext = callbackContext;
		try{
			final SafeUtil safeUtil = new SafeUtil();
			safeUtil.verify(cordova.getActivity());
			String type = args.getString(0);
			String msg = args.getString(1); 
			
			if(action.equals("encrypt")){
				byte[] enstr_rsa = null;
				if("rsa".equals(type)){
					enstr_rsa = safeUtil.rsaEncryptByPublicKey(msg.getBytes());
				}else if("3des".equals(type)){
					enstr_rsa = safeUtil.encrypt3Des(msg.getBytes());
				}else{
					mCallbackContext.error("未知加密类型");
					return true;
				}
				String pw = Base64.encodeToString(enstr_rsa, Base64.DEFAULT);
				pw = pw.replace("\n", "").replace("+", "%2B");
				mCallbackContext.success(pw);
				
			}else if(action.equals("decrypt")){
				String card = null;
				if("rsa".equals(type)){
					card = new String(safeUtil.rsaDecryptByPublicKey(Base64.decode(msg, Base64.DEFAULT)));
				}else if("3des".equals(type)){
					card = new String(safeUtil.decrypt3Des(Base64.decode(msg, Base64.DEFAULT)));
				}else{
					mCallbackContext.error("未知解密类型");
					return true;
				}
				mCallbackContext.success(card);
			}
        }catch(Exception e){
        	Log.e("NTSecretPlugin", "失败， " + e.getMessage());
        	mCallbackContext.error("error");
        }
          
		return true;
	}
}
