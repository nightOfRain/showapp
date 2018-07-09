/*
 * Copyright (C) 2014 Nantian Electronics Information. All Rights Reserved.
 *
 * 本程序中所包含的信息属于机密信息，如无南天的书面许可，任何人都无权复制或利用。
 */
package com.nantian.showapp_Android.updates.mfp.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.nantian.config.ConfigurationConstant;
import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;
import com.nantian.showapp_Android.updates.framework.util.NTLogger;
import com.nantian.showapp_Android.updates.framework.util.NTSyncHttp;
import com.nantian.showapp_Android.updates.mfp.NTBusinessPackageManager;
import com.nantian.showapp_Android.updates.mfp.domain.UpdateInfo;
import com.nantian.showapp_Android.updates.mfp.domain.ZipInfo;
import com.nantian.showapp_Android.updates.mfp.service.NTTemplateService;

/**
 * 
 *
 * @author Genty
 *
 */
public class NTTemplateServiceImpl implements NTTemplateService {

	private static final String TAG = "NTTemplateServiceImpl";

	private static final int BUFFER_SIZE = 1024;
	
	private static final String HTML_UPDATE_URL = ConfigurationConstant.UPDATE_SERVER_URL + "/emmmng/zipfiles";
			
	private static final String APK_UPDATE_URL = ConfigurationConstant.UPDATE_SERVER_URL + "/emmmng/app";

