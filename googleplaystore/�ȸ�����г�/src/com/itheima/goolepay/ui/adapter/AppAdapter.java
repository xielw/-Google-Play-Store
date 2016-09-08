package com.itheima.goolepay.ui.adapter;

import java.util.ArrayList;

import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.protocol.AppProtocol;
import com.itheima.goolepay.ui.holder.AppHolder;
import com.itheima.goolepay.ui.holder.BaseHolder;

public class AppAdapter extends MyBaseAdapter<AppInfo> {

	public AppAdapter(ArrayList<AppInfo> data) {
		super(data);
		
	}

	@Override
	public BaseHolder<AppInfo> getHolder(int position) {
		
		return new AppHolder();
	}

	@Override
	public ArrayList<AppInfo> onLoadMore() {
		
		AppProtocol ap = new AppProtocol();
		ArrayList<AppInfo> moreData = ap.getData(getListSize());
		return moreData;
	}

}
