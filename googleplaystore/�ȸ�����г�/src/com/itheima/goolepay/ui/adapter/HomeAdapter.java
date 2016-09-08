package com.itheima.goolepay.ui.adapter;

import java.util.ArrayList;

import android.os.SystemClock;

import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.protocol.HomeProtocol;
import com.itheima.goolepay.ui.holder.BaseHolder;
import com.itheima.goolepay.ui.holder.HomeHolder;

public class HomeAdapter extends MyBaseAdapter<AppInfo> {

	
	public HomeAdapter(ArrayList<AppInfo> data) {
		super(data);
	
	}

	@Override
	public BaseHolder<AppInfo> getHolder(int position) {
		
		
		return new HomeHolder();
	}

	

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		HolderView holderView;
		if(convertView == null){
			//1.加载布局文件
			convertView = UIUtils.inflate(R.layout.list_item_home);
			holderView = new HolderView();
			//2.初始化控件 findViewById
			holderView.tvConten = (TextView) convertView.findViewById(R.id.tv_content);
			//3.打一个标记tag
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
			//4.根据数据来刷新界面
		holderView.tvConten.setText(data.get(position));
		return convertView;
	
	}
	
	 class HolderView {
		 TextView tvConten;
	 }*/
	

	//此方法运行在子线程
	@Override
	public ArrayList<AppInfo> onLoadMore() {
		
		/*ArrayList<String> moreData = new ArrayList<String>();
		for(int i = 0;i < 20; i++){
			moreData.add("测试更多数据:" + i);
		}
		SystemClock.sleep(2000);*/
		
		HomeProtocol hp = new HomeProtocol();
		// 20,40,60...
		//下一页数据的位置等于当前集合的大小
		ArrayList<AppInfo> moreData = hp.getData(getListSize());
		return moreData;
		
	}
}
