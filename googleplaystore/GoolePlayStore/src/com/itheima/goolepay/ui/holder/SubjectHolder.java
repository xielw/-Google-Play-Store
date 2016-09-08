package com.itheima.goolepay.ui.holder;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.SubjectInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectHolder extends BaseHolder<SubjectInfo> {

	private ImageView ivPic;
	private TextView tvTitle;
	private BitmapUtils bitmapUtils;
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_subject);
		ivPic = (ImageView) view.findViewById(R.id.iv_pic);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView(SubjectInfo data) {
		
		bitmapUtils = BitmapHelper.getBitmapUtils();
		bitmapUtils.display(ivPic, HttpHelper.URL + "image?name=" + data.url);
		tvTitle.setText(data.des);
	}

}
