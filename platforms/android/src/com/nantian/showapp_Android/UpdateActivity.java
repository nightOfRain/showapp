package com.nantian.showapp_Android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.cordova.*;
public class UpdateActivity extends CordovaActivity {  
	private UpdateManager mUpdateManager;
	String apkurl;
	private Dialog noticeDialog;
	private boolean flag=false;
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        super.init();
 //       setContentView(R.layout.progress);
        

      Intent in = this.getIntent();
      String apkurl = in.getStringExtra("apkurl"); 
      String newCode = in.getStringExtra("newcode");
      String currCode = in.getStringExtra("versioncode");
 
  
      
      
     mUpdateManager = new UpdateManager(this);
  //   mUpdateManager.checkUpdateInfo(apkurl,newCode,currCode); 
      Log.i("here", "点取消后到这里吗？");
    // if(flag)
    	 loadUrl(launchUrl);
   // finish();
    }

	
  
}  