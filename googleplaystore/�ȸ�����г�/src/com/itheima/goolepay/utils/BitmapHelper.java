package com.itheima.goolepay.utils;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {

	//单列，懒汉模式
	private static  BitmapUtils mBitmapUtils = null;
	public static BitmapUtils getBitmapUtils(){
		
		if(mBitmapUtils == null){
			synchronized (BitmapHelper.class) {
				if(mBitmapUtils == null){
					mBitmapUtils = new BitmapUtils(UIUtils.getContext());
				}
			}
		}
		
		return mBitmapUtils;
	}
	
	
}
