package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;

import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.protocol.AppProtocol;
import com.itheima.goolepay.ui.adapter.AppAdapter;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.ui.view.MyListView;
import com.itheima.goolepay.utils.UIUtils;

import android.view.View;


/**
 * 应用
 * @author xielianwu
 *
 */
public class AppFragment extends BaseFragment {

	private ArrayList<AppInfo> data;

	//只有成功加载数据时走此方法
	@Override
	public View onCreateSuccessView() {
	
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new AppAdapter(data));
		return listView;
	}


	@Override
	public ResultSate onLoad() {
		
		AppProtocol ap = new AppProtocol();
		data = ap.getData(0);
		return check(data);
	}

}
