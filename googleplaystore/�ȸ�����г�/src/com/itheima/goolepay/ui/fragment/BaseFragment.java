package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.itheima.goolepay.ui.view.LoadingPage;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.utils.UIUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	private LoadingPage mLoadingPage;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mLoadingPage = new LoadingPage(UIUtils.getContext()){

			@Override
			public View onCreateSuccessView() {
				
				//注意：此处一定要调用BaseFragment的onCreateSuccessView,否则栈溢出
				return BaseFragment.this.onCreateSuccessView();
			}

			@Override
			public ResultSate onLoad() {
				
				return BaseFragment.this.onLoad();
			};
		
		};
		return mLoadingPage;
	}

	//加载成功的布局，必须要由子类来实现
	public abstract View onCreateSuccessView();
	//加载网络数据，必须要由子类来实现
	public abstract ResultSate onLoad();

	public void loadData() {
		
		if(mLoadingPage != null){
			mLoadingPage.loadData();
		}
	}
	
	//对网络返回数据合法性进行校验
	public ResultSate check(Object obj){
		
		if(obj != null){
			
			if(obj instanceof ArrayList){  //判断是否是集合
				
				ArrayList list = (ArrayList) obj;
				if(list.isEmpty()){
					return ResultSate.STATE_EMPTY;
				}else {
					return ResultSate.STATE_SUCCESS;
				}
			}
		}
		
		return ResultSate.STATE_ERROR;
	}
}
