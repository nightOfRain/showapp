/*
 * Copyright (C) 2014 Nantian Electronics Information. All Rights Reserved.
 *
 * 本程序中所包含的信息属于机密信息，如无南天的书面许可，任何人都无权复制或利用。
 */
package com.nantian.showapp_Android.updates.mfp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.nantian.plugins.NTUpdatePlugin;
import com.nantian.showapp_Android.LoadingActivity;
import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;
import com.nantian.showapp_Android.updates.framework.util.NTDialogUtils;
import com.nantian.showapp_Android.updates.mfp.domain.UpdateInfo;
import com.nantian.showapp_Android.updates.mfp.domain.ZipInfo;

/**
 * $Id$
 * 
 * 负责流程调度，资源管理
 * 
 * @author Genty
 * 
 */
public class NTBusinessController {

	public static synchronized NTBusinessController getInstance() {
		if (instance == null) {
			instance = new NTBusinessController();
		}
		return instance;
	}

	private static final String TAG = "NTBusinessController";

	private static NTBusinessController instance = null;

	private Activity pageContext;

	private ResultListener packageDownloadListener = null;

	//是否是强制更新
	private boolean isForcedUpdating = false;
	//是否静默更新
	private boolean isStatic = false;
	//当前更新下载的为apk
	private boolean isApkupdate = false;
	
	private ZipInfo downloadInfo;
	
	private boolean autoUpdate;

	private final Handler handler = new Handler(Looper.getMainLooper());

	private final ResultListener listener = new ResultListener() {
		
		@Override
		public void onExecuted(final Object result, final NTServiceException se) {

			handler.post(new Runnable() {

				@Override
				public void run() {
					if (se != null) {
						if(packageDownloadListener != null)
							packageDownloadListener.onExecuted(result, se);
						return;
					}
					start();
				}
			});

		}
		@Override
		public void onProgress(long currentLen, long totalLen, boolean downloaded) {
		}
	};


	private NTBusinessController() {
	}

	/**
	 * 获首页的名称
	 * @param pagePath
	 * @return
	 */
	public String getFirstPageName(){
		// 通过
		SharedPreferences preferences = pageContext.getSharedPreferences(NTConstants.ZIP_INFO, Context.MODE_PRIVATE);
		return preferences.getString(NTConstants.ZIP_FIRST_PAGE_NAME, "");
	}
	
	/**
	 * 检查apk版本信息
	 * @param packageDownloadListener
	 */
	public synchronized void checkApkVersion(final Activity activity, final ResultListener loadApkVersionListener){
		NTBusinessPackageManager.getInstance().loadApkVersionInfo(new ResultListener() {
			
			@Override
			public void onProgress(long currentLen, long totalLen, boolean downloaded) {
				
			}
			
			@Override
			public void onExecuted(Object result, NTServiceException se) {
				if(se != null){
					loadApkVersionListener.onExecuted(result, se);
					return;
				}
				
				UpdateInfo info = (UpdateInfo)result;
				if(info.isHasUpdate()){
					loadApkVersionListener.onProgress(1, 1, true);
					//有更新
					Intent intent = new Intent(pageContext, LoadingActivity.class);
					intent.putExtra("content", info.getUpdateMsg());
					intent.putExtra("autoUpdate", false);
					activity.startActivity(intent);
					isApkupdate = true;
				}else{
					isApkupdate = false;
					loadApkVersionListener.onExecuted(null, null);
				}
			}
		});
	}
	
	/**
	 * 联网获取zip信息
	 * @param activity
	 * @param packageDownloadListener
	 * @param autoUpdate 是否是自动检查更新
	 */
	public synchronized void loadZipInfo(Activity activity, ResultListener packageDownloadListener, boolean autoUpdate){
		NTContextUtils.getContext().setCurrentActivity(activity);
		this.pageContext = activity;
		this.packageDownloadListener = packageDownloadListener;
		this.autoUpdate = autoUpdate;
		// 加载目录树
		NTBusinessPackageManager.getInstance().downloadCatalogue(listener, autoUpdate);
	}
	
	/**
	 * 开始校验zip包的信息
	 */
	private synchronized void start() {

		//zip信息加载完毕
		final Map<String, ZipInfo> map = NTBusinessPackageManager.getInstance().getNTZipDownloadInfo();
		if(map.size() <= 0){
			NTUpdatePlugin.zipUpdate = false;
			packageDownloadListener.onExecuted(null, null);
			return;
		}
		
		Collection<ZipInfo> collection = map.values();
		downloadInfo = new ArrayList<ZipInfo>(collection).get(0);
		//是否需要更新
		boolean needUpdate = false;
		
		isStatic = NTBusinessPackageManager.getInstance().updateInfo.isIsstatic();
		isForcedUpdating = NTBusinessPackageManager.getInstance().updateInfo.isForceupdate();
		
		// 当有更新
		if(map.values().size() > 0){
			needUpdate = true;
		}
		if(needUpdate){
			NTUpdatePlugin.zipUpdate = true;
			isApkupdate = false;
			if(isStatic){
				// 静默下载更新
				NTBusinessPackageManager.getInstance().loadDownInfo(downloadInfo, packageDownloadListener);
			}else{
				Intent intent = new Intent(pageContext, LoadingActivity.class);
				intent.putExtra("content", NTBusinessPackageManager.getInstance().updateInfo.getUpdateMsg());
				intent.putExtra("autoUpdate", autoUpdate);
				pageContext.startActivity(intent);
			}
		}else{
			NTUpdatePlugin.zipUpdate = false;
			packageDownloadListener.onExecuted(null, null);
		}
	}
	
	public void update_yes(){
		NTBusinessPackageManager.getInstance().loadDownInfo(downloadInfo, packageDownloadListener);
//		// 保存最新的版本信息
//		Editor edit = pageContext.getSharedPreferences(NTConstants.ZIP_INFO, Context.MODE_PRIVATE).edit();
//		edit.putString(NTConstants.ZIP_VERSION, NTBusinessPackageManager.getInstance().updateInfo.getApp_version());
//		edit.commit();
	}
	
	public void update_no(){
		if(isForcedUpdating){
			pageContext.getApplication().onTerminate();
		}else{
			packageDownloadListener.onExecuted(null, null);
		}
	}
	
	public void updateApk_yes(){
		if(!isApkupdate){
			update_yes();
		}else{
			NTBusinessPackageManager.getInstance().downloadApk(new ResultListener() {
				
				@Override
				public void onProgress(long currentLen, long totalLen, boolean downloaded) {
					LoadingActivity.updateProgress(currentLen, totalLen, false);
				}
				
				@Override
				public void onExecuted(Object result, NTServiceException se) {
					if(se != null){
						NTDialogUtils.showToast(pageContext, se.getMessage(), 2000, 500);
						return;
					}
					LoadingActivity.updateProgress(1, 1, true);
					
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					File file = new File(Environment.getExternalStorageDirectory().toString() + "/icCard/icCard.apk");
					intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
					pageContext.startActivity(intent);
					
//					pageContext.getApplication().onTerminate();
				}
			});
		}
	}
}
