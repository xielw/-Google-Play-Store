package com.itheima.goolepay.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.itheima.goolepay.protocol.HotProtocol;
import com.itheima.goolepay.ui.view.FlowLayout;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.ui.view.MyFlowLayout;
import com.itheima.goolepay.ui.view.NewFlowLayout;
import com.itheima.goolepay.utils.DrawableUtils;
import com.itheima.goolepay.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 排行
 * @author xielianwu
 *
 */
public class HotFragment extends BaseFragment {

	private ArrayList<String> data;

	@Override
	public View onCreateSuccessView() {
		FlowLayout fl = new FlowLayout(UIUtils.getContext());
		fl.setHorizontalSpacing(8);
		fl.setVerticalSpacing(14);
		//支持上下滑动
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		scrollView.addView(fl);
		int padding = UIUtils.dip2px(10);
		scrollView.setPadding(padding, padding, padding, padding);
		for(int i = 0; i < data.size(); i++){
			TextView textView = new TextView(UIUtils.getContext());
			final String keyWord = data.get(i);
			textView.setText(keyWord);
			Random random = new Random();
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);
			int color = 0xffcecece;//按下后偏白的颜色
			StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r, g, b), color, 6);
			textView.setBackgroundDrawable(selector);
			textView.setTextSize(18);
			
			textView.setPadding(padding, padding, padding, padding);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(Color.WHITE);
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					Toast.makeText(UIUtils.getContext(), keyWord, Toast.LENGTH_LONG).show();
				}
			});
			fl.addView(textView);
		}
		return scrollView;
	}

	@Override
	public ResultSate onLoad() {
		
		HotProtocol hp = new HotProtocol();
		data = hp.getData(0);
		return check(data);
	}

}
