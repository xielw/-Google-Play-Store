package com.itheima.goolepay.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Intent;
import android.net.Uri;

import com.itheima.goolepay.domain.AppInfo;
import com.itheima.goolepay.domain.DownloadInfo;
import com.itheima.goolepay.http.HttpHelper;
import com.itheima.goolepay.http.HttpHelper.HttpResult;
import com.itheima.goolepay.utils.IOUtils;
import com.itheima.goolepay.utils.UIUtils;

/**
 * 下载管理器 -未下载- 等待下载- 正在下载- 暂时下载- 下载成功
 * 
 * @author xielianwu
 * 
 */
public class DownLoadManager {

	public static final int STATE_UNDO = 0; //下载未开始
	public static final int STATE_WAITING = 1; //等待下载
	public static final int STATE_DOWNLOADING = 2; //正在下载
	public static final int STATE_PAUSE = 3; //下载暂停
	public static final int STATE_ERROR = 4; //下载失败
	public static final int STATE_SUCCESS = 5; //下载成功
	

	private static DownLoadManager mDM = new DownLoadManager();
	private static ArrayList<DownLoadObserver> mObservers = new ArrayList<DownLoadManager.DownLoadObserver>();

	private ConcurrentHashMap<String, DownloadInfo> mDownLoadInfoHashMap = new ConcurrentHashMap<String, DownloadInfo>();
	// 下载任务集合
	private ConcurrentHashMap<String, DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String, DownloadTask>();

	public DownLoadManager() {

	}

	public static DownLoadManager getInstance() {
		return mDM;

	}

	// 2.注册观察者
	public void registerObserver(DownLoadObserver observer) {

		if (observer != null && !mObservers.contains(observer)) {
			mObservers.add(observer);
		}
	}

	// 3.注销观察者
	public void unregisterObserver(DownLoadObserver observer) {
		if (observer != null && mObservers.contains(observer)) {
			mObservers.remove(observer);
		}
	}

	/**
	 * 1.声明观察者的接口
	 * 
	 * @author xielianwu
	 * 
	 */
	public interface DownLoadObserver {

		// 下载状态发生变化
		public void downLoadStateChange(DownloadInfo info);

		// 下载进度发生变化
		public void onDownloadProgressChanged(DownloadInfo info);

	}

	// 5.通知下载状态发生变化
	public static void notifyDownLoadStateChange(DownloadInfo info) {

		for (DownLoadObserver observer : mObservers) {
			observer.downLoadStateChange(info);
		}

	}

	// 6.通知下载进度发生变化
	public static void notifyDownLoadProgress(DownloadInfo info) {

		for (DownLoadObserver observer : mObservers) {
			observer.onDownloadProgressChanged(info);
		}

	}

	// 开始下载
	public synchronized void download(AppInfo info) {
		// 如果对象第一次下载，需要创建一个新的DownloadInfo,从头下载
		// 如果之前下载过，要接着下载，实现断点续传
		
		DownloadInfo downloadInfo = mDownLoadInfoHashMap.get(info.id);
		if (downloadInfo == null) {
			downloadInfo = DownloadInfo.copy(info);
		}
		downloadInfo.currentState = DownLoadManager.STATE_WAITING;// 状态切换等待下载
		DownLoadManager.notifyDownLoadStateChange(downloadInfo);// 通知所有的观察者，状态发生变化了

		System.out.println(info.name + "等待下载了");
		// 将下载对象放入集合中
		mDownLoadInfoHashMap.put(info.id, downloadInfo);

		// 初始化下载任务,并放入线程池中运行
		DownloadTask task = new DownloadTask(downloadInfo);
		ThreadPool.getThreadPool().execute(task);
		// 将下载任务放在集合中
		mDownloadTaskMap.put(downloadInfo.id, task);
	}

	class DownloadTask implements Runnable {

		DownloadInfo info;

		public DownloadTask(DownloadInfo info) {
			this.info = info;
		}

