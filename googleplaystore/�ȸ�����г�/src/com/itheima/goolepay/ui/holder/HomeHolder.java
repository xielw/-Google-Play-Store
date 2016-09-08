package com.itheima.goolepay.ui.holder;

import com.itcast.googleplayteach.ui.widget.ProgressArc;
import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.domain.DownloadInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.manager.DownLoadManager;
import com.itheima.goolepay.manager.DownLoadManager.DownLoadObserver;
import com.itheima.goolepay.utils.BitmapHelper;
import com.itheima.goolepay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HomeHolder extends BaseHolder<AppInfo> implements DownLoadObserver,OnClickListener {

	private TextView tvName;
	private TextView tvSize;
	private TextView tvDes;
	private ImageView ivIcon;
	private RatingBar rbStar;
	
	private BitmapUtils bitmapUtils;
	private FrameLayout flProgress;
	private ProgressArc pbProgress;
	private DownLoadManager mDM;
	private int mCurrentState;
	private float mProgress;
	private TextView tvDownload;
	@Override
	public View initView() {
		//1.加载布局
		View view = UIUtils.inflate(R.layout.list_item_home);
		//2.初始化控件
		tvName = (TextView) view.findViewById(R.id.tv_name);
		tvSize = (TextView) view.findViewById(R.id.tv_size);
		tvDes = (TextView) view.findViewById(R.id.tv_des);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		rbStar = (RatingBar) view.findViewById(R.id.rb_star);
		tvDownload = (TextView) view.findViewById(R.id.tv_download);
		flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
		flProgress.setOnClickListener(this);
		//初始化圆形进度条
    	pbProgress = new ProgressArc(UIUtils.getContext());
	    //设置圆形进度条直径
		pbProgress.setArcDiameter(UIUtils.dip2px(26));
		//设置进度条颜色
	    pbProgress.setProgressColor(UIUtils.getColor(R.color.progress));
		//设置进度条宽高布局参数
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						UIUtils.dip2px(27), UIUtils.dip2px(27));
		flProgress.addView(pbProgress, params);
		
	
		mDM = DownLoadManager.getInstance();
		mDM.registerObserver(this);
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		
		if(data == null)
			return;
		tvName.setText(data.name);
		tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
		tvDes.setText(data.des);
		rbStar.setRating(data.stars);
		bitmapUtils = BitmapHelper.getBitmapUtils();
		bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
		
		DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
		if(downloadInfo != null){
			System.out.println("///////downloadInfo = " + downloadInfo.currentState);
			mCurrentState = downloadInfo.currentState;
		    mProgress = downloadInfo.getProgress();
		}else {
			
			System.out.println("///////downloadInfo = null");
			mCurrentState = DownLoadManager.STATE_UNDO;
			mProgress = 0;
		}
		
		refreshUI(mProgress,mCurrentState,data.id);
	}

	private void refreshUI( float progress, int currentState,String id) {
		if(!(id.equals(getData().id))){
			return ;
		}
		switch (currentState) {
		case DownLoadManager.STATE_DOWNLOADING:
			pbProgress.setBackgroundResource(R.drawable.ic_pause);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
			pbProgress.setProgress(progress, true);
			tvDownload.setText((int) (progress * 100) + "%");
			break;
		case DownLoadManager.STATE_ERROR:
			pbProgress.setBackgroundResource(R.drawable.ic_redownload);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvDownload.setText("下载失败");
			break;
		case DownLoadManager.STATE_PAUSE:
			pbProgress.setBackgroundResource(R.drawable.ic_resume);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			flProgress.setVisibility(View.VISIBLE);
			break;
		case DownLoadManager.STATE_SUCCESS:
			pbProgress.setBackgroundResource(R.drawable.ic_install);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvDownload.setText("安装");
			break;
		case DownLoadManager.STATE_UNDO:
			pbProgress.setBackgroundResource(R.drawable.ic_download);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tvDownload.setText("下载");
			break;
		case DownLoadManager.STATE_WAITING:
			pbProgress.setBackgroundResource(R.drawable.ic_download);
			pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
			tvDownload.setText("等待中...");
			break;
		
		}
	}

	//在主线程更新UI
		private void refreshUIOnMainThread(final DownloadInfo info){
			UIUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					if(info.id.equals(getData().id)){
						refreshUI(info.getProgress(),info.currentState,info.id);	
					}
					
				}
			});
		}
		
	@Override
	public void onClick(View v) {
		System.out.println("/////////"+getData().name);
		System.out.println("///////mCurrentState = " + mCurrentState +"");
		if(mCurrentState == DownLoadManager.STATE_DOWNLOADING || mCurrentState == DownLoadManager.STATE_WAITING){
			//暂停下载
			mDM.pause(getData());
		}else if (mCurrentState == DownLoadManager.STATE_ERROR || mCurrentState == DownLoadManager.STATE_PAUSE || mCurrentState == DownLoadManager.STATE_UNDO) {
			//开始下载
			mDM.download(getData());
		}else if (mCurrentState == DownLoadManager.STATE_SUCCESS) {
			//安装
			mDM.install(getData());
		}
	}

	@Override
	public void downLoadStateChange(DownloadInfo info) {
		
		AppInfo appInfo = getData();
		if(appInfo.id.equals(info.id)){
			mCurrentState = info.currentState;
			refreshUIOnMainThread(info);
			
		}
		
		/*mCurrentState = info.currentState;
		mProgress = info.getProgress();
		refreshUIOnMainThread(info);*/
	}

	@Override
	public void onDownloadProgressChanged(DownloadInfo info) {
		
		AppInfo appInfo = getData();
		if(appInfo.id.equals(info.id)){
			mCurrentState = info.currentState;
			refreshUIOnMainThread(info);
			
		}
		
//		mCurrentState = info.currentState;
//		mProgress = info.getProgress();
//		refreshUIOnMainThread(info);
	}
	
	

}
