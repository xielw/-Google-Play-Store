package com.itheima.goolepay.ui.activity;

import com.itheima.goolepay.R;
import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.protocol.HomeDetailProtocol;
import com.itheima.goolepay.ui.holder.DetailAppInfoHolder;
import com.itheima.goolepay.ui.holder.DetailDesHolder;
import com.itheima.goolepay.ui.holder.DetailDownloadHolder;
import com.itheima.goolepay.ui.holder.DetailPicHolder;
import com.itheima.goolepay.ui.holder.DetailSafeHolder;
import com.itheima.goolepay.ui.view.LoadingPage;
import com.itheima.goolepay.ui.view.LoadingPage.ResultSate;
import com.itheima.goolepay.utils.UIUtils;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class HomeDetailActivity extends BaseActivity {

	private LoadingPage loadingPage;
	private String packageName;
	private AppInfo data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadingPage = new LoadingPage(this) {
			
			@Override
			public ResultSate onLoad() {
				
				return HomeDetailActivity.this.onLoad();
				
			}
			
			@Override
			public View onCreateSuccessView() {
				
				return HomeDetailActivity.this.onCreateSuccessView();
				
			}
		};
		
		setContentView(loadingPage);
		packageName = getIntent().getStringExtra("packageName");
		System.out.println("---packageName:" + packageName);
		loadingPage.loadData();
		
		initActionBar();
	}
	
	private void initActionBar() {
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("谷歌电子市场");
		actionBar.setHomeButtonEnabled(true);
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
	}

	public ResultSate onLoad(){
		HomeDetailProtocol protocol = new HomeDetailProtocol(packageName);
		data = protocol.getData(0);
		
		if(data == null){
			return ResultSate.STATE_ERROR;
		}else {
			return ResultSate.STATE_SUCCESS;
		}
	
		
	}
	
	public View onCreateSuccessView(){
		
		View view = UIUtils.inflate(R.layout.page_home_detail);
		//设置App信息
		FrameLayout flApp = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
		DetailAppInfoHolder appHolder = new DetailAppInfoHolder();
		flApp.addView(appHolder.getRootView());
		appHolder.setData(data);
		
		//设置安全信息
		FrameLayout flSafe = (FrameLayout) view.findViewById(R.id.fl_detail_safeinfo);
		DetailSafeHolder safeHolder = new DetailSafeHolder();
		flSafe.addView(safeHolder.getRootView());
		safeHolder.setData(data);
		
		//设置图片信息
		DetailPicHolder picHolder = new DetailPicHolder();
		HorizontalScrollView hsvPics = (HorizontalScrollView) view.findViewById(R.id.hsv_picsInfo);
		hsvPics.addView(picHolder.getRootView());
		picHolder.setData(data);
		
		//设置描叙信息
		FrameLayout flDes = (FrameLayout) view.findViewById(R.id.fl_detail_desinfo);
		DetailDesHolder desHolder = new DetailDesHolder();
		flDes.addView(desHolder.getRootView());
		desHolder.setData(data);
		
		//设置底部下载栏
		FrameLayout flDownload = (FrameLayout) view.findViewById(R.id.fl_detail_download);
		DetailDownloadHolder downloadHolder = new DetailDownloadHolder();
		flDownload.addView(downloadHolder.getRootView());
		downloadHolder.setData(data);
		return view;
		
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
