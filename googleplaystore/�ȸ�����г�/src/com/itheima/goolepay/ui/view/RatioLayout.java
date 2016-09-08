package com.itheima.goolepay.ui.view;

import com.itheima.goolepay.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 自定义控件，按照比例来决定布局高度
 * @author xielianwu
 *
 */
public class RatioLayout extends FrameLayout {

	private float ratio;
	

	public RatioLayout(Context context) {
		super(context);
		
	}

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//获取属性值
		//attrs.getAttributeFloatValue(namespace, "ratio", -1);
		//当自定义属性时，系统会自动生成属性相关id,此id通过R.styleable引用
		//obtainStyledAttributes方法第一个参数是属性集合atrrs,第二个参数就是系统自动生成的id:R.styleable.RatioLayout_ratio
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, -1);
		typedArray.recycle();  //回收typedArray，提高性能
		System.out.println("ratio:" + ratio);
		
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, -1);
		typedArray.recycle();  //回收typedArray，提高性能
		System.out.println("ratio:" + ratio);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//1.获取宽度
		//2.根据宽度及比例ratio，计算高度
		//3.重新测量控件
		
		//MeasureSpec.AT_MOST;至多模式,控件有多大显示多大,wrap_content
		//MeasureSpec.EXACTLY;确定模式,类似宽高写死成dip,match_parent
		//MeasureSpec.UNSPECIFIED;未指定模式
		
		int with = MeasureSpec.getSize(widthMeasureSpec);//获取宽度值
		int withMode = MeasureSpec.getMode(widthMeasureSpec); //获取宽度模式
		int height = MeasureSpec.getSize(heightMeasureSpec);//获取高度值
		int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高度模式
		
		//宽度确定,高度不确定,ratio合法,才计算高度值
		if(withMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio > 0){
			
			//图片宽度 = 控件宽度 - 左侧内边距 - 右侧内边距
			int imageWidth = with - getPaddingLeft() - getPaddingRight();
			//图片高度  = 图片宽度 / 宽高比例
			int imageHeight  =   (int) (imageWidth / ratio + 0.5f);
			//控件高度 = 图片高度 + 上侧内边距 + 下侧内边距
			height = imageHeight + getPaddingTop() + getPaddingBottom();
			
			//根据最新的高度重新生成heightMeasureSpec(高度模式是确定的)
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		}
		//按照最新的高度测量控件
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
