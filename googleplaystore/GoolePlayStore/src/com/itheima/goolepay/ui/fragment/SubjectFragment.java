package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.SubjectInfo;
import com.itheima.goolepay.protocol.SubjectProtocol;
import com.itheima.goolepay.ui.adapter.SubjectAdapter;
import com.itheima.goolepay.ui.view.MyListView;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.utils.UIUtils;

import android.view.View;


/**
 * 专题
 * @author xielianwu
 *
 */
public class SubjectFragment extends BaseFragment {

	private ArrayList<SubjectInfo> data;

	@Override
	public View onCreateSuccessView() {
		
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new SubjectAdapter(data));
		return listView;
	}

	@Override
	public ResultSate onLoad() {
	
		SubjectProtocol sp = new SubjectProtocol();
		data = sp.getData(0);
		return check(data);
	}

}
