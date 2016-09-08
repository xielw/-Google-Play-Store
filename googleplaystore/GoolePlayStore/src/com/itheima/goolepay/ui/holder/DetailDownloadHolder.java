package com.itheima.goolepay.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.domain.DownloadInfo;
import com.itheima.goolepay.manager.DownLoadManager;
import com.itheima.goolepay.manager.DownLoadManager.DownLoadObserver;
import com.itheima.goolepay.ui.view.ProgressHorizontal;
import com.itheima.goolepay.utils.UIUtils;

public class DetailDownloadHolder extends BaseHolder<AppInfo> implements DownLoadObserver,OnClickListener{

	private DownLoadManager mDM;
	private int mCurrentState;
	private float mCurrentPos;
	private Button btnDownload;
	private FrameLayout flProgress;
	private ProgressHorizontal pbProgress;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_download);
		btnDownload = (Button) view.findViewById(R.id.btn_download);
		
		flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
		flProgress.setOnClickListener(this);
		btnDownload.setOnClickListener(this);
		pbProgress = new ProgressHorizontal(UIUtils.getContext());

		pbProgress.setProgressBackgroundResource(R.drawable.progress_bg);//进度条背景图片
		pbProgress.setProgressResource(R.drawable.progress_normal);//进度条图片
		pbProgress.setProgressTextColor(Color.WHITE);//进度文字颜色
		pbProgress.setProgressTextSize(UIUtils.dip2px(16));//进度文字大小
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	
		//更贞布局添加自定义进度条
		flProgress.addView(pbProgress,params);
		
		mDM = new DownLoadManager();
		mDM.registerObserver(this);//注册观察者，监听状态和进度变化
		
		
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		
		DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
		//判断当前应用是否下载过
		if(downloadInfo != null){
			
			mCurrentState = downloadInfo.currentState;
			mCurrentPos = downloadInfo.getProgress();
			
		}else {
			//没有下载过
			mCurrentPos = 0;
			mCurrentState = DownLoadManager.STATE_UNDO;
		}
		
		refreshUI(mCurrentPos,mCurrentState);
	}
		

	private void refreshUI(float currentPos,int currentState) {
		
		switch (currentState) {
			case DownLoadManager.STATE_DOWNLOADING:
				
				flProgress.setVisibility(View.VISIBLE);
				btnDownload.setVisibility(View.GONE);
				pbProgress.setCenterText("");
				pbProgress.setProgress(currentPos);
				System.out.println("///STATE_DOWNLOADING///正在下载了");
				break;
			case DownLoadManager.STATE_ERROR:
				flProgress.setVisibility(View.GONE);
				btnDownload.setVisibility(View.VISIBLE);
				btnDownload.setText("载下失败");
			
				System.out.println("///STATE_ERROR///载失下败");
				break;
			case DownLoadManager.STATE_PAUSE:
				
				flProgress.setVisibility(View.VISIBLE);
				btnDownload.setVisibility(View.GONE);
				pbProgress.setProgress(currentPos);
				System.out.println("///STATE_PAUSE///暂停");
				pbProgress.setCenterText("暂停");
				break;
			case DownLoadManager.STATE_SUCCESS:
				btnDownload.setVisibility(View.VISIBLE);
				flProgress.setVisibility(View.GONE);
				btnDownload.setText("安装");
				System.out.println("///STATE_SUCCESS///安装");
				break;
			case DownLoadManager.STATE_UNDO:
				flProgress.setVisibility(View.GONE);
				btnDownload.setVisibility(View.VISIBLE);
				System.out.println("///STATE_UNDO///下载");
				btnDownload.setText("下载");
				break;
			case DownLoadManager.STATE_WAITING:
				flProgress.setVisibility(View.GONE);
				btnDownload.setVisibility(View.VISIBLE);
				System.out.println("///STATE_WAITING///等待下载了");
				btnDownload.setText("等待中...");
				break;
		}
	}

	//状态更新
	@Override
	public void downLoadStateChange(DownloadInfo info) {
		//判断下载对象是否是当前应用
		AppInfo appInfo = getData();
		if(appInfo.id.equals(info.id)){
			mCurrentState = info.currentState;
			refreshUIOnMainThread(info);
			
		}
		
	}

	//进度更新,子线程
	@Override
	public void onDownloadProgressChanged(DownloadInfo info) {
		//判断下载对象是否是当前应用
		AppInfo appInfo = getData();
		if(appInfo.id.equals(info.id)){
			mCurrentState = info.currentState;
			refreshUIOnMainThread(info);
			
		}
		
		System.out.println("---DetailDownloadHolder--onDownloadProgressChanged");
		
	}

	//在主线程更新UI
	private void refreshUIOnMainThread(final DownloadInfo info){
		UIUtils.runOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				refreshUI(info.getProgress(),info.currentState);	
			}
		});
	}

	@Override
	public void onClick(View v) {
		System.out.println("///onClick---" + mCurrentState);
		switch (v.getId()) {
		case R.id.fl_progress:
		case R.id.btn_download:
			if(mCurrentState == DownLoadManager.STATE_ERROR || mCurrentState == DownLoadManager.STATE_PAUSE || mCurrentState == DownLoadManager.STATE_UNDO){
				//下载
				System.out.println("//下载");
				System.out.println("/////000");
				mDM.download(getData());
			}else if (mCurrentState == DownLoadManager.STATE_DOWNLOADING || mCurrentState == DownLoadManager.STATE_WAITING) {
				//暂停
				System.out.println("/////暂停");
				mDM.pause(getData());
			}else if(mCurrentState == DownLoadManager.STATE_SUCCESS){
				//安装
				mDM.install(getData());
			}
			break;
		}
	}
}
