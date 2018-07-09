/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.nantian.showapp_Android;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.nantian.config.ConfigurationConstant;
import com.nantian.element.http.SyncHttp;
import com.nantian.plugins.NTDataDictionaryPlugin;
import com.nantian.showapp_Android.NTApplication;
import com.nantian.showapp_Android.R;
import com.nantian.showapp_Android.UpdateManager;
import com.nantian.showapp_Android.CordovaApp.UpdateApp;
import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;
import com.nantian.showapp_Android.updates.framework.util.NTDialogUtils;
import com.nantian.showapp_Android.updates.framework.util.NTLogger;
import com.nantian.showapp_Android.updates.framework.util.NTNetworkUtils;
import com.nantian.showapp_Android.updates.mfp.NTBusinessController;
import com.nantian.showapp_Android.updates.mfp.view.NTIceCreamWebViewClient;

public class CordovaApp extends CordovaActivity {

	private static class EnhancementWebView extends CordovaWebView {

		private boolean visible = false;

		public EnhancementWebView(Context context) {
			super(context);
		}

		@Override
		public void onDetachedFromWindow() {// this will be trigger when back
											// key pressed, not when
			if (!visible) {
				try {
					destroy();
				} catch (Exception e) {
				}
			}
			super.onDetachedFromWindow();
		}

		@Override
		public void onWindowVisibilityChanged(int visibility) {
			Log.i("LWJ", "when i be?");
			super.onWindowVisibilityChanged(visibility);
			if (visibility == View.GONE) {
				try {
					WebView.class.getMethod("onPause").invoke(this);
					
				} catch (Exception e) {
				}
				pauseTimers();
				visible = false;
			} else if (visibility == View.VISIBLE) {
				try {
					WebView.class.getMethod("onResume").invoke(this);
				} catch (Exception e) {
				}
				resumeTimers();
				visible = true;
			}
		}
	}

	@Override
	protected CordovaWebView makeWebView() {
		return new EnhancementWebView(this);
	}

