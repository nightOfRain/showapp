/*
 * Copyright (C) 2014 Nantian Electronics Information. All Rights Reserved.
 *
 * 本程序中所包含的信息属于机密信息，如无南天的书面许可，任何人都无权复制或利用。
 */
package com.nantian.showapp_Android.updates.mfp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.nantian.config.ConfigurationConstant;
import com.nantian.showapp_Android.updates.framework.NTAsyncTask;
import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor;
import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.util.NTCommonUtils;
import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;
import com.nantian.showapp_Android.updates.framework.util.NTLogger;
import com.nantian.showapp_Android.updates.mfp.domain.UpdateInfo;
import com.nantian.showapp_Android.updates.mfp.domain.ZipInfo;
import com.nantian.showapp_Android.updates.mfp.service.NTTemplateService;
import com.nantian.showapp_Android.updates.mfp.service.impl.NTTemplateServiceImpl;

/**
 * $Id$
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTBusinessPackageManager extends NTAsyncTaskExecutor {

	private static final String TAG = "NTBusinessPackageManager";

	private final NTTemplateService templateService;

	private static NTBusinessPackageManager instance = null;
	
	private static String zipLoadStrategy = ConfigurationConstant.ZIP_LOAD_STRATEGY;
	
	/**
	 * 保存当前zip版本信息
	 */
	public UpdateInfo updateInfo;
	
	private Map<String,ZipInfo> listDownloadInfos = new ConcurrentHashMap<String, ZipInfo>();
	
	/** 临时保存需要下载的zip包的id */
	private List<String> tempDownloadInfos = new ArrayList<String>();
	/** 临时保存需要解压的zip包的id */
	private List<ZipInfo> tempListUnzips = new ArrayList<ZipInfo>();

	/** 目录树状态 */
	private volatile int catalogueState = -1;


	/** 通讯框通讯结果监听器 */
	private ResultListener dialogResultListener;

	private class CatalogueDownloadListener implements ResultListener {

		@Override
		public void onExecuted(Object result, NTServiceException se) {

			if (se != null || result == null || !(result instanceof List<?>)) {
				NTLogger.error(TAG, "Failed to download template catalogue.");
				instance = null;
				if (dialogResultListener != null) {
					dialogResultListener.onExecuted(result, se);
				}
				return;
			}
			catalogueState = 1;
			
			@SuppressWarnings("unchecked")
			List<ZipInfo> _listDownloadInfos = (List<ZipInfo>)result;
			if (_listDownloadInfos != null && _listDownloadInfos.size() > 0) {
				for (ZipInfo downloadInfo : _listDownloadInfos) {
					listDownloadInfos.put(downloadInfo.getZip_id(), downloadInfo);
					if(!tempDownloadInfos.contains(downloadInfo.getZip_id())){
						tempDownloadInfos.add(downloadInfo.getZip_id());
					}
					tempListUnzips.add(downloadInfo);
				}
//				//根据版本排序
//				Collections.sort(tempListUnzips, new NTZipListComparator());
//				//保存最新版本
//				String zipVersion = tempListUnzips.get(tempListUnzips.size() - 1).getZip_version();
//				updateInfo.setApp_version(zipVersion);
			}
			if (dialogResultListener != null) {
				dialogResultListener.onExecuted(result, se);
			}
		}

		@Override
		public void onProgress(long currentLen, long totalLen, boolean loaded) {
			
		}
	}


	private class TemplateDownloadListener implements ResultListener {

		private ResultListener listener = null;

		private TemplateDownloadListener(ResultListener listener) {
			this.listener = listener;
		}

		@Override
		public void onExecuted(Object result, NTServiceException se) {
			if (se != null) {
				notifyResult((ZipInfo)result, se);
				return;
			}
			ZipInfo template = (ZipInfo) result;
			if (template == null) {
				NTLogger.warn(TAG, "Downloaded template is null.");
				notifyResult(null, new NTServiceException("下载模板失败"));
				return;
			}
			// 下载完一个就保存一个
			saveInfoToSharedPreferences(template);
			
			//当前zip下载完成后，及时删除
			tempDownloadInfos.remove(template.getZip_id());
			
			NTLogger.info(TAG, "Download zip finish. Zip id is " + template.getZip_id() + ". Zip version is " + template.getZip_version());
			notifyResult(template, null);
		}

		private void notifyResult(ZipInfo result, NTServiceException se) {
			if(se != null){
				if (listener != null) {
					listener.onExecuted(result, se);
					return;
				}
			}
			
			//判断是否还有zip包需要下载
			if(tempDownloadInfos != null && tempDownloadInfos.size() > 0){
				loadDownInfo(getZipDownloadInfo1(tempDownloadInfos.get(0)), listener);
				return;
			}
			//所有的zip下载完成
			if (listener != null) {
				listener.onProgress(1, 1, true);
				listDownloadInfos.clear();
				if(NTConstants.ZIPLOADSTRATEGY_UNZIP.equals(zipLoadStrategy)){
					// 解压zip
					// 判断是否包含基线版本，如果包含就删除之前解压的文件
					if(listDownloadInfos.size() >= 2){
						File html5Folder = NTContextUtils.getContext().getApplicationContext().getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
						final String target = html5Folder.toString() + "/" + NTConstants.Path.HTML5_UNZIP_FOLDER;
						File dirFile = new File(target);
						if(dirFile.isDirectory() && dirFile.exists()){
							delete(dirFile);
						}
					}
					unzip(listener);
				}else{
					listener.onExecuted(result, se);
					return;
				}
			}
		}

		@Override
		public void onProgress(long currentLen, long totalLen, boolean loaded) {
			
		}

	}

	private NTBusinessPackageManager() {
		templateService = new NTTemplateServiceImpl();
	}

	public static synchronized NTBusinessPackageManager getInstance() {
		if (instance == null) {
			instance = new NTBusinessPackageManager();
		}
		return instance;
	}

	public void loadDownInfo(ZipInfo zipInfo, ResultListener listener){
		String id = zipInfo.getZip_id();
		String version = zipInfo.getZip_version();
		if (id == null || id.length() == 0) {
			if (listener != null) {
				listener.onExecuted(null, new NTServiceException("系统错误，请稍后再试"));
			}
			return;
		}
		if (version == null || version.length() == 0) {
			if (listener != null) {
				listener.onExecuted(null, new NTServiceException("系统错误，请稍后再试"));
			}
			return;
		}
		
		download(getZipDownloadInfo(id), listener);
	}
	

	/**
	 * 下载zip包
	 * @param id
	 * @param version
	 * @param listener
	 */
	private void download(final ZipInfo info, final ResultListener listener) {
		execute(new NTAsyncTask(new TemplateDownloadListener(listener)) {

			@Override
			protected Object doInBackground() throws NTServiceException {
				return templateService.downloadTemplate(info, listener);
			}
		});
	}
	
	
	public void downloadApk(final ResultListener listener){
		execute(new NTAsyncTask(listener) {
			
			@Override
			protected Object doInBackground() throws NTServiceException {
				return templateService.downloadApk(listener);
			}
		});
	}
	/**
	 * 删除上次解压后成的文件
	 * @param file
	 */
	private void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	 * 解压模板
	 * 
	 * @param template
	 * @throws NTServiceException
	 */
	private void unzip(final ResultListener listener){
		
		final ZipInfo template = tempListUnzips.get(0);
		
		final File html5Folder = NTContextUtils.getContext().getApplicationContext().getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
		File source = new File(html5Folder + "/" + NTConstants.Path.HTML5_TEMPLATE_FOLDER + "/" + template.getZip_id() + ".zip");
		
		if(!NTCommonUtils.verifyFileMd5(source, template.getZip_md5())){
			NTLogger.error(TAG, "Failed to verify MD5");
			listener.onExecuted(template, new NTServiceException("校验MD5失败"));
			return;
		}
		
		final String target = html5Folder.toString() + "/" + NTConstants.Path.HTML5_UNZIP_FOLDER;
		NTLogger.debug(TAG, "Html5 work directory[" + target + "]");
		
		try {
			NTCommonUtils.unZipFileWithProgress(source, target, new UnzipListener() {
				
				@Override
				public void onUnzipError(String msg) {
					listener.onExecuted(template, new NTServiceException("解压失败"));
				}
				
				@Override
				public void onExecuted() {
					tempListUnzips.remove(0);
					
					// 判断zip是否解压完毕
					if(tempListUnzips != null && tempListUnzips.size() > 0){
						try {
							unzip(listener);
						} catch (Exception e) {
							NTLogger.error(TAG, "Failed to unzip template", e);
							listener.onExecuted(template, new NTServiceException(e.getMessage()));
						}
						return;
					}
					
					listener.onExecuted(null, null);
					
				}
			});
		} catch (Exception e) {
			NTLogger.error(TAG, "Failed to unzip template", e);
			listener.onExecuted(template, new NTServiceException("解压失败"));
			return;
		}
	}
	
	public interface UnzipListener{
		void onExecuted();
		void onUnzipError(String msg);
	}

	public void downloadCatalogue(ResultListener dialogResultListener, final boolean autoUpdate) {
		catalogueState = 0;
		this.dialogResultListener = dialogResultListener;
		execute(new NTAsyncTask(new CatalogueDownloadListener()) {

			@Override
			protected Object doInBackground() throws NTServiceException {
				return templateService.downloadCatalogue(autoUpdate);
			}
		});
	}
	
	public void loadApkVersionInfo(ResultListener dialogResultListener){
		execute(new NTAsyncTask(dialogResultListener) {

			@Override
			protected Object doInBackground() throws NTServiceException {
				return templateService.checkApkVersion();
			}
		});
	}

	public ZipInfo getZipDownloadInfo(String id) {
		if (catalogueState != 1) {
			return null;
		}
		if (id == null || id.trim().length() == 0) {
			return null;
		}
		ZipInfo p = listDownloadInfos.get(id.trim());
		return p;
	}
	
	public Map<String, ZipInfo> getNTZipDownloadInfo(){
		return listDownloadInfos;
	}
	
	public ZipInfo getZipDownloadInfo1(String id) {
		if (catalogueState != 1) {
			return null;
		}
		if (id == null || id.trim().length() == 0) {
			return null;
		}
		ZipInfo p = listDownloadInfos.get(id.trim());
		return p;
	}

	/**
	 * <p>将目录树id和版本信息写入文件中保存</P>
	 * @param downloadInfo
	 */
	private void saveInfoToSharedPreferences(ZipInfo downloadInfo){
		File html5Folder = NTContextUtils.getContext().getApplicationContext().getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
		
		SharedPreferences sharedPreferences = NTContextUtils.getContext().getApplicationContext().getSharedPreferences(NTConstants.ZIP_INFO, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		
		if(NTConstants.ZIP_TYPE_BUSINESS.equals(downloadInfo.getZip_type())){
			String id =  sharedPreferences.getString(NTConstants.KEY_BUSINESS_ID, "");
			if(!TextUtils.isEmpty(id)){
				// 保存之前需要删除掉之前版本的zip包
				String target = html5Folder.toString() + "/" + NTConstants.Path.HTML5_TEMPLATE_FOLDER + "/" + id + ".zip";
				File file = new File(target);
				if(file.isFile() && file.exists()){
					delete(file);
				}
			}
			editor.putString(NTConstants.KEY_BUSINESS_ID, downloadInfo.getZip_id());
			editor.putString(NTConstants.KEY_BUSINESS_MD5, downloadInfo.getZip_md5());
			editor.putString(NTConstants.KEY_BUSINESS_VERSION, downloadInfo.getZip_version());
		}else if(NTConstants.ZIP_TYPE_RESOURCE.equals(downloadInfo.getZip_type())){
			String id =  sharedPreferences.getString(NTConstants.KEY_RESOURCE_ID, "");
			if(!TextUtils.isEmpty(id)){
				// 保存之前需要删除掉之前版本的zip包
				String target = html5Folder.toString() + "/" + NTConstants.Path.HTML5_TEMPLATE_FOLDER + "/" + id + ".zip";
				File file = new File(target);
				if(file.isFile() && file.exists()){
					delete(file);
				}
			}
			editor.putString(NTConstants.KEY_RESOURCE_ID, downloadInfo.getZip_id());
			editor.putString(NTConstants.KEY_RESOURCE_MD5, downloadInfo.getZip_md5());
			editor.putString(NTConstants.KEY_RESOURCE_VERSION, downloadInfo.getZip_version());
		}
		editor.commit();
	}

	/**
	 * 程序退出后，清除缓存(慎用)
	 */
	public void clear(){
		if(listDownloadInfos != null) listDownloadInfos.clear();
		if(tempListUnzips != null) tempListUnzips.clear();
		if(tempDownloadInfos != null) tempDownloadInfos.clear();
		if(updateInfo != null) updateInfo = new UpdateInfo();
	}
}
