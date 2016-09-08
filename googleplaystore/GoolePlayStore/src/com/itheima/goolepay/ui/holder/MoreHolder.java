package com.itheima.goolepay.ui.holder;

import com.itheima.goolepay.R;
import com.itheima.goolepay.utils.UIUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreHolder extends BaseHolder<Integer> {

	//加载更多的几种状态
	//1.加载更多
	//2.加载更多失败
	//3.没有更多数据
	public static final int STATE_MORE_MORE = 1;
	public static final int STATE_MORE_ERROR = 2;
	public static final int STATE_MORE_NONE = 3;
	private TextView tvLoadError;
	private LinearLayout llLoadMore;
	
	public MoreHolder(boolean hasMore){
	//若有更多数据，状态为STATE_MORE_MORE,此状态会传递给父类的data,父类同时刷新数据
		if(hasMore){
			setData(STATE_MORE_MORE);
		}else {
			setData(STATE_MORE_NONE);
		}
	}
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_more);
		tvLoadError = (TextView) view.findViewById(R.id.tv_load_error);
		llLoadMore = (LinearLayout) view.findViewById(R.id.ll_load_more);
		return view;
	}

	//此方法在setData()方法调用后立即调用
	@Override
	public void refreshView(Integer data) {
		
		switch (data) {
		case STATE_MORE_MORE:
			tvLoadError.setVisibility(View.GONE);
			llLoadMore.setVisibility(View.VISIBLE);
			//显示更多数据
			break;
			
		case STATE_MORE_NONE:
			//隐藏加载更多
			tvLoadError.setVisibility(View.GONE);
			llLoadMore.setVisibility(View.GONE);
			break;
			
		case STATE_MORE_ERROR:
			//显示加载失败
			tvLoadError.setVisibility(View.VISIBLE);
			llLoadMore.setVisibility(View.GONE);
			break;
		}
		
	}

}
