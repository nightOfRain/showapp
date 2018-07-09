package com.nantian.showapp_Android;

import java.math.BigDecimal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nantian.config.ConfigurationConstant;
import com.nantian.showapp_Android.R;
import com.nantian.showapp_Android.updates.mfp.NTBusinessController;

public class LoadingActivity extends Activity implements OnClickListener {
	
	private static final String STRATEGY_SERVER = "server";

	private static String pageLoadStrategy = ConfigurationConstant.PAGE_LOAD_STRATEGY;
	
	private static int mProgress = 0;
	private static boolean mLoaded = false;
	private LinearLayout ll;
	private ProgressBar pb_loading;
	private TextView tv_info;
	private LinearLayout ll_dialog;
	private TextView tv_content;
	private TextView tv_yes;
	private TextView tv_no;
	
	private String content;
	// 是否的自动更新 默认为true
	private boolean autoUpdate = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		((NTApplication)getApplication()).addActivity(this.getClass().getSimpleName(), this);
		
		content = getIntent().getStringExtra("content");
		autoUpdate = getIntent().getBooleanExtra("autoUpdate", true);
				
		ll = (LinearLayout) this.findViewById(R.id.ll);
		pb_loading = (ProgressBar) this.findViewById(R.id.pb_loading);
		tv_info = (TextView) this.findViewById(R.id.tv_info);
		
		
		ll_dialog = (LinearLayout) this.findViewById(R.id.ll_dialog);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_yes = (TextView) findViewById(R.id.tv_yes);
		tv_no = (TextView) findViewById(R.id.tv_no);
		
		ll_dialog.setVisibility(View.VISIBLE);
		tv_yes.setOnClickListener(this);
		tv_no.setOnClickListener(this);
		
		if(!TextUtils.isEmpty(content)){
			tv_content.setText(content);
		}
		
		if (autoUpdate && STRATEGY_SERVER.equals(pageLoadStrategy)) {
			
			new Thread(new Runnable() {
				int i = 0;
				@Override
				public void run() {
					while(true){
						if(mProgress != i){
							//说明progress有变化，即更新pb_loading
							i = mProgress;
							LoadingActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ll.setVisibility(View.VISIBLE);
									pb_loading.setProgress(mProgress);
									tv_info.setText("加载中..." + mProgress + "%");
								}
							});
							if(mLoaded){
								mProgress = 0;
								i = 0;
								mLoaded = false;
								break;
							}
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					LoadingActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pb_loading.setProgress(100);
							tv_info.setText("加载完成");
							LoadingActivity.this.finish();
						}
					});
				}
			}).start();

		} else {
			ll.setVisibility(View.GONE);
		}
	}
	
	/**
	 * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
	 * 
	 * @param bytes
	 * @return
	 */
	private String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		if (returnValue > 1)
			return (returnValue + "MB");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		return (returnValue + "KB");
	}

	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		((NTApplication)getApplication()).removeActivity(this.getClass().getSimpleName());
		super.onDestroy();
	}
	
	/**
	 * 
	 * @param currentLen
	 * @param totalLen
	 * @param loaded 加载完成
	 */
	public static void updateProgress(long currentLen, long totalLen, boolean loaded){
		double current = currentLen;
		double total = totalLen;
		mProgress = (int) (current/total * 100);
		mLoaded = loaded;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_yes:
			ll_dialog.setVisibility(View.GONE);
			
			if(autoUpdate){
				NTBusinessController.getInstance().update_yes();
				ll.setVisibility(View.VISIBLE);
			}else{
				NTBusinessController.getInstance().updateApk_yes();
				//手动检查的时候就弹出带进度条的弹窗
				final ProgressDialog dialog = new ProgressDialog(this);
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setTitle("提示");
				dialog.setMax(100);
				dialog.setMessage("正在检查更新，请稍等...");
				dialog.show();
				
				// 开始下载
				new Thread(new Runnable() {
					int i = 0;
					@Override
					public void run() {
						while(true){
							if(mProgress != i){
								//说明progress有变化，即更新pb_loading
								i = mProgress;
								LoadingActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										dialog.setMessage("正在下载..." + mProgress + "%");
										dialog.setProgress(mProgress);
									}
								});
								if(mLoaded){
									//下载完成
									mProgress = 0;
									i = 0;
									mLoaded = false;
									LoadingActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											dialog.dismiss();
											LoadingActivity.this.finish();
										}
									});
									break;
								}
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
			break;
		case R.id.tv_no:
			if(autoUpdate){
				NTBusinessController.getInstance().update_no();
			}
			this.finish();
			break;
		default:
			break;
		}
	}
	
}