	@Override
	protected CordovaWebViewClient makeWebViewClient(CordovaWebView webView) {

		NTLogger.debug(TAG, "pageLoadStrategy[" + pageLoadStrategy + "]");

		if (STRATEGY_SERVER.equals(pageLoadStrategy)) {
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
				NTDialogUtils.showToast(this, "系统版本过低, 不支持此功能", 2 * 1000, 500);
				return null;
			} else {
				return new NTIceCreamWebViewClient(this, webView);
			}
		}
		return super.makeWebViewClient(webView);

	}
	
	/** 保存引导页打开的时间 */
	private long openLandingPageTime;
	/** 引导页最少显示的时间 */
	private final long miniTime = 3000;
	
	private static final String TAG = "CordovaApp";
	
	private static final String STRATEGY_SERVER = "server";
	
	/** 是否被覆盖 */
	private boolean needAlarm = true;
	
	private static String pageLoadStrategy = ConfigurationConstant.PAGE_LOAD_STRATEGY;
	private static String zipLoadStrategy = ConfigurationConstant.ZIP_LOAD_STRATEGY;
	
	private static String landingPageUrl = "file:///android_asset/landing.html";
	int versionCode;
	int newCode;
	String ApkUrl;
	public static String Code;
	public static String currcode;
	String ip="";
	String port="";
	private Thread updateThread;
	private Thread keepThread;
	 private UpdateManager mUpdateManager;
	 private Dialog noticeDialog;  
	 String pathstr;
	 private static JSONObject obj;

	/** 业务包下载监听器 */
	private final ResultListener packageDownloadListener = new ResultListener() {
		
		@Override
		public void onExecuted(Object result, final NTServiceException se) {
			
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					if (se != null) {
						NTDialogUtils.showToast(getActivity(), se.getMessage(), 2000, 500);
						// 发生异常后需要加载一个错误的界面显示
						CordovaApp.this.startActivity(new Intent(CordovaApp.this, ErrorActivity.class));
						return;
					}
					String firstPage = NTBusinessController.getInstance().getFirstPageName();
					if (!TextUtils.isEmpty(firstPage)) {
						if(NTConstants.ZIPLOADSTRATEGY_UNZIP.equals(zipLoadStrategy)){
							File html5Folder = NTContextUtils.getContext().getApplicationContext().getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
							//拼接解压路径
							String target = html5Folder.toString() + "/" + NTConstants.Path.HTML5_UNZIP_FOLDER;
							//拼接完整的路径
							launchUrl = "file:///" + target + "/" + firstPage;
							
						}else{
							launchUrl = "file:///" + firstPage;
						}
						loadPage(launchUrl);
						
						Activity activity = ((NTApplication) CordovaApp.this.getApplication()).getActivityByName(LoadingActivity.class.getSimpleName());
						if(activity != null){
							activity.finish();
						}
					}else{
						CordovaApp.this.startActivity(new Intent(CordovaApp.this, ErrorActivity.class));
					}
				}
			});
		}
		
		@Override
		public void onProgress(long currentLen, long totalLen, boolean loaded) {
			LoadingActivity.updateProgress(currentLen, totalLen, loaded);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();
		
		/*ANDROID透明状态栏*/
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
//	           setTranslucentStatus(true);    
//	           SystemBarTintManager tintManager = new SystemBarTintManager(this);    
//	           tintManager.setStatusBarTintEnabled(true);    
//	           tintManager.setStatusBarTintResource(R.color.top_bg_color);//通知栏所需颜色  
//	       } 
		
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
	            Window window=this.getWindow();
	      
	            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.setStatusBarColor(Color.TRANSPARENT);

	        } else {
	            //。。。。
	            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	        }
		
		((NTApplication) this.getApplication()).addActivity(this.getClass().getSimpleName(), this);
		
		/*获取唯一设备号*/
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		String DEVICE_ID = tm.getDeviceId();
		NTDataDictionaryPlugin.dic.put("serinum", DEVICE_ID);
		Log.i("lwjid", DEVICE_ID);
		
		//防止远程跨域攻击
	//	appView.getSettings().setAllowFileAccessFromFileURLs(false);
	//	appView.getSettings().setAllowUniversalAccessFromFileURLs(false);
		/*加载页面*/
		loadUrl(landingPageUrl);
		

		
		setActivityResultCallback(appView.pluginManager.getPlugin("NTPictureTaker"));

		
		//极光推送注册
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		
		
		//本地下载地址
		pathstr = "/sdcard/appcheck/download";
        File file = new File(pathstr);
        //如果路径不存在就创建
        if(!file.exists())
        		file.mkdirs();
        
        //自动更新线程
        mUpdateManager = new UpdateManager(this);  
		//App 开始前先检查是否有新版本需要更新       
	    updateThread = new Thread(new UpdateApp());        
		updateThread.start();
			
		      
			
		openLandingPageTime = System.currentTimeMillis();
		if (STRATEGY_SERVER.equals(pageLoadStrategy)) {
			if(!NTNetworkUtils.isOnline(this)){
				NTDialogUtils.showToast(this, NTConstants.Error.NETWORK_ERROR, 2000, 500);
				CordovaApp.this.startActivity(new Intent(CordovaApp.this, ErrorActivity.class));
				return;
			}
			
			NTBusinessController.getInstance().loadZipInfo(CordovaApp.this, packageDownloadListener, true);

		} else {
			loadPage(launchUrl);
		}
		
		
	}
	     
	   private void setTranslucentStatus(boolean on) {    
	       Window win = getWindow();    
	       WindowManager.LayoutParams winParams = win.getAttributes();    
	       final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;    
	       if (on) {    
	           winParams.flags |= bits;    
	       } else {    
	           winParams.flags &= ~bits;    
	       }    
	       win.setAttributes(winParams);    
	   }
	Handler mHandler = new Handler(){  
        public void handleMessage(Message msg) {  
            switch (msg.what) {    
            case 1:  				
				String msg1 = "有新版本需要更新";
				Log.i("ABC", msg1);	
				Log.i("ABC", "Code=" + Code);
				Log.i("ABC", "currcode=" + currcode);
				Log.i("ABC", "pathstr=" + pathstr);
				Log.i("ABC", "ApkUrl=" + ApkUrl);
				
				mUpdateManager.checkUpdateInfo(ApkUrl,Code,currcode, pathstr); 				
                break;  
            default:  
                break;  
            }  
        };  
    };
	private void loadPage(final String url){
		CordovaApp.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 获取时间差
				long timeDifference = System.currentTimeMillis() - openLandingPageTime;
				long temp = miniTime - timeDifference;
				if (temp > 0) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							
							appView.loadUrl(url);
						}
					}, temp);
		
				}else{
					appView.loadUrl(url);
				}
			}
		});
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if((event.getKeyCode()==KeyEvent.KEYCODE_BACK || event.getKeyCode()==KeyEvent.KEYCODE_HOME)){
            needAlarm = false;
        }
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if(landingPageUrl.equals(appView.getUrl())){
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onResume() {
		Log.i("LWJ", "每次返回前台执行？");
		needAlarm = true;
		//每次返回前台时都执行更新和服务器的session操作
		//心跳线程
		keepThread = new Thread(new KeepApp());  
		keepThread.start();
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		if(needAlarm && !isAppInBackground()) {
			PackageManager manager = getApplication().getPackageManager();
			ApplicationInfo applicationInfo;
			try {
				applicationInfo = manager.getApplicationInfo(getPackageName(), 0);
			
	            //弹出警示信息
	            Toast.makeText(getApplicationContext(), manager.getApplicationLabel(applicationInfo).toString() + "已进入后台运行", Toast.LENGTH_LONG).show();
	            
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
        }
		super.onStop();
	}
	
	/**
	 * 获取当前应用进程是否在后台
	 * @return true表示程序在前台，false表示程序进入后台
	 */
	private boolean isAppInBackground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;

	}
	public class UpdateApp implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				updatemanager();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    };
    
	public class KeepApp implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				keep_heart();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    };
    private void updatemanager() throws IOException {
    	//获取配置里的IP信息
		ip = (String) this.getResources().getString(R.string.ip);
		port = (String) this.getResources().getString(R.string.port);
		obj = new JSONObject();
		Log.i("ABCIP", ip);
		Log.i("ABCPORT", port);
		
		try {
	
			obj.put("userid", "0");
			obj.put("token", "1"); //add for apple
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			Log.i("ABC6088", obj.toString());

			
			String respone1 = SyncHttp
					.httpPostJson(							
							
							this.getApplicationContext(), "http://"+ip+":"+port+"/app/6088",
							obj.toString());

			Log.i("ABC", respone1);
			if (respone1 != null & !"".equals(respone1)) {

				JSONObject resObj = new JSONObject(respone1);
				
				resObj=resObj.getJSONObject("data");				
				resObj=resObj.getJSONObject("updateapk");
				//获取服务器最新版本信息及下载路径
				ApkUrl =  (String) resObj.get("apkurl");	
				Code =  (String) resObj.get("versioncode");	
				newCode = Integer.parseInt(Code);
				
				//获取当前版本号
				versionCode = getVerCode();
				currcode = String.valueOf(versionCode);
				Log.i("ApkUrl", ApkUrl);
				Log.i("newCode", Code);
				Log.i("currCode", currcode);
				
				//如果最新版本大于当前版本则执行更新操作
				if( newCode > versionCode )
					mHandler.sendEmptyMessage(1);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//CrashHandler.saveException2File(e);
			e.printStackTrace();
			Log.e("ABC", e + "");
		}

		return;
		//
	}

    public int getVerCode() {  
        int verCode = -1;  
        try {  
            verCode = this.getPackageManager().getPackageInfo(  
                    "com.nantian.appCheck_Android", 0).versionCode;  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, e.getMessage());  
        }  
        return verCode;  
    } 
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		((NTApplication) this.getApplication()).removeActivity(this.getClass().getSimpleName());
	}
	
    private void keep_heart() throws IOException {
		ip = (String) this.getApplicationContext().getResources().getString(R.string.ip);
		port = (String) this.getApplicationContext().getResources().getString(R.string.port);
		JSONObject obj1 = new JSONObject();
	
		Log.i("ABCIP", ip);
		Log.i("ABCPORT", port);
		try {			
			String userid =  NTDataDictionaryPlugin.dic.get("userid");
			if (userid == null || userid.equals("")) {
				Log.i("ABCNULL", "没有获取到userid");
				return;
			}
			String token =  NTDataDictionaryPlugin.dic.get("token");
			obj1.put("user_id", userid);
			obj1.put("token", token); //add for apple
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		try {
			Log.i("ABC", obj1.toString());
			String respone3 = SyncHttp
					.httpPostJson(							
							this.getApplicationContext(), "http://"+ip+":"+port+"/app/6099?user_id="+obj1.getString("user_id")+"&token="+obj1.getString("token"),
							obj1.toString());

			Log.i("6099", respone3);
		
					//Toast.makeText(this.context.getApplicationContext(), "没有最新版本需要更新", Toast.LENGTH_SHORT).show();
				
			
		} catch (Exception e) {
			
			e.printStackTrace();
			Log.e("ABCerr", e + "");
		}

		return;
		
	}
}
