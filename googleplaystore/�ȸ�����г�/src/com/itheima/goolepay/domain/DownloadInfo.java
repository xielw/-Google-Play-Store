package com.itheima.goolepay.domain;

//http://cspro.org/

import java.io.File;

import com.itheima.goolepay.manager.DownLoadManager;
import android.os.Environment;

public class DownloadInfo {

	public String id;
	public String name;
	public String packageName;
	public long size;
	public String downloadUrl;
	public long currentPos; //当前下载位置
	public int currentState; //当前下载状态
	public String path; //下载到本地文件路径
	
	public static final String GOOGLE_MARKE = "GOOGLE_MARKE"; //scard根目录
	public static final String DOWNLOAD = "download"; //子文件夹名称，存放下载文件
	
	
	public float getProgress(){
		if(size == 0)
			return 0;
		return currentPos / (float)size;
	}
	
	//获取下载路径
	public static String getFilePath(String name){
		
		StringBuffer sb = new StringBuffer();
		String sdCared =Environment.getExternalStorageDirectory().getAbsolutePath();
		sb.append(sdCared);
		sb.append(File.separator);
		
		sb.append(GOOGLE_MARKE);
		sb.append(File.separator);
		sb.append(DOWNLOAD);
		if(createDir(sb.toString())){
		
			return sb.toString() + File.separator + name + ".apk";//返回文件路径
		}
		
		return null;
		
	}

	private static boolean createDir(String dir) {
		System.out.println("///:" + dir);
		File file = new File(dir);
		//文件夹不存在或者不是一个文件夹
		if(!file.exists() || !file.isDirectory()){
			boolean b = file.mkdirs();
			System.out.println("///b" + b);
			return b;
		}
		return true;//文件夹存在
	}
	
	public static DownloadInfo copy(AppInfo info){
		
		DownloadInfo downloadInfo = new DownloadInfo();
		if(info != null){
			downloadInfo.id = info.id;
			downloadInfo.name = info.name;
			downloadInfo.packageName = info.packageName;
			downloadInfo.size = info.size;
			downloadInfo.downloadUrl = info.downloadUrl;
			
			downloadInfo.currentPos = 0;
			downloadInfo.currentState = DownLoadManager.STATE_UNDO; //默认状态是未下载状态
			downloadInfo.path = getFilePath(info.name);
			System.out.println("+++copy"+downloadInfo.path);
		}
		
		return downloadInfo;
			
	}
	
	
	
	
}
