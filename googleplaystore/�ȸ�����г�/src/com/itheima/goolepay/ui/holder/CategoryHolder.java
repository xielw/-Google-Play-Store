package com.itheima.goolepay.ui.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.CategoryInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class CategoryHolder extends BaseHolder<CategoryInfo> implements OnClickListener{

	private LinearLayout llGrid1;
	private LinearLayout llGrid2;
	private LinearLayout llGrid3;
	private ImageView ivIcon1;
	private ImageView ivIcon2;
	private ImageView ivIcon3;
	private TextView name1;
	private TextView name2;
	private TextView name3;
	private BitmapUtils bitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_category);
		llGrid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
		llGrid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);
		llGrid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);
		ivIcon1 = (ImageView) view.findViewById(R.id.iv_icon1);
		ivIcon2 = (ImageView) view.findViewById(R.id.iv_icon2);
		ivIcon3 = (ImageView) view.findViewById(R.id.iv_icon3);
		name1 = (TextView) view.findViewById(R.id.tv_name1);
		name2 = (TextView) view.findViewById(R.id.tv_name2);
		name3 = (TextView) view.findViewById(R.id.tv_name3);
		
		llGrid1.setOnClickListener(this);
		llGrid2.setOnClickListener(this);
		llGrid3.setOnClickListener(this);
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		
		bitmapUtils.display(ivIcon1, HttpHelper.URL + "image?name=" + data.url1);
		name1.setText(data.name1);
		
		bitmapUtils.display(ivIcon2, HttpHelper.URL + "image?name=" + data.url2);
		name2.setText(data.name2);
		
		bitmapUtils.display(ivIcon3, HttpHelper.URL + "image?name=" + data.url3);
		name3.setText(data.name3);
		System.out.println("--分类刷新:"+data.name1 +"--"+ data.name2);
		
	}

	@Override
	public void onClick(View v) {
		CategoryInfo info = getData();
		switch (v.getId()) {
		case R.id.ll_grid1:
			Toast.makeText(UIUtils.getContext(),info.name1, Toast.LENGTH_SHORT).show();
			break;

		case R.id.ll_grid2:
			Toast.makeText(UIUtils.getContext(),info.name2, Toast.LENGTH_SHORT).show();
			break;
		case R.id.ll_grid3:
			Toast.makeText(UIUtils.getContext(),info.name3, Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
