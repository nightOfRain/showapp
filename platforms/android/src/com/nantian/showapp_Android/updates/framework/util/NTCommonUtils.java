package com.nantian.showapp_Android.updates.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lingala.zip4j.core.ZipFile;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.nantian.showapp_Android.updates.framework.NTConstants;
import com.nantian.showapp_Android.updates.framework.NTConstants.Spec;
import com.nantian.showapp_Android.updates.mfp.NTBusinessPackageManager.UnzipListener;

/**
 * $Id: NTCommonUtils.java 4989 2014-12-19 01:52:25Z Genty $
 * 
 * 
 * 
 * @author Genty
 * 
 */
public abstract class NTCommonUtils {

	private static final int BUFFER_SIZE = 1024;
	private static final String TAG = "NTCommonUtils";

	public static String encode(String content) {
		try {
			return URLEncoder.encode(content, Spec.CHARSET);
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return content;
	}

	public static String decode(String content) {
		try {
			return URLDecoder.decode(content, Spec.CHARSET);
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return content;
	}

	/**
	 * This method convets dp unit to equivalent device specific value in pixels.
	 * 
	 * @param dp A value in dp(Device independent pixels) unit. Which we need to convert into pixels
	 * @return A float value to represent Pixels equivalent to dp according to device
	 */
	public static float convertDpToPixel(float dp) {
		float px = dp * (NTContextUtils.getContext().getDensityDpi() / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to device independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @return A float value to represent db equivalent to px value
	 */
	public static float convertPixelsToDp(float px) {
		float dp = px / (NTContextUtils.getContext().getDensityDpi() / 160f);
		return dp;

	}

	/**
	 * 格式化日期
	 * 
	 * @param date 日期
	 * @param pattern 模式 如: yyyy-MM-dd
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
		return formatter.format(date);
	}

	/**
	 * 将字符串转换为日期
	 * 
	 * @param dateString 日期
	 * @param pattern 模式 如: yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateString, String pattern) {
		SimpleDateFormat formatter = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		formatter = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			return formatter.parse(dateString.trim());
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日期的加减操作
	 * 
	 * @param date Date 操作前的日期
	 * @param field int 日期的部分如: 年, 月, 日
	 * @param amount int 增加或减少的值(-表示减)
	 * @return Date
	 */
	public static Date dateAdd(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 给指定的View设置背景
	 * 
	 * @param view 要设置背景的View
	 */
	public static void setBackground(View view) {
		if (view != null && NTContextUtils.getContext().getSkinId() != 0) {
			try {
				view.setBackgroundResource(NTContextUtils.getContext().getSkinId());
			} catch (RuntimeException e) {
				NTLogger.error("CommonUtils", "Not found resource!", e);
			}
		}
	}

	/**
	 * MD5消息摘要算法
	 * 
	 * @param string
	 * @return
	 */
	public static String md5(String string) {

		byte[] hash;

		try {

			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			throw new RuntimeException("Huh, MD5 should be supported?", e);

		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("Huh, UTF-8 should be supported?", e);

		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {

			if ((b & 0xFF) < 0x10)
				hex.append("0");

			hex.append(Integer.toHexString(b & 0xFF));

		}

		return hex.toString();

	}
	
	public static void unZipFileWithProgress(File zipFile, String unzipPath, final UnzipListener listener){
		try{
			ZipFile zFile = new ZipFile(zipFile);
			zFile.setFileNameCharset("utf-8");
	
			if (!zFile.isValidZipFile()) { //
				listener.onUnzipError("ZipFile is suspect resource");
				NTLogger.error(TAG, "ZipFile is suspect resource");
				return;
			}
			File destDir = new File(unzipPath);
			if (destDir.isDirectory() && !destDir.exists()) {
				destDir.mkdir();
			}
			if (zFile.isEncrypted()) {
				zFile.setPassword(NTConstants.ZIP_KEY); // 设置解压密码
			}
	
//			final ProgressMonitor progressMonitor = zFile.getProgressMonitor();
//			Thread thread = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						int precentDone = 0;
//						while (true) {
//							// 每隔50ms,获取一次解压进度
//							Thread.sleep(50);
//							precentDone = progressMonitor.getPercentDone();
//							if (precentDone >= 100) {
//								listener.onExecuted();
//								break;
//							}
//						}
//					} catch (Exception e) {
//					} 
//				}
//			});
//			thread.start();
			zFile.setRunInThread(false); // true 在子线程中进行解压 , false当前线程中解压
			zFile.extractAll(unzipPath); // 将压缩文件解压到unzipPath中...
			listener.onExecuted();
		}catch (Exception e){
			listener.onUnzipError(e.getMessage());
			NTLogger.error(TAG, "Unzip the files failed", e);
		}
	}

	/**
	 * 验证文件的MD5是否是指定的值
	 * 
	 * @param source 源文件
	 * @param mac MD5字符串
	 * @return
	 */
	public static boolean verifyFileMd5(File source, String mac) {
		if (source == null || !source.exists() || !source.isFile() || mac == null || mac.length() == 0) {
			return false;
		}
		DigestInputStream dis = null;
		try {

			dis = new DigestInputStream(new FileInputStream(source), MessageDigest.getInstance("MD5"));

			byte[] buf = new byte[BUFFER_SIZE];
			while (dis.read(buf) > -1) {
			}
			byte[] digest = dis.getMessageDigest().digest();
			StringBuilder sb = new StringBuilder(digest.length * 2);
			for (byte b : digest) {
				if ((b & 0xFF) < 0x10) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(b & 0xFF));
			}
			NTLogger.debug(TAG, "File[" + source.getAbsolutePath() + "] md5 is [" + sb.toString() + "]");
			return mac.equals(sb.toString());
		} catch (IOException e) {
			NTLogger.error(TAG, "Failed to verify md5.", e);
		} catch (NoSuchAlgorithmException e) {
			NTLogger.warn(TAG, "No MD5 algorithm on the device.", e);
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					NTLogger.warn(TAG, "Failed to close dis.");
				}
				dis = null;
			}
		}

		return false;
	}

	/**
	 * 验证文件的MD5是否是指定的值
	 * 
	 * @param source 源文件
	 * @param mac MD5字符串
	 * @return
	 */
	public static boolean verifyFileMd5(InputStream source, String mac) {
		if (source == null || mac == null || mac.length() == 0) {
			return false;
		}
		DigestInputStream dis = null;
		try {

			dis = new DigestInputStream(source, MessageDigest.getInstance("MD5"));

			byte[] buf = new byte[BUFFER_SIZE];
			while (dis.read(buf) > -1) {
			}
			byte[] digest = dis.getMessageDigest().digest();
			StringBuilder sb = new StringBuilder(digest.length * 2);
			for (byte b : digest) {
				if ((b & 0xFF) < 0x10) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(b & 0xFF));
			}
			NTLogger.debug(TAG, "File md5 is [" + sb.toString() + "]");
			return mac.equals(sb.toString());
		} catch (IOException e) {
			NTLogger.error(TAG, "Failed to verify md5.", e);
		} catch (NoSuchAlgorithmException e) {
			NTLogger.warn(TAG, "No MD5 algorithm on the device.", e);
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					NTLogger.warn(TAG, "Failed to close dis.");
				}
				dis = null;
			}
		}

		return false;
	}