	@Override
	public List<ZipInfo> downloadCatalogue(boolean autoUpdate) throws NTServiceException {
		
		try {
			SharedPreferences preferences = NTContextUtils.getContext().getApplicationContext().getSharedPreferences(NTConstants.ZIP_INFO, Context.MODE_PRIVATE);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			  // 添加要传递的参数
			NameValuePair pair1 = new BasicNameValuePair(NTConstants.PARAMS_TXCODE_KEY, NTConstants.PARAMS_TXCODE_102);
			params.add(pair1);
			NameValuePair pair2 = new BasicNameValuePair("app_id", NTContextUtils.getContext().getApplicationContext().getPackageName());
			params.add(pair2);
			NameValuePair pair3 = new BasicNameValuePair("business_version", preferences.getString(NTConstants.KEY_BUSINESS_VERSION, ""));
			params.add(pair3);
			NameValuePair pair4 = new BasicNameValuePair("resource_version", preferences.getString(NTConstants.KEY_RESOURCE_VERSION, ""));
			params.add(pair4);
			NameValuePair pair5 = new BasicNameValuePair("isAuto", autoUpdate ? "1" : "0");
			params.add(pair5);
			String json = "";
			HttpResponse httpResponse = NTSyncHttp.httpPost(NTContextUtils.getContext().getApplicationContext(),HTML_UPDATE_URL, params);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			     // 取得返回的字符串
				json = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("--->> json " + json);
		    }else{
		    	throw new NTServiceException(NTConstants.Error.NETWORK_ERROR);
		    }

			JSONObject catalogues = new JSONObject(json);
			
			UpdateInfo updateInfo = new UpdateInfo();
			updateInfo.setIsstatic("1".equals(catalogues.getString("isstatic")));// 1 表示静���下载安装
			updateInfo.setForceupdate("1".equals(catalogues.getString("forceupdate"))); // 1 表示强制更新
			updateInfo.setStartpage(catalogues.getString("startpage"));
//			updateInfo.setHasBaseVerson("1".equals(catalogues.getString("hasBaseVerson"))); // 1表示包含基线版本
			updateInfo.setUpdateMsg(catalogues.getString("version_msg"));
			
			NTBusinessPackageManager.getInstance().updateInfo = updateInfo;
			if(!TextUtils.isEmpty(updateInfo.getStartpage())){
				// 获取首页的相对路径。并写入SharedPreferences
				Editor edit = preferences.edit();
				edit.putString(NTConstants.ZIP_FIRST_PAGE_NAME, updateInfo.getStartpage());
				edit.commit();
			}
			
			List<ZipInfo> listTemplate = new ArrayList<ZipInfo>();
			
			//解析里面的zip包信息
			JSONArray array = catalogues.getJSONArray("packages");
			if (array != null && array.length() > 0) {
				int n = array.length();
				for (int a = 0; a < n; a++) {
					JSONObject b = array.getJSONObject(a);
					ZipInfo info = new ZipInfo();
					info.setApp_id(b.getString("app_id"));
					info.setZip_id(b.getString("zip_id"));
					info.setZip_md5(b.getString("zip_md5"));
					info.setZip_size(b.getString("zip_size"));
					info.setZip_type(b.getString("zip_type"));
					info.setZip_version(b.getString("app_version"));
					listTemplate.add(info);
				}
			}

			
			return listTemplate;
			
		} catch (Exception e) {
			NTLogger.error(TAG, "Failed to download catalogue", e);
			throw new NTServiceException(NTConstants.Error.NETWORK_ERROR);
		}
	}

	@Override
	public ZipInfo downloadTemplate(ZipInfo zipinfo, ResultListener listener) throws NTServiceException {
		File html5Folder = NTContextUtils.getContext().getApplicationContext().getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
		File templateFolder = new File(html5Folder, NTConstants.Path.HTML5_TEMPLATE_FOLDER);
		if (!templateFolder.exists()) {
			if (!templateFolder.mkdirs()) {
				NTLogger.error(TAG, "Failed to create html5 template folder.");
				throw new NTServiceException(NTConstants.Error.SYSTEM_ERROR);
			}
		}
		NTLogger.debug(TAG, "Html5 folder[" + html5Folder.getAbsolutePath() + "]");
		
		DigestInputStream dis = null;
		OutputStream os = null;
		try {
			// 这里应该是从网站下载
			String params = "?" + NTConstants.PARAMS_TXCODE_KEY + "=" +  NTConstants.PARAMS_TXCODE_003
					+ "&app_id=" + NTContextUtils.getContext().getApplicationContext().getPackageName()
					+ "&app_version=" + zipinfo.getZip_version()
					+ "&zip_type=" + zipinfo.getZip_type();
			
			HttpResponse httpResponse = NTSyncHttp.httpGet(NTContextUtils.getContext().getApplicationContext(), HTML_UPDATE_URL + params);
			HttpEntity respEnt = httpResponse.getEntity();
			long lenght = respEnt.getContentLength();
			Boolean isStream = respEnt.isStreaming();
			InputStream fileInStream = null;
			if (isStream) {
				fileInStream = respEnt.getContent();
		    }else{
		    	throw new NTServiceException("Failed to load template");
		    }
			
			dis = new DigestInputStream(fileInStream, MessageDigest.getInstance("MD5"));
			byte[] buf = new byte[BUFFER_SIZE];
			int len = -1;

			File target = new File(templateFolder, zipinfo.getZip_id() + ".zip");

			os = new BufferedOutputStream(new FileOutputStream(target), BUFFER_SIZE);
			long currentLen = 0;
			while ((len = dis.read(buf)) > -1) {
				os.write(buf, 0, len);
				currentLen += len;
				listener.onProgress(currentLen, lenght, false);
			}
			os.flush();
			return zipinfo;
		} catch (IOException e) {
			NTLogger.error(TAG, "Failed to load template", e);
			throw new NTServiceException(NTConstants.Error.NETWORK_ERROR);
		} catch (NoSuchAlgorithmException e) {
			NTLogger.warn(TAG, "No MD5 algorithm on the device.");
			throw new NTServiceException(NTConstants.Error.SYSTEM_ERROR);
		} catch (Exception e) {
			NTLogger.warn(TAG, "No MD5 algorithm on the device.");
			throw new NTServiceException(NTConstants.Error.SYSTEM_ERROR);
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					NTLogger.warn(TAG, "Failed to close dis.");
				}
				dis = null;
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					NTLogger.warn(TAG, "Failed to close os.");
				}
				os = null;
			}
		}
	}

	@Override
	public Object checkApkVersion()throws NTServiceException {
		
		try{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			  // 添加要传递的参数
			NameValuePair pair1 = new BasicNameValuePair(NTConstants.PARAMS_TXCODE_KEY, NTConstants.PARAMS_TXCODE_101);
			params.add(pair1);
			NameValuePair pair2 = new BasicNameValuePair("appid", NTContextUtils.getContext().getApplicationContext().getPackageName());
			params.add(pair2);
			
			PackageManager manager = NTContextUtils.getContext().getApplicationContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(NTContextUtils.getContext().getApplicationContext().getPackageName(), 0);
			String appVersion = info.versionName;
			NameValuePair pair3 = new BasicNameValuePair("appversion", appVersion);
			params.add(pair3);
			String json = "";
			HttpResponse httpResponse = NTSyncHttp.httpPost(NTContextUtils.getContext().getApplicationContext(), APK_UPDATE_URL, params);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			     // 取得返回的字符串
				json = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("--->> json " + json);
		    }else{
		    	throw new NTServiceException(NTConstants.Error.NETWORK_ERROR);
		    }
			JSONObject obj = new JSONObject(json);
			boolean hasUpdate = "1".equals(obj.getString("hasUpdate"));
			String updateMsg = obj.getString("msg");
			UpdateInfo update = new UpdateInfo();
			update.setHasUpdate(hasUpdate);
			update.setUpdateMsg(updateMsg);
			return update;
		}catch(Exception e){
			e.printStackTrace();
			throw new NTServiceException(NTConstants.Error.NETWORK_ERROR);
		}
	}

	@Override
	public Object downloadApk(ResultListener listener) throws NTServiceException {
		DigestInputStream dis = null;
		OutputStream os = null;
		try {
			// 这里应该是从网站下载
			String params = "?appid=" + NTContextUtils.getContext().getApplicationContext().getPackageName();
			
			HttpResponse httpResponse = NTSyncHttp.httpGet(NTContextUtils.getContext().getApplicationContext(), APK_UPDATE_URL + "/download" + params);
			HttpEntity respEnt = httpResponse.getEntity();
			long lenght = respEnt.getContentLength();
			Boolean isStream = respEnt.isStreaming();
			InputStream fileInStream = null;
			if (isStream) {
				fileInStream = respEnt.getContent();
		    }else{
		    	throw new NTServiceException("Failed to load template");
		    }
			
			dis = new DigestInputStream(fileInStream, MessageDigest.getInstance("MD5"));
			byte[] buf = new byte[BUFFER_SIZE];
			int len = -1;
			
			String sdPath = Environment.getExternalStorageDirectory().toString();
			String state = Environment.getExternalStorageState();
			if (!Environment.MEDIA_MOUNTED.equals(state)) { // 检测sd是否可用
				Log.i("TestFile", "SD card is not avaiable/writeable right now.");
				throw new NTServiceException("SD卡不可用");
			}
			
			String fileDir = sdPath + "/icCard/";
			File file = new File(fileDir);
			if(!file.exists()){
				file.mkdirs();// 创建文件夹
			}
			
			File target = new File(file + "/icCard.apk");

			os = new BufferedOutputStream(new FileOutputStream(target), BUFFER_SIZE);
			long currentLen = 0;
			while ((len = dis.read(buf)) > -1) {
				os.write(buf, 0, len);
				currentLen += len;
				listener.onProgress(currentLen, lenght, false);
			}
			os.flush();
			return null;
		} catch (IOException e) {
			NTLogger.error(TAG, "Failed to load template", e);
			throw new NTServiceException(NTConstants.Error.NETWORK_ERROR);
		} catch (NoSuchAlgorithmException e) {
			NTLogger.warn(TAG, "No MD5 algorithm on the device.");
			throw new NTServiceException(NTConstants.Error.SYSTEM_ERROR);
		} catch (Exception e) {
			NTLogger.warn(TAG, "No MD5 algorithm on the device.");
			throw new NTServiceException(NTConstants.Error.SYSTEM_ERROR);
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					NTLogger.warn(TAG, "Failed to close dis.");
				}
				dis = null;
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					NTLogger.warn(TAG, "Failed to close os.");
				}
				os = null;
			}
		}
	}
}
