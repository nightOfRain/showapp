package com.nantian.showapp_Android.updates.framework.util;

import android.util.Log;

/**
 * $Id: NTLogger.java 2472 2014-04-15 01:35:40Z Genty $
 * 
 * 日志输出, 代理android.util.Log类
 * 
 * @author Genty
 * 
 */
public abstract class NTLogger {

	/**
	 * 日志级别定义
	 */
	private static enum Level {

		/** 详尽 */
		VERBOSE(2),

		/** 调试 */
		DEBUG(3),

		/** 消息 */
		INFO(4),

		/** 警告 */
		WARN(5),

		/** 错误 */
		ERROR(6),

		/** 断言 */
		ASSERT(7);

		private final int level;

		private Level(int level) {
			this.level = level;
		}
	}

	/** 设置日志输出级别, TODO 生产时可以将级别设置为 Level.ERROR */
	private static final Level LEVEL = Level.DEBUG;

	/** 日志标签 */
	private static final String LOG_TAG = "i-Share";

	/** 日志提示 */
	private static final String LOG_TIP = "Log failed";

	private static boolean isDebug = true;

	/**
	 * debug
	 * 
	 * @param tag
	 * @param message
	 */
	public static void debug(String tag, String message) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.DEBUG.level) {
			try {
				Log.d(tag, message);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * debug
	 * 
	 * @param tag
	 * @param message
	 * @param tr
	 */
	public static void debug(String tag, String message, Throwable tr) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.DEBUG.level) {
			try {
				Log.d(tag, message, tr);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * error
	 * 
	 * @param tag
	 * @param message
	 */
	public static void error(String tag, String message) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.ERROR.level) {
			try {
				Log.e(tag, message);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * error
	 * 
	 * @param tag
	 * @param message
	 * @param tr
	 */
	public static void error(String tag, String message, Throwable tr) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.ERROR.level) {
			try {
				Log.e(tag, message, tr);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * info
	 * 
	 * @param tag
	 * @param message
	 */
	public static void info(String tag, String message) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.DEBUG.level) {
			try {
				Log.i(tag, message);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * warn
	 * 
	 * @param tag
	 * @param message
	 * @param tr
	 */
	public static void warn(String tag, String message, Throwable tr) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.INFO.level) {
			try {
				Log.w(tag, message, tr);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * info
	 * 
	 * @param tag
	 * @param message
	 */
	public static void warn(String tag, String message) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.DEBUG.level) {
			try {
				Log.i(tag, message);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * info
	 * 
	 * @param tag
	 * @param message
	 * @param tr
	 */
	public static void info(String tag, String message, Throwable tr) {
		if (!isDebug) {
			return;
		}
		if (LEVEL.level <= Level.INFO.level) {
			try {
				Log.i(tag, message, tr);
			} catch (Throwable e) {
				logError(e);
			}
		}
	}

	/**
	 * 在写日志出错的时候记录下错误信息
	 * 
	 * @param e
	 */
	private static void logError(Throwable e) {
		if (LEVEL.level <= Level.ERROR.level) {
			Log.e(LOG_TAG, LOG_TIP, e);
		}
	}
}
