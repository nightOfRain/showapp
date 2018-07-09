package com.nantian.showapp_Android.updates.mfp.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.IceCreamCordovaWebViewClient;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.nantian.config.ConfigurationConstant;
import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.util.NTCommonUtils;
import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;
import com.nantian.showapp_Android.updates.framework.util.NTLogger;

public class NTIceCreamWebViewClient extends IceCreamCordovaWebViewClient {

	private static final String TAG = "NTIceCreamWebViewClient";
	
	private static String zipLoadStrategy = ConfigurationConstant.ZIP_LOAD_STRATEGY;

	private static final int BUFFER_SIZE = 1024;

	private boolean md5Verify = false;

	public NTIceCreamWebViewClient(CordovaInterface cordova, CordovaWebView view) {
		super(cordova, view);
	}

	public NTIceCreamWebViewClient(CordovaInterface cordova) {
		super(cordova);
	}
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		String name = "";
		if(url.contains("/js/lib/")){
			name = url.substring(url.lastIndexOf("/js/lib/"));
//			if(name.contains("/www/")){
				InputStream inputStream = getInputStreamFromAssets(view.getContext(), name);
				if(name.endsWith(".js")){
					return new WebResourceResponse("text/javascripts", "utf-8", inputStream);
				}else if(name.endsWith(".css")){
					return new WebResourceResponse("text/css", "utf-8", inputStream);
				}else{
					return new WebResourceResponse("application/octet-stream", "utf-8", inputStream);
				}
//			}

		}
		name = url.substring(url.lastIndexOf("/"));
		
		if((name.contains("cordova")) && name.endsWith(".js")){
			InputStream inputStream = getInputStreamFromAssets(view.getContext(), name);
			return new WebResourceResponse("text/javascripts", "utf-8", inputStream);
		}
		
		if(NTConstants.ZIPLOADSTRATEGY_UNZIP.equals(zipLoadStrategy)){
			return super.shouldInterceptRequest(view, url);
		}
		
		
		WebResourceResponse response = null;

		File html5Folder = NTContextUtils.getContext().getApplicationContext()
				.getDir(NTConstants.Path.HTML5_FOLDER, Context.MODE_PRIVATE);
		File html5source = new File(html5Folder
				+ "/"
				+ NTConstants.Path.HTML5_TEMPLATE_FOLDER
				+ "/"
				+ NTCommonUtils.getZipInfo(
						NTContextUtils.getContext().getApplicationContext(),
						NTConstants.KEY_BUSINESS_ID) + ".zip");
		File html5publicsource = new File(html5Folder
				+ "/"
				+ NTConstants.Path.HTML5_TEMPLATE_FOLDER
				+ "/"
				+ NTCommonUtils.getZipInfo(
						NTContextUtils.getContext().getApplicationContext(),
						NTConstants.KEY_RESOURCE_ID) + ".zip");
		ZipFile source = null;
		ZipFile basicSource = null;
		try {
			
			source = new ZipFile(html5source);
			basicSource = new ZipFile(html5publicsource);
			// 验证业务包的MD5
			// 验证资源包MD5
			if(!md5Verify){
				if (!NTCommonUtils.verifyFileMd5(html5source, NTCommonUtils.getZipInfo(NTContextUtils.getContext().getApplicationContext(), NTConstants.KEY_BUSINESS_MD5))) {
					NTLogger.error(TAG, "Failed to verify MD5");
					md5Verify = false;
					return null;
				}else{
					md5Verify = true;
				}
				if(html5publicsource.exists() && !NTCommonUtils.verifyFileMd5(html5publicsource, NTCommonUtils.getZipInfo(NTContextUtils.getContext().getApplicationContext(), NTConstants.KEY_RESOURCE_MD5))){
					md5Verify = false;
					return null;
				}else{
					md5Verify = true;
				}
			}
			if (url.endsWith(".js")) {
				response = new WebResourceResponse("text/javascripts", "utf-8", getInputStream(source, name));
			} else if (url.endsWith(".html") || url.endsWith(".htm")) {
				response = new WebResourceResponse("text/html", "utf-8", getInputStream(source, name));
			} else if (url.endsWith(".css")) {
				response = new WebResourceResponse("text/css", "utf-8", getInputStream(source, name));
			} else if (url.endsWith(".png")) {
				response = new WebResourceResponse("image/png", "utf-8", getInputStream(basicSource, name));
			} else if (url.endsWith(".jpg")) {
				response = new WebResourceResponse("image/jpg", "utf-8", getInputStream(basicSource, name));
			}
		} catch (Exception e) {
			NTLogger.error(TAG, "Failed to retrieve " + url, e);
		}
		return response;
	}
	
	public InputStream getInputStream(ZipFile zipFile, String name) {
		ZipInputStream zis = null;
		ByteArrayOutputStream os = null;
		try {
			//设置解压密码
			if (zipFile.isEncrypted()) {

				zipFile.setPassword(NTConstants.ZIP_KEY);
			}
			List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
			for (int i = 0; i < fileHeaderList.size(); i++) {
				if (fileHeaderList.get(i) != null) {
					if (fileHeaderList.get(i).getFileName().endsWith(name)) {
						zis = zipFile.getInputStream(fileHeaderList.get(i));  
						os = new ByteArrayOutputStream();
						byte data[] = new byte[BUFFER_SIZE];
						int len = -1;
						while ((len = zis.read(data, 0, BUFFER_SIZE)) != -1) {
							os.write(data, 0, len);
						}
						os.flush();
						return new ByteArrayInputStream(os.toByteArray());
					}
				}
			}
		} catch (IOException e) {
			NTLogger.error(TAG, "Failed to read " + name, e);
		} catch (ZipException e) {
			NTLogger.error(TAG, "Failed to read " + name, e);
		} finally {
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					//
				}
				zis = null;
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
				os = null;
			}
		}
		return null;
	}

	public InputStream getInputStreamFromAssets(Context context, String name) {
		ByteArrayOutputStream os = null;
		InputStream inputStream = null;
		try{
			inputStream = context.getAssets().open("www" + name);
			if(inputStream == null){
				return null;
			}
			os = new ByteArrayOutputStream();
			byte data[] = new byte[BUFFER_SIZE];
			int len = -1;
			while ((len = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
				os.write(data, 0, len);
			}
			os.flush();
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			NTLogger.error(TAG, "Failed to read " + name, e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
				inputStream = null;
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
				os = null;
			}
		}
		return null;
	}
}
