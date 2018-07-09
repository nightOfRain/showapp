package com.nantian.showapp_Android;

import org.json.JSONException;
import org.json.JSONObject;
import com.nantian.element.http.SyncHttp;
import com.nantian.showapp_Android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class CallMeToUpdate {
	 private Thread updateThread;
	 private UpdateManager mUpdateManager;
	 int versionCode;
	 int newCode;
	 String ApkUrl;
	 public static String Code;
	 public static String currcode;
	 String ip="";
	 String port="";
	 private Dialog noticeDialog;  
	 String pathstr;
	 private static JSONObject obj;
	 private static final String TAG = "Update_App";
	 
	 Context context;
	 public void CallMeToUpdate(){
		 
	 }
	 
	 public void InitUpdate(Context context){
		 Log.i("InitUpdate", "can you see me?");
		 this.context = context;
		
		 updateThread = new Thread(new UpdateApp());        
			updateThread.start();
		
	 }
	 
	 Handler mHandler = new Handler(){  
	        public void handleMessage(Message msg) {  
	        	Log.i("what", "what="+msg.what);
	            switch (msg.what) {    
	            case 1:  				
					String msg1 = "有新版本需要更新";
					Log.i("ABC", msg1);	
					Log.i("ABC", "Code=" + Code);
					Log.i("ABC", "currcode=" + currcode);
					mUpdateManager.checkUpdateInfo(ApkUrl,Code,currcode, pathstr); 				
	                break;  
	            default:  
	                break;  
	            }  
	        };  
	    };
		public class UpdateApp implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					updatemanager();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    };
	    
	    private void updatemanager() throws JSONException {
			ip = (String) context.getResources().getString(R.string.ip);
			port = (String) context.getResources().getString(R.string.port);
			obj = new JSONObject();
			Log.i("ABCIP", ip);
			Log.i("ABCPORT", port);
			
			try {			
				obj.put("upstat", "0");
				obj.put("id", "1"); //add for apple
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			try {
				Log.i("ABC", obj.toString());
				String respone = SyncHttp
						.httpPostJson(							
								this.context, "http://"+ip+":"+port+"/app/6023",
								obj.toString());

				Log.i("ABC", respone);
				if (respone != null & !"".equals(respone)) {

					JSONObject resObj = new JSONObject(respone);
					
					resObj=resObj.getJSONObject("data");
									
					ApkUrl =  (String) resObj.get("apkurl");	
					Code =  (String) resObj.get("versioncode");	
					newCode = Integer.parseInt(Code);
					versionCode = getVerCode();
					currcode = String.valueOf(versionCode);
					Log.i("ApkUrl", ApkUrl);
					Log.i("newCode", Code);
					Log.i("currCode", currcode);
					
					
					if( newCode > versionCode ){
						Log.i("newCode", "what");
						mHandler.sendEmptyMessage(1);
					}						
					else{
						Log.i("newCode", "what happen");
						 Looper.prepare();
						Log.i("MyAPp", "已是最新版本无需更新");
						new AlertDialog.Builder(context)
										.setTitle("更新提示")
										.setMessage("已是最新版本无需更新")
										.setNegativeButton("确定", null)
										.show();
						Looper.loop();
					
					}
						//Toast.makeText(this.context.getApplicationContext(), "没有最新版本需要更新", Toast.LENGTH_SHORT).show();
					
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
	            verCode = context.getPackageManager().getPackageInfo(  
	                    "com.nantian.Jrwgh_Android", 0).versionCode;  
	        } catch (NameNotFoundException e) {  
	            Log.e(TAG, e.getMessage());  
	        }  
	        return verCode;  
	    } 
}
