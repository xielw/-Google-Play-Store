package com.itheima.goolepay.ui.holder;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AppHolder extends BaseHolder<AppInfo> {

	private TextView tvName;
	private TextView tvSize;
	private TextView tvDes;
	private ImageView ivIcon;
	private RatingBar rbStar;
	private ImageView ivDown;
	private BitmapUtils bitmapUtils;
	@Override
	public View initView() {
		//1.加载布局
		View view = UIUtils.inflate(R.layout.list_item_app);
		//2.初始化控件
		tvName = (TextView) view.findViewById(R.id.tv_name);
		tvSize = (TextView) view.findViewById(R.id.tv_size);
		tvDes = (TextView) view.findViewById(R.id.tv_des);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		ivDown = (ImageView) view.findViewById(R.id.iv_down);
		rbStar = (RatingBar) view.findViewById(R.id.rb_star);
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		
		tvName.setText(data.name);
		tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
		tvDes.setText(data.des);
		rbStar.setRating(data.stars);
		bitmapUtils = BitmapHelper.getBitmapUtils();
		bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
	}

}
