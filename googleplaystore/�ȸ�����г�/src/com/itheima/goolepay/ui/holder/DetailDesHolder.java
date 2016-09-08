package com.itheima.goolepay.ui.holder;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class DetailDesHolder extends BaseHolder<AppInfo> implements OnClickListener{

	private TextView tvDes;
	private TextView tvAuthor;
	private ImageView ivArrow;
	
	boolean isOpen;
	private LinearLayout.LayoutParams params;
	private int longHeight;
	private int shortHeight;
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_desinfo);
		tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
		tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
	
		tvDes.setText(data.des);
		tvAuthor.setText(data.author);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.height = getShortHeight();
		tvDes.setLayoutParams(params);
		
		ivArrow.setOnClickListener(this);
		isOpen = false;
		
	}
	
	public int getShortHeight(){
		
		TextView view = new TextView(UIUtils.getContext());
		view.setText(getData().des);
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		view.setMaxLines(7);
		int with = view.getMeasuredWidth();
		System.out.println("---view.getMeasuredWidth():" + with);
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(with, MeasureSpec.EXACTLY);//match_parent
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);//wrap_content	
		view.measure(widthMeasureSpec, heightMeasureSpec);
		return view.getMeasuredHeight();
	}

      public int getLongHeight(){
		
		TextView view = new TextView(UIUtils.getContext());
		view.setText(getData().des);
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		
		int with = view.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(with, MeasureSpec.EXACTLY);//match_parent
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);//wrap_content	
		view.measure(widthMeasureSpec, heightMeasureSpec);
		return view.getMeasuredHeight();
	}

	@Override
	public void onClick(View v) {
		
		toggle();
	}

	private void toggle() {
		ValueAnimator animator = null;
		longHeight = getLongHeight();
		shortHeight = getShortHeight();
		if(isOpen){
			//关闭
			animator = ValueAnimator.ofInt(longHeight,shortHeight);
			isOpen = false;
			
		}else {
			//打开
			isOpen = true;
			animator = ValueAnimator.ofInt(shortHeight,longHeight);
		}
		
		animator.setDuration(200);
		animator.start();
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int height = (Integer) valueAnimator.getAnimatedValue();
				System.out.println("--动画变动时的高度值:" + height);
				params.height = height;
				tvDes.setLayoutParams(params);
			}
		});
		
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animator) {
			
			}
			
			@Override
			public void onAnimationRepeat(Animator animator) {
			
			}
			
			@Override
			public void onAnimationEnd(Animator animator) {
			

				if(isOpen){
					ivArrow.setImageResource(R.drawable.arrow_up);
				}else {
					ivArrow.setImageResource(R.drawable.arrow_down);
				}
				
				final ScrollView svContainer = getScrollView();
				svContainer.post(new Runnable() {
					
					@Override
					public void run() {
						svContainer.fullScroll(View.FOCUS_DOWN);
						
					}
				});
			}
			

			@Override
			public void onAnimationCancel(Animator animator) {
		
			}
		});
	}
	
	private ScrollView getScrollView() {
		
		ViewParent parent = tvDes.getParent();
		while (!(parent instanceof ScrollView)) {
			parent = parent.getParent();
		}
		return (ScrollView) parent;
	}
	
}
