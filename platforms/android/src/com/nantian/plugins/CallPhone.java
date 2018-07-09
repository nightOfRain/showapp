package com.nantian.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CallPhone extends CordovaPlugin { 
  
    @Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) 
            throws JSONException {
        Activity activity = this.cordova.getActivity();

        Log.i("CallPhone", action);
        Log.i("CallPhone", args.getString(0));
       
      if( action.equals("show")){
        	call_phone(args.getString(0));
        }
       
        Log.i("getstockinfo", "Ok");
        return true;
    }
    
	
	private void call_phone(String phoneno){
		Activity activity = this.cordova.getActivity();
		Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneno));
	//	Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneno));    
	    activity.startActivity(dialIntent);
	}
}