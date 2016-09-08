package com.itheima.goolepay.ui.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class DetailSafeHolder extends BaseHolder<AppInfo> implements OnClickListener {

	private ImageView[] ivSafe;
	private ImageView[] ivDes;
	private TextView[] tvDes;
	private LinearLayout[] llDes;
	private ImageView ivArrow;
	private LinearLayout llDesRoot;
	//boolean isClicked = false;
	boolean isOpen = false;
	private int mHeight;
	private LinearLayout.LayoutParams mParams;
	@Override
	public View initView() {
		
		View view = UIUtils.inflate(R.layout.layout_detail_safeinfo);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		llDesRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);
	
		ivArrow.setOnClickListener(this);
		ivSafe = new ImageView[4];
		ivSafe[0] = (ImageView) view.findViewById(R.id.iv_safe1);
		ivSafe[1] = (ImageView) view.findViewById(R.id.iv_safe2);
		ivSafe[2] = (ImageView) view.findViewById(R.id.iv_safe3);
		ivSafe[3] = (ImageView) view.findViewById(R.id.iv_safe4);
		
		ivDes = new ImageView[4];
		ivDes[0] = (ImageView) view.findViewById(R.id.iv_des1);
		ivDes[1] = (ImageView) view.findViewById(R.id.iv_des2);
		ivDes[2] = (ImageView) view.findViewById(R.id.iv_des3);
		ivDes[3] = (ImageView) view.findViewById(R.id.iv_des4);
		
		tvDes = new TextView[4];
		tvDes[0] = (TextView) view.findViewById(R.id.tv_des1);
		tvDes[1] = (TextView) view.findViewById(R.id.tv_des2);
		tvDes[2] = (TextView) view.findViewById(R.id.tv_des3);
		tvDes[3] = (TextView) view.findViewById(R.id.tv_des4);
		
		llDes = new LinearLayout[4];
		llDes[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
		llDes[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
		llDes[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
		llDes[3] = (LinearLayout) view.findViewById(R.id.ll_des4);
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils();
		
		for(int i = 0 ; i < 4; i++){
			if(i < data.safe.size()){
				bitmapUtils.display(ivSafe[i], HttpHelper.URL + "image?name=" +data.safe.get(i).safeUrl);
				bitmapUtils.display(ivDes[i], HttpHelper.URL + "image?name=" +data.safe.get(i).safeDesUrl);
				tvDes[i].setText(data.safe.get(i).safeDes);
			}else {
				ivSafe[i].setVisibility(View.GONE);
				llDes[i].setVisibility(View.GONE);
			}
		}
		
		llDesRoot.measure(0, 0);
		mHeight = llDesRoot.getMeasuredHeight();
		System.out.println("--高度:" + mHeight);
		mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mParams.height = 0;
		llDesRoot.setLayoutParams(mParams);
		
		
	}

	@Override
	public void onClick(View v) {
		
		/*if(!isClicked){
			llDesRoot.setVisibility(View.VISIBLE);
			isClicked = true;
			ivArrow.setImageResource(R.drawable.arrow_up);
		}else{
			llDesRoot.setVisibility(View.GONE);
			isClicked = false;
			ivArrow.setImageResource(R.drawable.arrow_down);
		}
		*/
		
		toogle();
	}

	private void toogle() {

		ValueAnimator animator = null;
		if (isOpen) {
			// 关闭
			isOpen = false;
			//animator = new ValueAnimator();
			//animator.setIntValues(mHeight,0);
			animator = ValueAnimator.ofInt(mHeight,0);
			
		} else {
			// 打开
			isOpen = true;
			//属性动画
			animator = ValueAnimator.ofInt(0,mHeight);//从某个值变化到某个值
		
//			animator = new ValueAnimator();
//			animator.setIntValues(0,mHeight);
			
		}
		animator.setDuration(200);
		animator.start();
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				//重新获得最新高度
				Integer height =  (Integer) animator.getAnimatedValue();
				System.out.println("---height:" + height);
				mParams.height = height;
				llDesRoot.setLayoutParams(mParams);
			}
		});
		
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
			
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
			
				if(isOpen){
					ivArrow.setImageResource(R.drawable.arrow_up);
				}else {
					ivArrow.setImageResource(R.drawable.arrow_down);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
			
			}
		});
	}

}
