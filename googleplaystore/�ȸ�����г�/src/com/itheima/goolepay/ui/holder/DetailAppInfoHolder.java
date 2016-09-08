package com.itheima.goolepay.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class DetailAppInfoHolder extends BaseHolder<AppInfo> {

	private ImageView ivIcon;
	private TextView tvName;
	private RatingBar rbStar;
	private TextView tvDownLoadNum;
	private TextView tvVersion;
	private TextView tvDate;
	private TextView tvSize;
	private BitmapUtils bitmapUtils;

	@Override
	public View initView() {
		
		View view = UIUtils.inflate(R.layout.layout_detail_appinfo);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		tvName = (TextView) view.findViewById(R.id.tv_name);
		rbStar = (RatingBar) view.findViewById(R.id.rb_star);
		tvDownLoadNum = (TextView) view.findViewById(R.id.tv_download_num);
		tvVersion = (TextView) view.findViewById(R.id.tv_version);
		tvDate = (TextView) view.findViewById(R.id.tv_date);
		tvSize = (TextView) view.findViewById(R.id.tv_size);
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		bitmapUtils = BitmapHelper.getBitmapUtils();
		bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
		tvName.setText(data.name);
		tvDate.setText(data.date);
		tvDownLoadNum.setText("下载量:"+data.downloadNum);
		tvSize.setText(Formatter.formatShortFileSize(UIUtils.getContext(), data.size));
		rbStar.setRating(data.stars);
		tvVersion.setText("版本:"+data.version);
	}

}
