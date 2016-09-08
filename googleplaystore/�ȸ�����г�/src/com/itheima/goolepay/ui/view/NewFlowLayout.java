package com.itheima.goolepay.ui.view;


import java.util.ArrayList;

import com.itheima.goolepay.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
//ViewTreeObserver
public class NewFlowLayout extends ViewGroup {

	private int mHorizontalSpacing = UIUtils.dip2px(6);
	private int mVerticalSpacing = UIUtils.dip2px(8);
	private ArrayList<Line> mLineList = new ArrayList<NewFlowLayout.Line>();
	private Line mLine = null;
	private int mTotalUsedWith = 0; //当前行已经用的总宽度
	private final int MAX_LINE = 100;
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		System.out.println("--l:" + l);
		System.out.println("--t:" + t);
		
		System.out.println("---------------------------------------------------");
		System.out.println("--l:" + l);
		System.out.println("--t:" + t);
		if(changed){
			
			l = getPaddingLeft();
			t = getPaddingTop();
			for(int i = 0; i < mLineList.size();i++){
				Line line = mLineList.get(i);
				line.layout(l,t);
				t += mVerticalSpacing + line.maxHeight;
			}
			
		}
	}

	public NewFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public NewFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public NewFlowLayout(Context context) {
		super(context);
		
	}

	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	
		System.out.println("---MeasureSpec.getSize(widthMeasureSpec):" + MeasureSpec.getSize(widthMeasureSpec));
		System.out.println("---MeasureSpec.getSize(heightMeasureSpec):" + MeasureSpec.getSize(heightMeasureSpec));
		int withSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
		int withMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int childCount = getChildCount();
		for(int i = 0; i < childCount; i++){
			View childView = getChildAt(i);
			int childWithMode = withMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : withMode;
			int childHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;
			int childWithMeasureSpec = MeasureSpec.makeMeasureSpec(withSize, childWithMode);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, childHeightMode);
			childView.measure(childWithMeasureSpec, childHeightMeasureSpec);
			
			if(null == mLine){
				mLine = new Line();
			}
			mTotalUsedWith += childView.getWidth();
			if(mTotalUsedWith < withSize){
				
				mLine.addView(childView);
				
				if(mTotalUsedWith + mHorizontalSpacing < withSize){
					mTotalUsedWith += mHorizontalSpacing;
				}else {
				//	mLineList.add(mLine);
					if(!newLine()){
						break;
					}
				}
			}else {
				if(mLine.getViewCount() ==0){
					mLine.addView(childView);
					if(!newLine()){
						break;
					}
				}else {
					//mLineList.add(mLine);
					if(!newLine()){
						break;
					}
					mLine.addView(childView);
					// 换行后mUsedWidth归零, 需要重新初始化
					mTotalUsedWith += childView.getMeasuredWidth() + mHorizontalSpacing;
				}
			}
			
		}
		
		if(mLine != null && !mLineList.contains(mLine) &&mLine.getViewCount() > 0){
			mLineList.add(mLine);
		}
		// 计算整个控件的宽高
		int totalWith = MeasureSpec.getSize(widthMeasureSpec);  //待查getSize(widthMeasureSpec) 与getMeasureWith()区别
		int totalHeight = 0;
		int lineCount = mLineList.size();
		for(int i = 0;i < lineCount;i++){
			totalHeight += mLineList.get(i).maxHeight;
		}
		
		totalHeight += (lineCount - 1)*mVerticalSpacing + getPaddingBottom() + getPaddingTop();
		
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(totalWith, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private boolean newLine() {
		
		mLineList.add(mLine);
		if(mLineList.size() < MAX_LINE){
			mLine = new Line();
			mTotalUsedWith = 0;
			return true;
		}
		
		return false;
	}

	
	public void setHorizontalSpacing(int spacing){
		mHorizontalSpacing = UIUtils.dip2px(spacing);
	}
	
	public void setVerticalSpacing(int spacing){
		mVerticalSpacing = UIUtils.dip2px(spacing);
	}
	
	public class Line{
		ArrayList<View> viewList = new ArrayList<View>();
		private int mTotalWith = 0;
		int maxHeight = 0;
		public void addView(View view){
			viewList.add(view);
			int viewHeight = view.getMeasuredHeight();
			int viewWith = view.getMeasuredWidth();
			mTotalWith += viewWith;
			maxHeight = maxHeight > viewHeight  ? maxHeight : viewHeight;
			view.getHeight();
		}
		
		public void layout(int l, int t) {
			
			int surplusWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mTotalWith -(getViewCount() - 1) *mHorizontalSpacing;
			if(surplusWidth >= 0){
				int spacing = (int)((float)surplusWidth / getViewCount() + 0.5f);
				
				for(int i = 0; i < viewList.size(); i++){
					View view =  viewList.get(i);
					int childWith =view.getMeasuredWidth() + spacing;
					int childHeight = view.getMeasuredHeight();
					int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWith, MeasureSpec.EXACTLY);
					int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
					view.measure(widthMeasureSpec, heightMeasureSpec);
					if(childHeight < maxHeight){
						int offSet = (int) ((maxHeight - childHeight) / 2 + 0.5f); 
						if(offSet < 0){
							offSet = 0;
						}
						
						t += offSet;
					}
					view.layout(l,t,l + childWith,t + childHeight);
					l += mHorizontalSpacing + childWith;
				}
			}else {
				//view的宽度大于父控件宽度
				View view = viewList.get(0);
				view.layout(l, t, l + view.getMeasuredWidth(), t + view.getMeasuredHeight());
			}
			
		}

		public int getViewCount(){
			return viewList.size();
		}
		
		
	}
	
	
}