	public static String mask(String value) {
		if (value == null) {
			return null;
		}
		if (value.length() < 8) {
			return value;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(value.substring(0, 4));
		sb.append("***");
		sb.append(value.substring(value.length() - 4));

		return sb.toString();
	}

	/**
	 * 格式化数字
	 * 
	 * @param value
	 * @param pattern 格式: 如 "##0.00元"
	 * @return
	 */
	public static String format(double value, String pattern) {
		// 通过JSONObject中的optDouble(String name)方法取值时可能出现此种情况。Huangming 2014-11-17
		if ("NaN".equals(String.valueOf(value))) {
			return "";
		}
		DecimalFormat f = new DecimalFormat(pattern);
		return f.format(value);
	}

	/**
	 * 将手机号码或帐号格式化成“1351***6789”的格式
	 * 
	 * @param phoneNumber 要格式化的电话号码或帐号
	 * @return
	 */
	public static String formatPhoneNumber(String phoneNumber) {
		StringBuilder sb = new StringBuilder();
		if (phoneNumber != null && phoneNumber.length() > 7) {
			sb.append(phoneNumber.substring(0, 4));
			sb.append("***");
			sb.append(phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length()));
		} else {
			sb.append(phoneNumber);
		}
		return sb.toString();
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			// Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2,3,5-9]))\\d{8}$");
			// 只判断是不是以“1”开头号，及位数。
			Pattern p = Pattern.compile("^1\\d{10}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			NTLogger.error(TAG, "验证手机号码错误:" + mobiles, e);
			flag = false;
		}
		return flag;
	}

	/**
	 * 校正手机号码
	 * 
	 * @param phoneNumber 需要校正的手机号码
	 * @return 校正后的手机号码
	 */
	public static String fixPhoneNumber(String phoneNumber) {
		// 去掉开头的”+86“, 去掉所有非数字字符
		return phoneNumber.replace("+86", "").replaceAll("[\\D]", "");

	}
	
	public static String getZipInfo(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				NTConstants.ZIP_INFO, Context.MODE_PRIVATE);
		String res = sharedPreferences.getString(key, "");
		return  res;
	}

}
