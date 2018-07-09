package com.nantian.showapp_Android.updates.framework;

import com.nantian.showapp_Android.updates.framework.NTAsyncTaskExecutor.ResultListener;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.util.NTLogger;

/**
 * $Id$
 * 
 * 异步任务，每个任务只能被执行1次
 * 
 * @author Genty
 * 
 */
public abstract class NTAsyncTask implements Runnable {

	private static final String TAG = "AsyncTask";

	private final ResultListener listener;

	public NTAsyncTask(ResultListener listener) {
		this.listener = listener;
	}

	/**
	 * 需执行的异步任务内容
	 * 
	 * @return 异步任务结果
	 * @throws NTServiceException 执行任务时发生的异常
	 */
	protected abstract Object doInBackground() throws NTServiceException;

	/**
	 * 通知监听器执行结果
	 * 
	 * @param result
	 * @param se
	 */
	private void notifyResult(final Object result, final NTServiceException se) {
		if (listener != null) {
			try {
				listener.onExecuted(result, se);
			} catch (Throwable e) {
				NTLogger.error(TAG, "Failed to notify result.");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		try {
			NTLogger.debug(TAG, "doInBackground...");
			Object result = doInBackground();
			NTLogger.debug(TAG, "notifyResult...");
			notifyResult(result, null);
		} catch (NTServiceException se) {
			notifyResult(null, se);
		} catch (Throwable e) {
			notifyResult(null, new NTServiceException(e));
		}
	}

}
