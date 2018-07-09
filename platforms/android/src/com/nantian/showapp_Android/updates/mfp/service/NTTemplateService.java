/*
 * Copyright (C) 2014 Nantian Electronics Information. All Rights Reserved.
 *
 * 本程序中所包含的信息属于机密信息，如无南天的书面许可，任何人都无权复制或利用。
 */
package com.nantian.showapp_Android.updates.mfp.service;

import java.util.List;

import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.mfp.domain.ZipInfo;

public interface NTTemplateService {

	/**
	 * 获取zip版本信息
	 */
	List<ZipInfo> downloadCatalogue(boolean autoUpdate) throws NTServiceException;

	/**
	 * 下载zip包
	 * 
	 * @param id
	 * @param version
	 * @return
	 * @throws NTServiceException
	 */
	ZipInfo downloadTemplate(ZipInfo zipinfo, ResultListener listener) throws NTServiceException;

	/**
	 * 检查apk版本信息
	 * @return
	 * @throws NTServiceException 
	 */
	Object checkApkVersion() throws NTServiceException;

	/**
	 * 下载apk
	 * @param listener
	 * @return
	 * @throws NTServiceException
	 */
	Object downloadApk(ResultListener listener) throws NTServiceException;

}
