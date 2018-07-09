package com.nantian.plugins;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nantian.showapp_Android.ExampleUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;

public class JPushPlugin extends CordovaPlugin {

		 public Activity activity;
	    @Override
		public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) 
	            throws JSONException {
	    	activity  = this.cordova.getActivity();

	        Log.i("ExtraInfo", action);
	        Log.i("ExtraInfo", args.getString(0));
	       
	        if (action.equals("setAlias")){
	        	
	        	setAlias(args.getString(0));
	        	
	        }
	       
	        Log.i("getstockinfo", "Ok");
	        return true;
	    }
	    
	    
	    private void setAlias(String msg) {
			
	        // EditText aliasEdit = (EditText) findViewById(R.id.et_alias);
	      //   String alias = "18674032829";
	    	msg = msg.replace("\"", "");
	    	msg = msg.replace("[", "");
	    	msg = msg.replace("]", "");
	    	String alias = msg;
	    	Log.i("setAlias", "alias="+alias);
	         if (TextUtils.isEmpty(alias)) {
	          //   Toast.makeText(PushSetActivity.this,R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
	        	 Log.i("myJpush", "isEmpty");
	             return;
	         }
	         if (!ExampleUtil.isValidTagAndAlias(alias)) {
	           //  Toast.makeText(PushSetActivity.this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
	        	 Log.i("myJpush", "isvalid");
	             return;
	         }

	         // 调用 Handler 来异步设置别名
	         mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	     }

	     private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
	         @Override
	         public void gotResult(int code, String alias, Set<String> tags) {
	             String logs ;
	             switch (code) {
	             case 0:
	                 logs = "Set tag and alias success";
	                 Log.i("myJpush", logs);
	                 // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
	                 break;
	             case 6002:
	                 logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
	                 Log.i("myJpush", logs);
	                 // 延迟 60 秒来调用 Handler 设置别名
	                 mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
	                 break;
	             default:
	                 logs = "Failed with errorCode = " + code;
	                 Log.i("myJpush", logs);
	             }
	          //   ExampleUtil.showToast(logs, activity.getApplicationContext());
	         }

	 	
	     };
	     private static final int MSG_SET_ALIAS = 1001;
	     Handler mHandler = new Handler() {  
	         public void handleMessage(android.os.Message msg) {
	         
	             switch (msg.what) {
	                 case MSG_SET_ALIAS:
	                     Log.i("myJpush", "Set alias in handler."+(String)msg.obj);
	                     // 调用 JPush 接口来设置别名。
	                     /*
	                     JPushInterface.setAliasAndTags(activity.getApplicationContext(),
	                                                     (String) msg.obj,
	                                                      null,
	                                                      mAliasCallback);
	                                                      */
	                     JPushInterface.setAlias(activity.getApplicationContext(),
	                    		 (String) msg.obj, mAliasCallback);
	                 break;
	             default:
	                 Log.i("myJpush", "Unhandled msg - " + msg.what);
	             }
	         }                                       
	     };

	}