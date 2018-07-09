package com.nantian.showapp_Android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;

import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;

public class NTApplication extends Application {
	
	private Map<String, Activity> activitys = new HashMap<String, Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		NTContextUtils.getContext().setApplicationContext(this);
	}
	
	public void addActivity(String name, Activity activity) {
		this.activitys.put(name, activity);
	}

	public void removeActivity(String name) {
		this.activitys.remove(name);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		for (Activity activity : activitys.values()) {
			activity.finish();
		}
	}
	
	/**
	 * finish掉除了名称为name的activity
	 * @param name
	 */
	public void onFinishActivity(String name){
		for (Activity activity : activitys.values()) {
			if(activity.getClass().getSimpleName().equals(name)){
				continue;
			}
			activity.finish();
		}
	}
	
	public Activity getActivityByName(String name){
		return activitys.get(name);
	}

}
