package com.itheima.goolepay.ui.adapter;

import java.util.ArrayList;

import com.itheima.goolepay.domain.SubjectInfo;
import com.itheima.goolepay.protocol.SubjectProtocol;
import com.itheima.goolepay.ui.holder.BaseHolder;
import com.itheima.goolepay.ui.holder.SubjectHolder;

public class SubjectAdapter extends MyBaseAdapter<SubjectInfo> {

	public SubjectAdapter(ArrayList<SubjectInfo> data) {
		super(data);
		
	}

	@Override
	public BaseHolder<SubjectInfo> getHolder(int position) {
		
		return new SubjectHolder();
	}

	@Override
	public ArrayList<SubjectInfo> onLoadMore() {
		
		SubjectProtocol sp = new SubjectProtocol();
		ArrayList<SubjectInfo> moreData = sp.getData(getListSize());
		return moreData;
	}

}
