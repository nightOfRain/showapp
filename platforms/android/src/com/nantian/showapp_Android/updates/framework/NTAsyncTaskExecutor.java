package com.nantian.showapp_Android.updates.framework;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.util.NTNetworkUtils;

/**
 * $Id$
 * 
 * 异步任务执行程序
 * 
 * @author Genty
 * 
 */
public class NTAsyncTaskExecutor {

	private static final long DELAY_MILLISECONDS = 200;

	private ScheduledThreadPoolExecutor executor = null;

	/**
	 * 异步任务结果监听器
	 */
	public static interface ResultListener {

		/**
		 * 执行完成后通知结果或错误
		 * 
		 * @param result 执行结果，当se不为空时该值无效
		 * @param se 执行过程中发生的异常
		 */
		void onExecuted(Object result, NTServiceException se);
		
		/**
		 * 任务执行进度
		 * @param currentLen
		 * @param totalLen
		 * @param downloaded 下载是否完成
		 */
		void onProgress(long currentLen, long totalLen, boolean downloaded);
	}

	protected NTAsyncTaskExecutor() {
		executor = new ScheduledThreadPoolExecutor(1);
	}

	/**
	 * 延时执行任务
	 * 
	 * @param task
	 */
	protected void execute(NTAsyncTask task) {
		executor.schedule(task, DELAY_MILLISECONDS, TimeUnit.MILLISECONDS);
	}

	/**
	 * 撤销当前未执行的任务
	 */
	public void cancel() {
		executor.shutdown();
		NTNetworkUtils.disconnect();
	}
}
