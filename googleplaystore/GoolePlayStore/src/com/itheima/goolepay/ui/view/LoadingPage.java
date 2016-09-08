package com.itheima.goolepay.ui.view;

import com.itheima.goolepay.R;
import com.itheima.goolepay.manager.ThreadPool;
import com.itheima.goolepay.utils.UIUtils;

import android.content.Context;
import android.net.NetworkInfo.State;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
/**
 * 根据当前状态宣示不同页面的自定义控件
 * 
 * -未加载 -加载中 -加载失败 -数据为空 -加载成功
 * @author xielianwu
 *
 */
public abstract class LoadingPage extends FrameLayout {

	private static final int STATE_LOAD_UNDO = 1;//未加载
	private static final int STATE_LOAD_LOADING = 2;//正在加载
	private static final int STATE_LOAD_ERROR = 3;//加载失败
	private static final int STATE_LOAD_EMPTY = 4;//数据为空
	private static final int STATE_LOAD_SUCCESS = 5;//加载成功
	
	private int mCurrentState = STATE_LOAD_UNDO; //当前状态
	private View mLoadingPage;
	private View mErrorPage;
	private View mEmptyPage;
	private View mSuccessPage;
	private Button btnRetry;
	public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initView();//初始化布局
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();//初始化布局
	}

	public LoadingPage(Context context) {
		super(context);
		initView();//初始化布局
	}

	//初始化布局
	private void initView() {
	
		//初始化加载中的布局
		if(mLoadingPage == null){
			mLoadingPage = UIUtils.inflate(R.layout.page_loading);
			addView(mLoadingPage);
		}
	
		//初始化加载失败的布局
		if(mErrorPage == null){
			mErrorPage = UIUtils.inflate(R.layout.page_error);
			btnRetry = (Button) mErrorPage.findViewById(R.id.btn_retry);
			btnRetry.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadData();
					System.out.println("加载失败，请重试！！！！");
				}
			});
			addView(mErrorPage);
		}
		
		//初始化布局为空的布局
		if(mEmptyPage == null){
			mEmptyPage = UIUtils.inflate(R.layout.page_empty);
			addView(mEmptyPage);
		}
		
		showRightPage();
	}

	//根据当前状态，决定宣示哪个布局
	private void showRightPage() {
		
		if(mCurrentState == STATE_LOAD_UNDO || mCurrentState == STATE_LOAD_LOADING){
			mLoadingPage.setVisibility(View.VISIBLE);
		}else {
			mLoadingPage.setVisibility(View.GONE);
		}
		
		
		mEmptyPage.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);
		mErrorPage.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);
		
		if(mSuccessPage == null && mCurrentState == STATE_LOAD_SUCCESS){
			mSuccessPage = onCreateSuccessView();
			if(mSuccessPage != null)
				addView(mSuccessPage);
		}
		
		if(mSuccessPage != null)
			mSuccessPage.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
	}
		//加载成功后显示的布局，必须由调用者来实现
	public abstract View onCreateSuccessView();
	
	//开始加载数据
	public void loadData(){
		
		if(mCurrentState != STATE_LOAD_LOADING){  //若当前没有加载，就开始加载数据
			
			mCurrentState = STATE_LOAD_LOADING;
//			new Thread(){
//				public void run() {
//					final ResultSate resultSate = onLoad();
//					//切换到主线程
//					UIUtils.runOnUIThread(new Runnable() {
//						
//						@Override
//						public void run() {
//						
//							if(resultSate != null){
//								mCurrentState = resultSate.getState(); //网路状态结束后，更新网络状态
//								
//								//根据当前最新的状态刷新页面
//								showRightPage();
//							}
//						}
//					});
//					
//				};
//			}.start();
			
			ThreadPool.getThreadPool().execute(new Runnable() {
				
				@Override
				public void run() {
					
					final ResultSate resultSate = onLoad();
					//切换到主线程
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
						
							if(resultSate != null){
								mCurrentState = resultSate.getState(); //网路状态结束后，更新网络状态
								
								//根据当前最新的状态刷新页面
								showRightPage();
							}
						}
					});
				}
			});
		}
	}
	
	//加载网络数据，返回值表示请求网络结果后的状态
	public abstract ResultSate onLoad();
	
	public enum ResultSate{
		
		STATE_SUCCESS(STATE_LOAD_SUCCESS),
		STATE_EMPTY(STATE_LOAD_EMPTY),
		STATE_ERROR(STATE_LOAD_ERROR);
		
		private ResultSate(int state){
			this.state = state;
		}
		
		private int state;
		
		public int getState(){
			return state;
		}
	}
}