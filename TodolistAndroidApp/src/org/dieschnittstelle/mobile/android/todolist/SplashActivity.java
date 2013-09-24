package org.dieschnittstelle.mobile.android.todolist;

import org.dieschnittstelle.mobile.android.todolist.handler.ColorHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private static final long SPLASH_DISPLAY_LENGTH = 4000;

	private ColorHandler colorHandler = new ColorHandler();
	
	TextView appTitle1;
	TextView appTitle2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		
		appTitle1 = (TextView) findViewById(R.id.app_title1);
		appTitle2 = (TextView) findViewById(R.id.app_title2);
		colorHandler
				.setActivityBackground(this, colorHandler.getMyColorBlack());
		colorHandler.setTextColor(appTitle1, colorHandler.getMyColorSnow());
		colorHandler.setTextColor(appTitle2, colorHandler.getMyColorRoyalblue());
	}

	@Override
	protected void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				SplashActivity.this.finish();

				Intent mainIntent = new Intent(SplashActivity.this,
						LoginActivity.class);
				SplashActivity.this.startActivity(mainIntent);
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

}
