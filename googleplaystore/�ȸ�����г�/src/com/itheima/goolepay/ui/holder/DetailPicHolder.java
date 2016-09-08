package com.itheima.goolepay.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class DetailPicHolder extends BaseHolder<AppInfo> {

	private ImageView[] ivPics;

	@Override
	public View initView() {
	
		View view = UIUtils.inflate(R.layout.layout_detail_picinfo);
		ivPics = new ImageView[5];
		ivPics[0] = (ImageView) view.findViewById(R.id.iv_pic1);
		ivPics[1] = (ImageView) view.findViewById(R.id.iv_pic2);
		ivPics[2] = (ImageView) view.findViewById(R.id.iv_pic3);
		ivPics[3] = (ImageView) view.findViewById(R.id.iv_pic4);
		ivPics[4] = (ImageView) view.findViewById(R.id.iv_pic5);
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils();
		for(int i = 0; i < 5; i++){
			if(i < data.screen.size()){
				//设置imageView
				bitmapUtils.display(ivPics[i], HttpHelper.URL + "image?name=" + data.screen.get(i));
			}else {
				//除去多余的imageView
				ivPics[i].setVisibility(View.GONE);
			}
		}
	}

}
