package com.itheima.goolepay.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.CategoryInfo;
import com.itheima.goolepay.utils.UIUtils;

public class TitleHolder extends BaseHolder<CategoryInfo> {

	private TextView tvTitle;

	@Override
	public View initView() {
	
		View view = UIUtils.inflate(R.layout.list_item_title);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		tvTitle.setText(data.title);
	}

}
