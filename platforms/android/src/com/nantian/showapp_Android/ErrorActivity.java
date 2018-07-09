package com.nantian.showapp_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.nantian.showapp_Android.R;

public class ErrorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		
		((NTApplication)getApplication()).addActivity(this.getClass().getSimpleName(), this);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			this.getApplication().onTerminate();
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	protected void onDestroy() {
		((NTApplication)getApplication()).removeActivity(this.getClass().getSimpleName());
		super.onDestroy();
	}
}