		@Override
		public void run() {

			
			HttpResult httpResult = null;
			System.out.println(info.name + "开始下载了");
			// 状态切换成为正在下载
			info.currentState = DownLoadManager.STATE_DOWNLOADING;
			DownLoadManager.notifyDownLoadStateChange(info);
		
			File file = new File(info.path);
			
			if (!file.exists() || file.length() != info.currentPos
					|| info.currentPos == 0) {
				// 从头开始下载
				file.delete(); // 文件不存在也可以删除，只不过没有效果而已
				info.currentPos = 0; // 当前下载位置为0
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + info.downloadUrl);

			} else {
				// 断点续传
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + info.downloadUrl + "&range="
						+ file.length());
				System.out.println("/// 断点续传" +httpResult );
			}

			if (httpResult != null && httpResult.getInputStream() != null) {
				InputStream in = httpResult.getInputStream();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file, true);
					int len = 0;
					byte[] buffer = new byte[1024];

					// 只有状态是正在下载，才继续轮询，解决下载过程中中途暂停问题，
					System.out.println("///run---" + info.currentState);
						while ((len = in.read(buffer)) != -1
								&& info.currentState == DownLoadManager.STATE_DOWNLOADING) {
							
							fos.write(buffer, 0, len);
							fos.flush();
							info.currentPos += len;
							DownLoadManager.notifyDownLoadProgress(info);
						
						}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.close(fos);
					IOUtils.close(in);

				}
					
				// 文件下载结束
				if (file.length() == info.size) {
					// 文件完整，表示下载成功
					info.currentState = DownLoadManager.STATE_SUCCESS;
					System.out.println("///DownLoadManager---下载成功");
					DownLoadManager.notifyDownLoadStateChange(info);
				} else if (info.currentState == DownLoadManager.STATE_PAUSE) {
					System.out.println("///DownLoadManager---暂停");
					DownLoadManager.notifyDownLoadStateChange(info);
				} else {
					// 下载失败
					file.delete();
					System.out.println("///DownLoadManager---下载失败");
					info.currentState = DownLoadManager.STATE_ERROR;
					info.currentPos = 0;
					DownLoadManager.notifyDownLoadStateChange(info);
					
				}

			} else {
				// 网络出现异常
				// 删除无效文件
				file.delete();
				System.out.println("///网络出现异常");
				info.currentState = DownLoadManager.STATE_ERROR;
				info.currentPos = 0;
				DownLoadManager.notifyDownLoadStateChange(info);
			}

			// 不管下载成功,失败还是暂停, 下载任务已经结束,都需要从当前任务集
			mDownloadTaskMap.remove(info.id);
			System.out.println("///run---" + info.currentState);
		}

	}

	// 下载暂停
	public synchronized void pause(AppInfo info) {
		DownloadInfo downloadInfo = mDownLoadInfoHashMap.get(info.id);
		if (downloadInfo != null) {
			// 只有正在下载，或者等待下载，才能暂停
			if (downloadInfo.currentState == DownLoadManager.STATE_WAITING
					|| downloadInfo.currentState == DownLoadManager.STATE_DOWNLOADING) {
				downloadInfo.currentState = DownLoadManager.STATE_PAUSE;
				DownLoadManager.notifyDownLoadStateChange(downloadInfo);
				DownloadTask task = mDownloadTaskMap.get(downloadInfo.id);
				if (task != null) {
					// 移除下载任务，如果任务还没有开始，正在等待，可以不通过此方法移除
					// 如果任务已经开始运行,需要在run方法里面运行中断
					ThreadPool.getThreadPool().cancle(task);
				}

			}
		}
	}

	// 开始安装
	public synchronized void install(AppInfo info) {

		System.out.println("///install");
		DownloadInfo downloadInfo = mDownLoadInfoHashMap.get(info.id);
		if (downloadInfo != null) {
			// 跳到系统的安装页面进行安装
			System.out.println("///install页面进行安装");
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // application/vnd.android.package-archive
			intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
					"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(intent);
		}
	}
	
	//根据应用信息返回下载对象
	public DownloadInfo getDownloadInfo(AppInfo info){
		return mDownLoadInfoHashMap.get(info.id);
	}

	
}
