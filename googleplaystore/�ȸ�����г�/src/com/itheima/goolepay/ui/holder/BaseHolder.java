package com.itheima.goolepay.ui.holder;

import android.view.View;

public abstract class BaseHolder<T> {

	private View mRootView;//list_item的根布局
	private T data;
	public BaseHolder(){
		//1.加载布局文件 2.初始化控件 findViewById
		mRootView = initView();
		// 3.打一个标记tag
		mRootView.setTag(this);
	}
	//返回当前item根布局
	public View getRootView(){
		return mRootView;
	}
	
	// 设置当前item的数据
	public void setData(T data){
		this.data = data;
		refreshView(data);
	}
	
	// 获取当前item的数据
	public T getData(){
		return data;
	}
	//1.加载布局文件
	//2.初始化控件 findViewById
	public abstract View initView();
	//4.根据数据来刷新界面
	public abstract void refreshView(T data);
}
