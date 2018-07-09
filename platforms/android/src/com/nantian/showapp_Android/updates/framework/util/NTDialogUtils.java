package com.nantian.showapp_Android.updates.framework.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * $Id: NTDialogUtils.java 4989 2014-12-19 01:52:25Z Genty $
 * 
 * 对话框工具，该工具只能在context初始化后使用
 * 
 * @author Genty
 * 
 */
public abstract class NTDialogUtils {

	private static final String TAG = "NTDialogUtils";

	/** 当前对话框 */
	private static volatile Dialog dialog = null;

	/** 最大宽度 */
	// private static final int MAX_WIDTH = 588;

	// galaxy s4 屏幕尺寸:1920*1080 PPI:441

	/**
	 * 显示成功对话框
	 * 
	 * @param content 提示内容
	 */
	public static void showSuccess(String content) {
		showDialog(content, NTResourceUtils.getDrawableId("nt_success"), null);

	}

	/**
	 * 显示对话框
	 * 
	 * @param content 提示内容
	 * @param iconId 提示图标
	 */
	private static void showDialog(String content, int iconId, View.OnClickListener listener) {
		try {
			dismiss();

			Activity activity = NTContextUtils.getContext().getCurrentActivity();

			if (activity == null || activity.isFinishing()) {
				return;
			}

			// 创建弹出框
			dialog = new AlertDialog.Builder(activity).create();
			dialog.show();
			dialog.setCancelable(false);

			int width = NTContextUtils.getScreenSize().x;
			// if (width > MAX_WIDTH) {
			// width = MAX_WIDTH;
			// }
			LayoutInflater factory = LayoutInflater.from(activity);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = width;
			dialog.getWindow().setAttributes(lp);
			View view = factory.inflate(NTResourceUtils.getLayoutId("nt_alert_dialog"), null);
			TextView textView = (TextView) view.findViewById(NTResourceUtils.getId("lbl_tip"));
			textView.setText(content);
			ImageView tipIcon = (ImageView) view.findViewById(NTResourceUtils.getId("img_tip_icon"));
			tipIcon.setImageResource(iconId);
			Button confirmButton = (Button) view.findViewById(NTResourceUtils.getId("btn_confirm"));
			if (null != listener) {
				confirmButton.setOnClickListener(listener);
			} else {
				confirmButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
			}
			dialog.getWindow().setContentView(view);

		} catch (Throwable e) {
			NTLogger.error(TAG, "Failed to show dialog", e);
		}
	}

	/**
	 * 显示错误提示对话框
	 * 
	 * @param content 错误提示内容
	 */
	public static void showError(String content) {
		showDialog(content, NTResourceUtils.getDrawableId("nt_warn"), null);
	}

	/**
	 * 显示带监听器的错误提示对话���
	 * 
	 * @param content 错误提示内容
	 */
	public static void showError(String content, View.OnClickListener listener) {
		showDialog(content, NTResourceUtils.getDrawableId("nt_warn"), listener);
	}

	/**
	 * 方法描述: 创建进度对话框
	 * 
	 * @param content
	 */

	public static void showProgress(String content) {

		try {

			dismiss();

			Activity activity = NTContextUtils.getContext().getCurrentActivity();
			NTLogger.debug(TAG, "activity[" + activity + "]");

			dialog = new AlertDialog.Builder(activity).create();
			dialog.show();
			dialog.setCancelable(false);

			int width = NTContextUtils.getScreenSize().x; // if (width > MAX_WIDTH) { // width = MAX_WIDTH; // }
			NTLogger.debug(TAG, "window width[" + width + "]");
			LayoutInflater factory = LayoutInflater.from(activity);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = width;
			dialog.getWindow().setAttributes(lp);

			View view = factory.inflate(NTResourceUtils.getLayoutId("nt_pop_dialog"), null);
			TextView textView = (TextView) view.findViewById(NTResourceUtils.getId("tip_label"));
			textView.setText(content);
			dialog.getWindow().setContentView(view);

		} catch (Throwable e) {
			NTLogger.error(TAG, "Failed to show dialog", e);
		}
	}

	/**
	 * 显示通讯对话框
	 */
	public static void showProgress() {
		showProgress(null);
	}

	/**
	 * 关闭当前对话框
	 */
	public static void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public static void showToast(Context context, CharSequence text, int duration, int y) {
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.BOTTOM, 0, y);
		toast.show();
	}
}
