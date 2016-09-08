package com.itheima.goolepay.global;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Handler;

public class GooglePlayApplication extends Application {

	private static Context context;
	private static Handler handler;
	private static int mainThreadId;

	Service service;
	Activity activity;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();  //获取当前主线程ID
	}

	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return handler;
	}

	public static int getMainThreadId() {
		return mainThreadId;
	}
	
	
}
