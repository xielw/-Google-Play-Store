package com.itheima.goolepay.ui.holder;

import java.util.ArrayList;

import com.itheima.goolepay.R;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

	private ViewPager mViewPager;
	
	private LinearLayout llContainer;
	private int mPrePosition = 0;;
	@Override
	public View initView() {
		RelativeLayout rl = new RelativeLayout(UIUtils.getContext());
		AbsListView.LayoutParams rlParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(140)) ;
		rl.setLayoutParams(rlParams);
		mViewPager = new ViewPager(UIUtils.getContext());
		RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rl.addView(mViewPager, vpParams);
	
		llContainer = new LinearLayout(UIUtils.getContext());
		RelativeLayout.LayoutParams RlParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		RlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llContainer.setLayoutParams(RlParams);
		int padding = UIUtils.dip2px(10);
		llContainer.setPadding(padding, padding, padding, padding);
		rl.addView(llContainer);
		return rl;
	}

	@Override
	public void refreshView(final ArrayList<String> data) {
		HomeHeaderAdapter adapter = new HomeHeaderAdapter(data);
		mViewPager.setAdapter(adapter);
		int startPosition = data.size() * 10000;
		mViewPager.setCurrentItem(startPosition);
		HomeHeaderTask task = new HomeHeaderTask();
		task.start();
	
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		for(int i = 0; i < data.size(); i++){
			ImageView point = new ImageView(UIUtils.getContext());
			point.setImageResource(R.drawable.indicator_normal);
			if(0 == i){
				point.setImageResource(R.drawable.indicator_selected);
			}else {
				params.leftMargin = UIUtils.dip2px(3);
			}
			llContainer.addView(point,params);
		}
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				position = position % data.size();
				ImageView point = (ImageView) llContainer.getChildAt(position);
				point.setImageResource(R.drawable.indicator_selected);
				ImageView prePoint = (ImageView) llContainer.getChildAt(mPrePosition);
				prePoint.setImageResource(R.drawable.indicator_normal);
				mPrePosition = position;
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			
			}
		});
	
	}

	class HomeHeaderTask implements Runnable{

		public void start(){
			//移除之前发送的消息，避免消息重复
			UIUtils.getHandler().removeCallbacksAndMessages(null);
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		@Override
		public void run() {
			
			int  currentItem = mViewPager.getCurrentItem();
			currentItem ++;
			mViewPager.setCurrentItem(currentItem);
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		
	}
	class HomeHeaderAdapter extends PagerAdapter{

		private BitmapUtils bitmapUtils;
		private ArrayList<String> data;
		public HomeHeaderAdapter(ArrayList<String> data){
			this.data = data;
			bitmapUtils = BitmapHelper.getBitmapUtils();
		}
		@Override
		public int getCount() {
			
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			position = position % data.size();
			ImageView imageView = new ImageView(UIUtils.getContext());
			imageView.setScaleType(ScaleType.FIT_XY);
			String url = data.get(position);
			bitmapUtils.display(imageView, HttpHelper.URL + "image?name=" +url);
			container.addView(imageView);
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			container.removeView((View) object);
			
		}
		
	}
}