package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;

import com.itheima.goolepay.R;
import com.itheima.goolepay.R.array;
import com.itheima.goolepay.R.id;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.protocol.HomeProtocol;
import com.itheima.goolepay.ui.activity.HomeDetailActivity;
import com.itheima.goolepay.ui.adapter.HomeAdapter;
import com.itheima.goolepay.ui.holder.HomeHeaderHolder;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.ui.view.MyListView;
import com.itheima.goolepay.utils.UIUtils;

import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 首页
 * @author xielianwu
 *
 */
public class HomeFragment extends BaseFragment {

	
	private ArrayList<AppInfo> data;
	private ArrayList<String> pictureList;


	//数据加载成功时调用此方法。运行在主线程
	@Override
	public View onCreateSuccessView() {
	
		MyListView view = new MyListView(UIUtils.getContext());
		
		HomeHeaderHolder holder = new HomeHeaderHolder();
		View headRootView = holder.getRootView();
		holder.setData(pictureList);
		view.addHeaderView(headRootView);
		view.setAdapter(new HomeAdapter(data));
		
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				Intent intent = new Intent(getActivity(), HomeDetailActivity.class);
				AppInfo info = data.get(position - 1);//减掉一个头布局
				intent.putExtra("packageName", info.packageName);
				startActivity(intent);
			}
		});
		return view;
	}

	
	// 运行在主线程，可以直接执行耗时网络操作
	@Override
	public ResultSate onLoad() {
	
		/*data = new ArrayList<String>();
		for(int i = 0; i < 20; i++){
			data.add("测试数据" + i);
		}*/
		
		HomeProtocol hp = new HomeProtocol();
		data = hp.getData(0);
		pictureList = hp.getPictureList();
		return check(data); //校验数据并返回
	}

	
}
